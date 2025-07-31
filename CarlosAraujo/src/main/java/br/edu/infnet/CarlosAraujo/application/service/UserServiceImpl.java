package br.edu.infnet.CarlosAraujo.application.service;

import br.edu.infnet.CarlosAraujo.application.exception.DuplicateEmailException;
import br.edu.infnet.CarlosAraujo.application.exception.EmailNotExistException;
import br.edu.infnet.CarlosAraujo.application.port.in.UserService;
import br.edu.infnet.CarlosAraujo.application.port.out.UserRepositoryPort;
import br.edu.infnet.CarlosAraujo.application.useCase.UserCreateCommand;
import br.edu.infnet.CarlosAraujo.application.useCase.UserUpdateCommand;
import br.edu.infnet.CarlosAraujo.domain.enums.Role;
import br.edu.infnet.CarlosAraujo.domain.user.User;
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
        if(userRepositoryPort.existsByEmail(userCreateCommand.email())){
            throw new DuplicateEmailException("Error: Email (" + userCreateCommand.email() + ") already registered.");
        }

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
}