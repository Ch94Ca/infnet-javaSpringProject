package br.edu.infnet.carlos_araujo.adapters.in.web.dto.user;

import jakarta.validation.constraints.Email;

public record UserDataUpdateDTO(
        String name,

        @Email(message = "Invalid email format")
        String email
) {}

