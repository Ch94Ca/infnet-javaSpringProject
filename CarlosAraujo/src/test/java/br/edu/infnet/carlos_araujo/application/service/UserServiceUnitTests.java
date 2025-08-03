package br.edu.infnet.carlos_araujo.application.service;

import br.edu.infnet.carlos_araujo.application.exception.DuplicateEmailException;
import br.edu.infnet.carlos_araujo.application.exception.EmailNotExistException;
import br.edu.infnet.carlos_araujo.application.exception.InvalidCredentialsException;
import br.edu.infnet.carlos_araujo.application.port.out.UserRepositoryPort;
import br.edu.infnet.carlos_araujo.application.use_case.user.UserCreateCommand;
import br.edu.infnet.carlos_araujo.application.use_case.user.UserUpdateCommand;
import br.edu.infnet.carlos_araujo.domain.enums.Role;
import br.edu.infnet.carlos_araujo.domain.User;
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
            UserUpdateCommand command = new UserUpdateCommand(newName, null);

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

    @Nested
    @DisplayName("changePassword Tests")
    class ChangePasswordTests {

        @Test
        @DisplayName("Should change password successfully when current password is correct")
        void changePassword_ShouldSucceed_WhenCurrentPasswordIsCorrect() {
            // Arrange
            String email = "user@test.com";
            String currentPassword = "oldPassword123";
            String newPassword = "newPassword456";
            String encodedOldPassword = "encodedOldPassword";
            String encodedNewPassword = "encodedNewPassword";

            User existingUser = new User(
                    1L,
                    "Carlos",
                    email,
                    encodedOldPassword,
                    Role.ROLE_USER,
                    true,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

            when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(existingUser));
            when(passwordEncoder.matches(currentPassword, encodedOldPassword)).thenReturn(true);
            when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);
            when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            userService.changePassword(email, currentPassword, newPassword);

            // Assert
            verify(userRepositoryPort, times(1)).findByEmail(email);
            verify(passwordEncoder, times(1)).matches(currentPassword, encodedOldPassword);
            verify(passwordEncoder, times(1)).encode(newPassword);
            verify(userRepositoryPort, times(1)).save(userCaptor.capture());

            User savedUser = userCaptor.getValue();
            assertEquals(encodedNewPassword, savedUser.getPassword());
            assertEquals(email, savedUser.getEmail());
            assertEquals("Carlos", savedUser.getName());
        }

        @Test
        @DisplayName("Should throw EmailNotExistException when user is not found")
        void changePassword_ShouldThrowException_WhenUserNotFound() {
            // Arrange
            String email = "nonexistent@test.com";
            String currentPassword = "oldPassword123";
            String newPassword = "newPassword456";

            when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.empty());

            // Act & Assert
            EmailNotExistException exception = assertThrows(
                    EmailNotExistException.class,
                    () -> userService.changePassword(email, currentPassword, newPassword)
            );

            assertEquals("User not found", exception.getMessage());
            verify(userRepositoryPort, times(1)).findByEmail(email);
            verify(passwordEncoder, never()).matches(anyString(), anyString());
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepositoryPort, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw InvalidCredentialsException when current password is incorrect")
        void changePassword_ShouldThrowException_WhenCurrentPasswordIsIncorrect() {
            // Arrange
            String email = "user@test.com";
            String currentPassword = "wrongPassword";
            String newPassword = "newPassword456";
            String encodedOldPassword = "encodedOldPassword";

            User existingUser = new User(
                    1L,
                    "Carlos",
                    email,
                    encodedOldPassword,
                    Role.ROLE_USER,
                    true,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(existingUser));
            when(passwordEncoder.matches(currentPassword, encodedOldPassword)).thenReturn(false);

            // Act & Assert
            InvalidCredentialsException exception = assertThrows(
                    InvalidCredentialsException.class,
                    () -> userService.changePassword(email, currentPassword, newPassword)
            );

            assertEquals("Current password is incorrect", exception.getMessage());
            verify(userRepositoryPort, times(1)).findByEmail(email);
            verify(passwordEncoder, times(1)).matches(currentPassword, encodedOldPassword);
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepositoryPort, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should not change password when current and new passwords are the same")
        void changePassword_ShouldSucceed_WhenCurrentAndNewPasswordsAreSame() {
            // Arrange
            String email = "user@test.com";
            String password = "samePassword123";
            String encodedPassword = "encodedSamePassword";
            String encodedNewPassword = "encodedNewSamePassword";

            User existingUser = new User(
                    1L,
                    "Carlos",
                    email,
                    encodedPassword,
                    Role.ROLE_USER,
                    true,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

            when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(existingUser));
            when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
            when(passwordEncoder.encode(password)).thenReturn(encodedNewPassword);
            when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            userService.changePassword(email, password, password);

            // Assert
            verify(userRepositoryPort, times(1)).findByEmail(email);
            verify(passwordEncoder, times(1)).matches(password, encodedPassword);
            verify(passwordEncoder, times(1)).encode(password);
            verify(userRepositoryPort, times(1)).save(userCaptor.capture());

            User savedUser = userCaptor.getValue();
            assertEquals(encodedNewPassword, savedUser.getPassword());
        }

        @Test
        @DisplayName("Should verify password encoding is called with correct parameters")
        void changePassword_ShouldCallPasswordEncodingCorrectly() {
            // Arrange
            String email = "user@test.com";
            String currentPassword = "current123";
            String newPassword = "newPassword789";
            String encodedCurrentPassword = "encodedCurrent";
            String encodedNewPassword = "encodedNew";

            User existingUser = new User(
                    1L,
                    "Test User",
                    email,
                    encodedCurrentPassword,
                    Role.ROLE_USER,
                    true,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(existingUser));
            when(passwordEncoder.matches(currentPassword, encodedCurrentPassword)).thenReturn(true);
            when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);
            when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            userService.changePassword(email, currentPassword, newPassword);

            // Assert
            verify(passwordEncoder, times(1)).matches(
                    currentPassword,
                    encodedCurrentPassword
            );
            verify(passwordEncoder, times(1)).encode(newPassword);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepositoryPort, times(1)).save(userCaptor.capture());

            User savedUser = userCaptor.getValue();
            assertEquals(encodedNewPassword, savedUser.getPassword());
            assertEquals(email, savedUser.getEmail());
        }
    }
}
