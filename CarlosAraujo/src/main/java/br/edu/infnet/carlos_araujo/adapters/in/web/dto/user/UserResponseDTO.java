package br.edu.infnet.carlos_araujo.adapters.in.web.dto.user;

import br.edu.infnet.carlos_araujo.domain.enums.Role;
import jakarta.validation.constraints.Email;

public record UserResponseDTO(
        Long id,

        String name,

        @Email(message = "Invalid email format")
        String email,

        Role role
) {}

