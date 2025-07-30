package br.edu.infnet.CarlosAraujo.application.service;

import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserCreateDTO;
import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserDataUpdateDTO;
import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserResponseDTO;
import br.edu.infnet.CarlosAraujo.application.exception.DuplicateEmailException;
import br.edu.infnet.CarlosAraujo.application.exception.EmailNotExistException;
import br.edu.infnet.CarlosAraujo.application.mapper.UserDtoMapper;
import br.edu.infnet.CarlosAraujo.application.port.in.UserService;
import br.edu.infnet.CarlosAraujo.application.port.out.UserRepositoryPort;
import br.edu.infnet.CarlosAraujo.domain.enums.Role;
import br.edu.infnet.CarlosAraujo.domain.user.User;
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
        newUser.setUserRole(Role.ROLE_USER);
        newUser.setActive(true);

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