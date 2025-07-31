package br.edu.infnet.carlos_araujo.adapters.in.web.dto;

import br.edu.infnet.carlos_araujo.domain.enums.Role;
import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateDTO(
        @NotNull
        Role newRole
) {}
