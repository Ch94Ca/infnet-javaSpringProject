package com.infnet.simpleExpenseManager.application.port.in;


import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserCreateDTO;
import com.infnet.simpleExpenseManager.domain.user.User;

import java.util.List;

public interface UserService {
    User createUser(UserCreateDTO userDTO);
    User findUserById(Long id);
    List<User> findAllUsers();
    User updateUser(Long id, User userDetails);
    void deleteUserById(Long id);
}