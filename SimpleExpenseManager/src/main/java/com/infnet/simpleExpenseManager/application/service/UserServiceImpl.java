package com.infnet.simpleExpenseManager.application.service;

import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserCreateDTO;
import com.infnet.simpleExpenseManager.application.mapper.UserDtoMapper;
import com.infnet.simpleExpenseManager.application.port.in.UserService;
import com.infnet.simpleExpenseManager.application.port.out.UserRepositoryPort;
import com.infnet.simpleExpenseManager.domain.enums.Roles;
import com.infnet.simpleExpenseManager.application.exception.DuplicateEmailException;
import com.infnet.simpleExpenseManager.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    public User createUser(UserCreateDTO userDTO) {
        if(existsByEmail(userDTO.email())){
            throw new DuplicateEmailException("Error: Email (" + userDTO.email() + ") already registered.");
        }

        User newUser = userDtoMapper.toDomain(userDTO);

        newUser.setPassword(passwordEncoder.encode(userDTO.password()));
        newUser.setUserRole(Roles.ROLE_USER);
        newUser.setActive(true);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        userRepositoryPort.save(newUser);

        return newUser;
    }

    @Override
    public User findUserById(Long id) {
        return null;
    }

    @Override
    public List<User> findAllUsers() {
        return List.of();
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        return null;
    }

    @Override
    public void deleteUserById(Long id) {
    }

    private Boolean existsByEmail(String email){
        return userRepositoryPort.existsByEmail(email);
    }


}