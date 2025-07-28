package com.infnet.simpleExpenseManager.application.service;

import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserDataUpdateDTO;
import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserResponseDTO;
import com.infnet.simpleExpenseManager.application.exception.DuplicateEmailException;
import com.infnet.simpleExpenseManager.application.exception.EmailNotExistException;
import com.infnet.simpleExpenseManager.application.mapper.UserDtoMapper;
import com.infnet.simpleExpenseManager.application.port.in.AdminService;
import com.infnet.simpleExpenseManager.application.port.out.UserRepositoryPort;
import com.infnet.simpleExpenseManager.domain.enums.Roles;
import com.infnet.simpleExpenseManager.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepositoryPort userRepositoryPort;
    private final UserDtoMapper userDtoMapper;

    @Autowired
    public AdminServiceImpl(
            UserRepositoryPort userRepositoryPort,
            PasswordEncoder passwordEncoder,
            UserDtoMapper userDtoMapper
    ) {
        this.userRepositoryPort = userRepositoryPort;
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
    public UserResponseDTO changeUserRole(String email, Roles newRole) {
        User domainUser = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new EmailNotExistException("Error: The email provided (" + email + ") does not exist."));

        domainUser.setUserRole(newRole);

        User updatedUser = userRepositoryPort.save(domainUser);
        return userDtoMapper.toResponseDto(updatedUser);
    }
}