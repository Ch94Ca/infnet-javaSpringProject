package br.edu.infnet.CarlosAraujo.application.service.security;

import br.edu.infnet.CarlosAraujo.application.port.in.UserService;
import br.edu.infnet.CarlosAraujo.application.port.out.UserRepositoryPort;
import br.edu.infnet.CarlosAraujo.application.useCase.LoginCommand;
import br.edu.infnet.CarlosAraujo.application.useCase.UserCreateCommand;
import br.edu.infnet.CarlosAraujo.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final UserRepositoryPort userRepositoryPort;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public User register(UserCreateCommand userCreateCommand) {
        return userService.createUser(userCreateCommand);
    }

    public String authenticate(LoginCommand loginCommand) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginCommand.email(),
                        loginCommand.password()
                )
        );

        var user = userRepositoryPort.findByEmail(loginCommand.email())
                .orElseThrow();

        return jwtService.generateToken(user);
    }
}
