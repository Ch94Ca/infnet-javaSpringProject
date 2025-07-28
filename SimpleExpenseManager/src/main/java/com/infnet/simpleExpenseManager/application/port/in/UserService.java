package com.infnet.simpleExpenseManager.application.port.in;

import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserCreateDTO;
import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserDataUpdateDTO;
import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(UserCreateDTO userDTO);
    UserResponseDTO updateUserData(String email, UserDataUpdateDTO userDTO);
}