package br.edu.infnet.carlos_araujo.application.service;

import br.edu.infnet.carlos_araujo.application.exception.EmailNotExistException;
import br.edu.infnet.carlos_araujo.application.port.in.UserService;
import br.edu.infnet.carlos_araujo.application.port.out.UserRepositoryPort;
import br.edu.infnet.carlos_araujo.domain.enums.Role;
import br.edu.infnet.carlos_araujo.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceUnitTests {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(adminService, "adminEmail", "admin@test.com");
        ReflectionTestUtils.setField(adminService, "adminPassword", "secretPassword");
    }

    @Nested
    @DisplayName("deleteUserByEmail Tests")
    class DeleteUserByEmailTests {

        @Test
        @DisplayName("Should delete user when email exists")
        void deleteUserByEmail_ShouldSucceed_WhenEmailExists() {
            // Arrange
            String email = "user@test.com";
            when(userRepositoryPort.deleteByEmail(email)).thenReturn(1L);

            // Act & Assert
            assertDoesNotThrow(() -> adminService.deleteUserByEmail(email));
            verify(userRepositoryPort, times(1)).deleteByEmail(email);
        }

        @Test
        @DisplayName("Should throw EmailNotExistException when email does not exist")
        void deleteUserByEmail_ShouldThrowException_WhenEmailDoesNotExist() {
            // Arrange
            String email = "nonexistent@test.com";
            when(userRepositoryPort.deleteByEmail(email)).thenReturn(0L);

            // Act & Assert
            assertThrows(EmailNotExistException.class, () -> adminService.deleteUserByEmail(email));
            verify(userRepositoryPort, times(1)).deleteByEmail(email);
        }
    }

    @Nested
    @DisplayName("changeUserRole Tests")
    class ChangeUserRoleTests {

        @Test
        @DisplayName("Should change user role successfully")
        void changeUserRole_ShouldSucceed() {
            // Arrange
            String email = "user@test.com";
            Role newRole = Role.ROLE_ADMIN;
            User existingUser = new User(
                    1L,
                    "Carlos",
                    "carlos@mail.com",
                    "12345678",
                    Role.ROLE_USER,
                    true,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

            when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(existingUser));
            when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            adminService.changeUserRole(email, newRole);

            // Assert
            verify(userRepositoryPort, times(1)).save(userCaptor.capture());
            User savedUser = userCaptor.getValue();
            assertEquals(newRole, savedUser.getUserRole());
        }

        @Test
        @DisplayName("Should throw EmailNotExistException when user to change role is not found")
        void changeUserRole_ShouldThrowException_WhenUserNotFound() {
            // Arrange
            String email = "nonexistent@test.com";
            when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EmailNotExistException.class, () -> adminService.changeUserRole(email, Role.ROLE_ADMIN));
            verify(userRepositoryPort, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("initializeAdminUser Tests")
    class InitializeAdminUserTests {

        @Test
        @DisplayName("Should create admin user if none exists")
        void initializeAdminUser_ShouldCreateAdmin_WhenNoneExists() {
            // Arrange
            when(userRepositoryPort.existsByUserRole(Role.ROLE_ADMIN)).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

            // Act
            adminService.initializeAdminUser();

            // Assert
            verify(passwordEncoder, times(1)).encode("secretPassword");
            verify(userRepositoryPort, times(1)).save(any(User.class));
        }

    }
}

