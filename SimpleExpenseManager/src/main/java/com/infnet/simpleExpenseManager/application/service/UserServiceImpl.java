package com.infnet.simpleExpenseManager.application.service;

import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserCreateDTO;
import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserDataUpdateDTO;
import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserResponseDTO;
import com.infnet.simpleExpenseManager.application.exception.DuplicateEmailException;
import com.infnet.simpleExpenseManager.application.exception.EmailNotExistException;
import com.infnet.simpleExpenseManager.application.mapper.UserDtoMapper;
import com.infnet.simpleExpenseManager.application.port.in.UserService;
import com.infnet.simpleExpenseManager.application.port.out.UserRepositoryPort;
import com.infnet.simpleExpenseManager.domain.enums.Roles;
import com.infnet.simpleExpenseManager.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final UserDtoMapper userDtoMapper;

    @Autowired
    public UserServiceImpl(
            UserRepositoryPort userRepositoryPort,
            PasswordEncoder passwordEncoder,
            UserDtoMapper userDtoMapper
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.userDtoMapper = userDtoMapper;
    }

    @Override
    public UserResponseDTO createUser(UserCreateDTO userDTO) {
        if(userRepositoryPort.existsByEmail(userDTO.email())){
            throw new DuplicateEmailException("Error: Email (" + userDTO.email() + ") already registered.");
        }

        User newUser = userDtoMapper.toDomain(userDTO);

        newUser.setPassword(passwordEncoder.encode(userDTO.password()));
        newUser.setUserRole(Roles.ROLE_USER);
        newUser.setActive(true);
        newUser.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepositoryPort.save(newUser);

        return userDtoMapper.toResponseDto(savedUser);
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
}