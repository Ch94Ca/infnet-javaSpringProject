package br.edu.infnet.carlos_araujo.application.service;

import br.edu.infnet.carlos_araujo.application.exception.DuplicateEmailException;
import br.edu.infnet.carlos_araujo.application.exception.EmailNotExistException;
import br.edu.infnet.carlos_araujo.application.exception.InvalidCredentialsException;
import br.edu.infnet.carlos_araujo.application.port.in.UserService;
import br.edu.infnet.carlos_araujo.application.port.out.UserRepositoryPort;
import br.edu.infnet.carlos_araujo.application.use_case.user.UserCreateCommand;
import br.edu.infnet.carlos_araujo.application.use_case.user.UserUpdateCommand;
import br.edu.infnet.carlos_araujo.domain.enums.Role;
import br.edu.infnet.carlos_araujo.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserCreateCommand userCreateCommand) {
        boolean emailExists = userRepositoryPort.existsByEmail(userCreateCommand.email());

        if (emailExists) {
            throw new DuplicateEmailException("Error: Email (" + userCreateCommand.email() + ") already registered.");
        }

        return createAndSaveNewUser(userCreateCommand);
    }

    private User createAndSaveNewUser(UserCreateCommand userCreateCommand) {
        User newUser = new User();
        newUser.setName(userCreateCommand.name());
        newUser.setEmail(userCreateCommand.email());
        newUser.setPassword(passwordEncoder.encode(userCreateCommand.password()));
        newUser.setUserRole(Role.ROLE_USER);
        newUser.setActive(true);
        return userRepositoryPort.save(newUser);
    }

    @Override
    public User updateUserData(String email, UserUpdateCommand userUpdateCommand) {
        User domainUser = findUserByEmail(email);

        updateUserName(domainUser, userUpdateCommand.name());
        updateUserEmail(domainUser, email, userUpdateCommand.email());

        return userRepositoryPort.save(domainUser);
    }

    private User findUserByEmail(String email) {
        return userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new EmailNotExistException(
                        "Error: The email provided (" + email + ") does not exist."));
    }

    private void updateUserName(User user, String newName) {
        if (newName != null && !newName.isBlank()) {
            user.setName(newName);
        }
    }

    private void updateUserEmail(User user, String currentEmail, String newEmail) {
        if (newEmail == null || newEmail.isBlank()) {
            return;
        }

        boolean isDifferentEmail = !currentEmail.equalsIgnoreCase(newEmail);
        boolean emailAlreadyExists = Boolean.TRUE.equals(userRepositoryPort.existsByEmail(newEmail));

        if (isDifferentEmail && emailAlreadyExists) {
            throw new DuplicateEmailException(
                    "Error: The new email (" + newEmail + ") is already in use.");
        }

        user.setEmail(newEmail);
    }

    @Override
    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new EmailNotExistException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepositoryPort.save(user);
    }
}