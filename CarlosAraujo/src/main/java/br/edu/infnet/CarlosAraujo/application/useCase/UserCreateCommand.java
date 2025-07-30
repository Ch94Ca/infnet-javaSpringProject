package br.edu.infnet.CarlosAraujo.application.useCase;

public record UserCreateCommand(
        String name,
        String email,
        String password
) {
}
