package com.infnet.simpleExpenseManager.adapters.out.persistence;

import com.infnet.simpleExpenseManager.adapters.out.persistence.entity.UserEntity;
import com.infnet.simpleExpenseManager.adapters.out.persistence.mapper.UserMapper;
import com.infnet.simpleExpenseManager.adapters.out.persistence.repository.UserJpaRepository;
import com.infnet.simpleExpenseManager.application.port.out.UserRepositoryPort;
import com.infnet.simpleExpenseManager.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        UserEntity savedEntity = userJpaRepository.save(userEntity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
