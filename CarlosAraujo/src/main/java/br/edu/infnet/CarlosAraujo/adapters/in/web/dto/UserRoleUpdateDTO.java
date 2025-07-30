package br.edu.infnet.CarlosAraujo.adapters.in.web.dto;

import br.edu.infnet.CarlosAraujo.domain.enums.Roles;
import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateDTO(
        @NotNull
        Roles newRole
) {}
