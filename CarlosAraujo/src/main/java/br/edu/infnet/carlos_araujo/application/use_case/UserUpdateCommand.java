package br.edu.infnet.carlos_araujo.application.use_case;

public record UserUpdateCommand(
        String name,
        String email
) {
}
