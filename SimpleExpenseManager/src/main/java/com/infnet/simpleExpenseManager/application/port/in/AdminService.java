package com.infnet.simpleExpenseManager.application.port.in;

import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserDataUpdateDTO;
import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserResponseDTO;
import com.infnet.simpleExpenseManager.domain.enums.Roles;

public interface AdminService {
    UserResponseDTO updateUserData(String email, UserDataUpdateDTO userDTO);
    void deleteUserByEmail(String email);
    UserResponseDTO changeUserRole(String email, Roles newRole);
}