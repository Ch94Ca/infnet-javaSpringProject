package br.edu.infnet.carlos_araujo.application.service;

import br.edu.infnet.carlos_araujo.application.exception.EmailNotExistException;
import br.edu.infnet.carlos_araujo.application.port.in.AdminService;
import br.edu.infnet.carlos_araujo.application.port.in.UserService;
import br.edu.infnet.carlos_araujo.application.port.out.UserRepositoryPort;
import br.edu.infnet.carlos_araujo.application.use_case.UserUpdateCommand;
import br.edu.infnet.carlos_araujo.domain.enums.Role;
import br.edu.infnet.carlos_araujo.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

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
        return userService.updateUserData(email, userUpdateCommand);
    }

    @Override
    public User changeUserRole(String email, Role newRole) {
        User domainUser = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new EmailNotExistException("Error: The email provided (" + email + ") does not exist."));

        domainUser.setUserRole(newRole);

        return userRepositoryPort.save(domainUser);
    }

    @Override
    public User changeUserActiveStatus(String email, boolean newActiveStatus) {
        User domainUser = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new EmailNotExistException("Error: The email provided (" + email + ") does not exist."));

        domainUser.setActive(newActiveStatus);

        return userRepositoryPort.save(domainUser);
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepositoryPort.findAllUsers(pageable);
    }

    @Override
    public void initializeAdminUser() {
        boolean adminExists = userRepositoryPort.existsByUserRole(Role.ROLE_ADMIN);

        if (!adminExists) {
            createAndSaveAdminUser();
        }
    }

    private void createAndSaveAdminUser() {
        User adminUser = new User();
        adminUser.setName("Admin");
        adminUser.setEmail(adminEmail);
        adminUser.setPassword(passwordEncoder.encode(adminPassword));
        adminUser.setUserRole(Role.ROLE_ADMIN);
        adminUser.setActive(true);
        userRepositoryPort.save(adminUser);
    }
}