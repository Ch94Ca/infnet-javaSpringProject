package br.edu.infnet.CarlosAraujo.application.service;

import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserDataUpdateDTO;
import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserResponseDTO;
import br.edu.infnet.CarlosAraujo.application.exception.DuplicateEmailException;
import br.edu.infnet.CarlosAraujo.application.exception.EmailNotExistException;
import br.edu.infnet.CarlosAraujo.application.mapper.UserDtoMapper;
import br.edu.infnet.CarlosAraujo.application.port.in.AdminService;
import br.edu.infnet.CarlosAraujo.application.port.out.UserRepositoryPort;
import br.edu.infnet.CarlosAraujo.domain.enums.Role;
import br.edu.infnet.CarlosAraujo.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepositoryPort userRepositoryPort;
    private final UserDtoMapper userDtoMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.initial.admin.email}")
    private String adminEmail;

    @Value("${app.initial.admin.password}")
    private String adminPassword;

    @Autowired
    public AdminServiceImpl(
            UserRepositoryPort userRepositoryPort,
            PasswordEncoder passwordEncoder,
            UserDtoMapper userDtoMapper
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.userDtoMapper = userDtoMapper;
    }

    @Override
    public void deleteUserByEmail(String email) {
        long deletedCount = userRepositoryPort.deleteByEmail(email);
        if (deletedCount == 0) {
            throw new EmailNotExistException("Error: The email provided (" + email + ") does not exist.");
        }
    }

    @Override
    public UserResponseDTO updateUserData(String email, UserDataUpdateDTO userDTO) {
        User domainUser = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new EmailNotExistException("Error: The email provided (" + email + ") does not exist."));

        if (userDTO.name() != null && !userDTO.name().isBlank()) {
            domainUser.setName(userDTO.name());
        }
        if (userDTO.email() != null && !userDTO.email().isBlank()) {
            if (!email.equalsIgnoreCase(userDTO.email()) && userRepositoryPort.existsByEmail(userDTO.email())) {
                throw new DuplicateEmailException("Error: The new email (" + userDTO.email() + ") is already in use.");
            }
            domainUser.setEmail(userDTO.email());
        }

        User updatedUser = userRepositoryPort.save(domainUser);
        return userDtoMapper.toResponseDto(updatedUser);
    }


    @Override
    public UserResponseDTO changeUserRole(String email, Role newRole) {
        User domainUser = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new EmailNotExistException("Error: The email provided (" + email + ") does not exist."));

        domainUser.setUserRole(newRole);

        User updatedUser = userRepositoryPort.save(domainUser);
        return userDtoMapper.toResponseDto(updatedUser);
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