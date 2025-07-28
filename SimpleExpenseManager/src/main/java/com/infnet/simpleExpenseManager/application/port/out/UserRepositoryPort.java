package com.infnet.simpleExpenseManager.application.port.out;

import com.infnet.simpleExpenseManager.domain.user.User;

public interface UserRepositoryPort {
    User save(User user);
    Boolean existsByEmail(String email);
    void deleteByEmail(String email);
    User findByEmail(String email);
}
