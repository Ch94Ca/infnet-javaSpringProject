package br.edu.infnet.CarlosAraujo.application.useCase;

public record UserUpdateCommand(
        String name,
        String email
) {
}
