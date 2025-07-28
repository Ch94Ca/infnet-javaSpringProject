package com.infnet.simpleExpenseManager.adapters.in.web.dto;

import com.infnet.simpleExpenseManager.domain.enums.Roles;
import jakarta.validation.constraints.Email;

public record UserDataUpdateDTO(
        String name,

        @Email(message = "Invalid email format")
        String email,

        Roles role
) {}

