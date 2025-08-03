package br.edu.infnet.carlos_araujo.adapters.in.web.dto.user;

import jakarta.validation.constraints.NotNull;

public record UserActiveStatusUpdateDTO(
        @NotNull
        boolean isActive
) {}
