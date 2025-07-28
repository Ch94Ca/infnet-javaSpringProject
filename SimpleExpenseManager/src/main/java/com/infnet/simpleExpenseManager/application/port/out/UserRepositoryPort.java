package com.infnet.simpleExpenseManager.application.port.out;

import com.infnet.simpleExpenseManager.domain.user.User;

import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Boolean existsByEmail(String email);
    Long deleteByEmail(String email);
    Optional<User> findByEmail(String email);
}
