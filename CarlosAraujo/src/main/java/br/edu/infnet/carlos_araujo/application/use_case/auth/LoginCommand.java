package br.edu.infnet.carlos_araujo.application.use_case.auth;

public record LoginCommand(
        String email,
        String password
) {
}
