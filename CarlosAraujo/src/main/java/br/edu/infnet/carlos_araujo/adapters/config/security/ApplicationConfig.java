package br.edu.infnet.carlos_araujo.adapters.config.security;

import br.edu.infnet.carlos_araujo.application.exception.EmailNotExistException;
import br.edu.infnet.carlos_araujo.application.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepositoryPort userRepositoryPort;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepositoryPort.findByEmail(username)
                .orElseThrow(() -> new EmailNotExistException("User not found with email: " + username));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
