package br.edu.infnet.carlos_araujo.application.service;


import br.edu.infnet.carlos_araujo.application.port.out.UserRepositoryPort;
import br.edu.infnet.carlos_araujo.application.service.security.AuthenticationService;
import br.edu.infnet.carlos_araujo.application.service.security.JwtService;
import br.edu.infnet.carlos_araujo.application.use_case.LoginCommand;
import br.edu.infnet.carlos_araujo.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceUnitTests
{

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void authenticate_ShouldReturnJwtToken_WhenCredentialsAreValid() {
        // Arrange
        LoginCommand command = new LoginCommand("carlos@email.com", "password123");
        User user = new User();
        user.setEmail(command.email());
        String expectedToken = "fake.jwt.token";

        when(authenticationManager.authenticate(any())).thenReturn(null);

        when(userRepositoryPort.findByEmail(command.email())).thenReturn(Optional.of(user));

        when(jwtService.generateToken(user)).thenReturn(expectedToken);

        // Act & Assert
        String actualToken = authenticationService.authenticate(command);

        assertNotNull(actualToken);
        assertEquals(expectedToken, actualToken);
    }
}
