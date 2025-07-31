package br.edu.infnet.carlos_araujo.application.use_case;

public record LoginCommand(
        String email,
        String password
) {
}
