package br.edu.infnet.CarlosAraujo.application.service.security;

import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserCreateDTO;
import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserResponseDTO;
import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.auth.AuthResponseDTO;
import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.auth.LoginRequestDTO;
import br.edu.infnet.CarlosAraujo.application.port.in.UserService;
import br.edu.infnet.CarlosAraujo.application.port.out.UserRepositoryPort;
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

    public UserResponseDTO register(UserCreateDTO request) {
        return userService.createUser(request);
    }

    public AuthResponseDTO authenticate(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = userRepositoryPort.findByEmail(request.email())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return new AuthResponseDTO(jwtToken);
    }
}
