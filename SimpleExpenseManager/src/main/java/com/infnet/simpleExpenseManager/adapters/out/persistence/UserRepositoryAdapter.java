package com.infnet.simpleExpenseManager.adapters.out.persistence;

import com.infnet.simpleExpenseManager.adapters.out.persistence.entity.UserEntity;
import com.infnet.simpleExpenseManager.adapters.out.persistence.mapper.UserMapper;
import com.infnet.simpleExpenseManager.adapters.out.persistence.repository.UserJpaRepository;
import com.infnet.simpleExpenseManager.application.port.out.UserRepositoryPort;
import com.infnet.simpleExpenseManager.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        UserEntity userEntity = userMapper.toEntity(user);
        UserEntity savedEntity = userJpaRepository.save(userEntity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public Long deleteByEmail(String email){
        return userJpaRepository.deleteByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }
}
