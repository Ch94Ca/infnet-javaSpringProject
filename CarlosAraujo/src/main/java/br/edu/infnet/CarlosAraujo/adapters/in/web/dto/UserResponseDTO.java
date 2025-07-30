package br.edu.infnet.CarlosAraujo.adapters.in.web.dto;

import br.edu.infnet.CarlosAraujo.domain.enums.Role;
import jakarta.validation.constraints.Email;

public record UserResponseDTO(
        Long id,

        String name,

        @Email(message = "Invalid email format")
        String email,

        Role role
) {}

