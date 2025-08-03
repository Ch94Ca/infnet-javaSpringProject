package br.edu.infnet.carlos_araujo.application.use_case.user;

public record UserUpdateCommand(
        String name,
        String email
) {
}
