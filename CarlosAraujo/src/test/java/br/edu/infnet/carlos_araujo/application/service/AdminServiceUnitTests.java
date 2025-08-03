package br.edu.infnet.carlos_araujo.application.service;

import br.edu.infnet.carlos_araujo.application.exception.EmailNotExistException;
import br.edu.infnet.carlos_araujo.application.port.in.UserService;
import br.edu.infnet.carlos_araujo.application.port.out.UserRepositoryPort;
import br.edu.infnet.carlos_araujo.domain.enums.Role;
import br.edu.infnet.carlos_araujo.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
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

    @Nested
    @DisplayName("changeUserActiveStatus Tests")
    class ChangeUserActiveStatusTests {

        @Test
        @DisplayName("Should activate user successfully")
        void changeUserActiveStatus_ShouldActivateUser_WhenUserExists() {
            // Arrange
            String email = "user@test.com";
            boolean newActiveStatus = true;

            User existingUser = new User(
                    1L,
                    "Carlos",
                    "carlos@mail.com",
                    "12345678",
                    Role.ROLE_USER,
                    false,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

            when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(existingUser));
            when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            User result = adminService.changeUserActiveStatus(email, newActiveStatus);

            // Assert
            verify(userRepositoryPort, times(1)).findByEmail(email);
            verify(userRepositoryPort, times(1)).save(userCaptor.capture());

            User savedUser = userCaptor.getValue();
            assertTrue(savedUser.isActive());
            assertEquals(newActiveStatus, result.isActive());
        }

        @Test
        @DisplayName("Should deactivate user successfully")
        void changeUserActiveStatus_ShouldDeactivateUser_WhenUserExists() {
            // Arrange
            String email = "user@test.com";
            boolean newActiveStatus = false;

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
            User result = adminService.changeUserActiveStatus(email, newActiveStatus);

            // Assert
            verify(userRepositoryPort, times(1)).findByEmail(email);
            verify(userRepositoryPort, times(1)).save(userCaptor.capture());

            User savedUser = userCaptor.getValue();
            assertFalse(savedUser.isActive());
            assertEquals(newActiveStatus, result.isActive());
        }

        @Test
        @DisplayName("Should throw EmailNotExistException when user to change status is not found")
        void changeUserActiveStatus_ShouldThrowException_WhenUserNotFound() {
            // Arrange
            String email = "nonexistent@test.com";
            boolean newActiveStatus = true;

            when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.empty());

            // Act & Assert
            EmailNotExistException exception = assertThrows(
                    EmailNotExistException.class,
                    () -> adminService.changeUserActiveStatus(email, newActiveStatus)
            );

            assertEquals("Error: The email provided (" + email + ") does not exist.", exception.getMessage());
            verify(userRepositoryPort, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("getUsers Tests")
    class GetUsersTests {

        @Test
        @DisplayName("Should return paginated users successfully")
        void getUsers_ShouldReturnPaginatedUsers() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);

            List<User> userList = List.of(
                    new User(1L, "User1", "user1@test.com", "pass1", Role.ROLE_USER, true, LocalDateTime.now(), LocalDateTime.now()),
                    new User(2L, "User2", "user2@test.com", "pass2", Role.ROLE_USER, true, LocalDateTime.now(), LocalDateTime.now()),
                    new User(3L, "Admin", "admin@test.com", "pass3", Role.ROLE_ADMIN, true, LocalDateTime.now(), LocalDateTime.now())
            );

            Page<User> expectedPage = new PageImpl<>(userList, pageable, userList.size());

            when(userRepositoryPort.findAllUsers(pageable)).thenReturn(expectedPage);

            // Act
            Page<User> result = adminService.getUsers(pageable);

            // Assert
            assertNotNull(result);
            assertEquals(3, result.getTotalElements());
            assertEquals(3, result.getContent().size());
            assertEquals(userList, result.getContent());
            assertEquals(pageable, result.getPageable());

            verify(userRepositoryPort, times(1)).findAllUsers(pageable);
        }

        @Test
        @DisplayName("Should return empty page when no users exist")
        void getUsers_ShouldReturnEmptyPage_WhenNoUsersExist() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            Page<User> emptyPage = new PageImpl<>(List.of(), pageable, 0);

            when(userRepositoryPort.findAllUsers(pageable)).thenReturn(emptyPage);

            // Act
            Page<User> result = adminService.getUsers(pageable);

            // Assert
            assertNotNull(result);
            assertEquals(0, result.getTotalElements());
            assertTrue(result.getContent().isEmpty());
            assertEquals(pageable, result.getPageable());

            verify(userRepositoryPort, times(1)).findAllUsers(pageable);
        }

        @Test
        @DisplayName("Should handle different page sizes correctly")
        void getUsers_ShouldHandleDifferentPageSizes() {
            // Arrange
            Pageable pageable = PageRequest.of(1, 2);

            List<User> userList = List.of(
                    new User(3L, "User3", "user3@test.com", "pass3", Role.ROLE_USER, true, LocalDateTime.now(), LocalDateTime.now()),
                    new User(4L, "User4", "user4@test.com", "pass4", Role.ROLE_USER, false, LocalDateTime.now(), LocalDateTime.now())
            );

            Page<User> expectedPage = new PageImpl<>(userList, pageable, 10);

            when(userRepositoryPort.findAllUsers(pageable)).thenReturn(expectedPage);

            // Act
            Page<User> result = adminService.getUsers(pageable);

            // Assert
            assertNotNull(result);
            assertEquals(10, result.getTotalElements());
            assertEquals(2, result.getContent().size());
            assertEquals(1, result.getNumber());
            assertEquals(2, result.getSize());
            assertEquals(5, result.getTotalPages());

            verify(userRepositoryPort, times(1)).findAllUsers(pageable);
        }
    }
}

