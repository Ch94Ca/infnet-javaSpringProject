package br.edu.infnet.CarlosAraujo.application.useCase;

public record LoginCommand(
        String email,
        String password
) {
}
