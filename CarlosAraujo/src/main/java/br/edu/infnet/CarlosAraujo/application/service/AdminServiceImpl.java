package br.edu.infnet.CarlosAraujo.application.service;

import br.edu.infnet.CarlosAraujo.application.exception.DuplicateEmailException;
import br.edu.infnet.CarlosAraujo.application.exception.EmailNotExistException;
import br.edu.infnet.CarlosAraujo.application.port.in.AdminService;
import br.edu.infnet.CarlosAraujo.application.port.out.UserRepositoryPort;
import br.edu.infnet.CarlosAraujo.application.useCase.UserUpdateCommand;
import br.edu.infnet.CarlosAraujo.domain.enums.Role;
import br.edu.infnet.CarlosAraujo.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.initial.admin.email}")
    private String adminEmail;

    @Value("${app.initial.admin.password}")
    private String adminPassword;

    @Override
    public void deleteUserByEmail(String email) {
        long deletedCount = userRepositoryPort.deleteByEmail(email);
        if (deletedCount == 0) {
            throw new EmailNotExistException("Error: The email provided (" + email + ") does not exist.");
        }
    }

    @Override
    public User updateUserData(String email, UserUpdateCommand userUpdateCommand) {
        User domainUser = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new EmailNotExistException("Error: The email provided (" + email + ") does not exist."));

        if (userUpdateCommand.name() != null && !userUpdateCommand.name().isBlank()) {
            domainUser.setName(userUpdateCommand.name());
        }
        if (userUpdateCommand.email() != null && !userUpdateCommand.email().isBlank()) {
            if (!email.equalsIgnoreCase(userUpdateCommand.email()) && userRepositoryPort.existsByEmail(userUpdateCommand.email())) {
                throw new DuplicateEmailException("Error: The new email (" + userUpdateCommand.email() + ") is already in use.");
            }
            domainUser.setEmail(userUpdateCommand.email());
        }

        return userRepositoryPort.save(domainUser);
    }


    @Override
    public User changeUserRole(String email, Role newRole) {
        User domainUser = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new EmailNotExistException("Error: The email provided (" + email + ") does not exist."));

        domainUser.setUserRole(newRole);

        return userRepositoryPort.save(domainUser);
    }

    @Override
    public void initializeAdminUser() {
        if (userRepositoryPort.existsByUserRole(Role.ROLE_ADMIN)) {
            return;
        }
        User adminUser = new User();
        adminUser.setName("Admin");
        adminUser.setEmail(adminEmail);
        adminUser.setPassword(passwordEncoder.encode(adminPassword));
        adminUser.setUserRole(Role.ROLE_ADMIN);
        adminUser.setActive(true);
        userRepositoryPort.save(adminUser);
    }
}