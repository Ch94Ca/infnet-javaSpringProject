package br.edu.infnet.CarlosAraujo.application.service;

import br.edu.infnet.CarlosAraujo.application.exception.DuplicateEmailException;
import br.edu.infnet.CarlosAraujo.application.exception.EmailNotExistException;
import br.edu.infnet.CarlosAraujo.application.port.out.UserRepositoryPort;
import br.edu.infnet.CarlosAraujo.application.useCase.UserCreateCommand;
import br.edu.infnet.CarlosAraujo.application.useCase.UserUpdateCommand;
import br.edu.infnet.CarlosAraujo.domain.enums.Role;
import br.edu.infnet.CarlosAraujo.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTests {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    @DisplayName("createUser Tests")
    class CreateUserTests {

        @Test
        @DisplayName("Should create user successfully when email is not in use")
        void createUser_ShouldSucceed_WhenEmailIsNew() {
            // Arrange
            UserCreateCommand command = new UserCreateCommand("New User", "new@test.com", "password123");
            when(userRepositoryPort.existsByEmail(command.email())).thenReturn(false);
            when(passwordEncoder.encode(command.password())).thenReturn("encodedPassword");

            when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> {
                User userToSave = invocation.getArgument(0);
                userToSave.setId(1L);
                return userToSave;
            });

            // Act
            User createdUser = userService.createUser(command);

            // Assert
            assertNotNull(createdUser);
            assertEquals(command.name(), createdUser.getName());
            assertEquals(command.email(), createdUser.getEmail());
            assertEquals("encodedPassword", createdUser.getPassword());
            assertEquals(Role.ROLE_USER, createdUser.getUserRole());
            assertTrue(createdUser.isActive());

            verify(userRepositoryPort, times(1)).existsByEmail(command.email());
            verify(passwordEncoder, times(1)).encode(command.password());
            verify(userRepositoryPort, times(1)).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw DuplicateEmailException when email is already registered")
        void createUser_ShouldThrowException_WhenEmailExists() {
            // Arrange
            UserCreateCommand command = new UserCreateCommand("Another User", "existing@test.com", "password123");
            when(userRepositoryPort.existsByEmail(command.email())).thenReturn(true);

            // Act & Assert
            assertThrows(DuplicateEmailException.class, () -> userService.createUser(command));

            verify(userRepositoryPort, never()).save(any(User.class));
            verify(passwordEncoder, never()).encode(anyString());
        }
    }

    @Nested
    @DisplayName("updateUserData Tests")
    class UpdateUserDataTests {

        @Test
        @DisplayName("Should throw EmailNotExistException when user to update is not found")
        void updateUserData_ShouldThrowException_WhenUserNotFound() {
            // Arrange
            String email = "nonexistent@test.com";
            UserUpdateCommand command = new UserUpdateCommand("New Name", "new.email@test.com");
            when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EmailNotExistException.class, () -> userService.updateUserData(email, command));
            verify(userRepositoryPort, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw DuplicateEmailException when new email is already in use by another user")
        void updateUserData_ShouldThrowException_WhenNewEmailIsDuplicate() {
            // Arrange
            String originalEmail = "user1@test.com";
            String newEmail = "user2@test.com";
            User existingUser = new User(
                    1L,
                    "Carlos",
                    originalEmail,
                    "12345678",
                    Role.ROLE_USER,
                    true,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            UserUpdateCommand command = new UserUpdateCommand("New Name", newEmail);

            when(userRepositoryPort.findByEmail(originalEmail)).thenReturn(Optional.of(existingUser));
            when(userRepositoryPort.existsByEmail(newEmail)).thenReturn(true);

            // Act & Assert
            assertThrows(DuplicateEmailException.class, () -> userService.updateUserData(originalEmail, command));
            verify(userRepositoryPort, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should update user data successfully")
        void updateUserData_ShouldSucceed() {
            // Arrange
            String originalEmail = "user@test.com";
            String newEmail = "new.user@test.com";
            String newName = "New Name";
            User existingUser = new User(
                    1L,
                    "Carlos",
                    originalEmail,
                    "12345678",
                    Role.ROLE_USER,
                    true,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            UserUpdateCommand command = new UserUpdateCommand(newName, newEmail);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

            when(userRepositoryPort.findByEmail(originalEmail)).thenReturn(Optional.of(existingUser));
            when(userRepositoryPort.existsByEmail(newEmail)).thenReturn(false);
            when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            User updatedUser = userService.updateUserData(originalEmail, command);

            // Assert
            assertNotNull(updatedUser);

            verify(userRepositoryPort, times(1)).save(userCaptor.capture());
            User savedUser = userCaptor.getValue();

            assertEquals(newName, savedUser.getName());
            assertEquals(newEmail, savedUser.getEmail());
        }

        @Test
        @DisplayName("Should update only name when email in command is null")
        void updateUserData_ShouldUpdateOnlyName_WhenEmailIsNull() {
            // Arrange
            String originalEmail = "user@test.com";
            String newName = "Only Name Updated";
            User existingUser = new User(
                    1L,
                    "Carlos",
                    originalEmail,
                    "12345678",
                    Role.ROLE_USER,
                    true,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            UserUpdateCommand command = new UserUpdateCommand(newName, null); // Email é null

            when(userRepositoryPort.findByEmail(originalEmail)).thenReturn(Optional.of(existingUser));
            when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            User updatedUser = userService.updateUserData(originalEmail, command);

            // Assert
            assertNotNull(updatedUser);
            assertEquals(newName, updatedUser.getName());
            assertEquals(originalEmail, updatedUser.getEmail());
            verify(userRepositoryPort, times(1)).save(any(User.class));
            verify(userRepositoryPort, never()).existsByEmail(anyString());
        }
    }
}
