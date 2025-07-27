package com.infnet.simpleExpenseManager.domain.user;

import java.time.LocalDateTime;
import java.util.Set;

import com.infnet.simpleExpenseManager.domain.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Roles userRole;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
