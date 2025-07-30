package br.edu.infnet.CarlosAraujo.adapters.in.web.dto;

import jakarta.validation.constraints.Email;

public record UserDataUpdateDTO(
        String name,

        @Email(message = "Invalid email format")
        String email
) {}

