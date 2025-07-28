package com.infnet.simpleExpenseManager.application.service.security;

import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserCreateDTO;
import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserResponseDTO;
import com.infnet.simpleExpenseManager.adapters.in.web.dto.auth.AuthResponseDTO;
import com.infnet.simpleExpenseManager.adapters.in.web.dto.auth.LoginRequestDTO;
import com.infnet.simpleExpenseManager.application.port.in.UserService;
import com.infnet.simpleExpenseManager.application.port.out.UserRepositoryPort;
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
