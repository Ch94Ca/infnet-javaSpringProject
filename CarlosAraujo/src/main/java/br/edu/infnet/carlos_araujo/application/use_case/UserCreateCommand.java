package br.edu.infnet.carlos_araujo.application.use_case;

public record UserCreateCommand(
        String name,
        String email,
        String password
) {
}
