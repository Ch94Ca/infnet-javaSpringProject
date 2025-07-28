package com.infnet.simpleExpenseManager.adapters.in.web.dto;

import com.infnet.simpleExpenseManager.domain.enums.Roles;
import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateDTO(
        @NotNull
        Roles newRole
) {}
