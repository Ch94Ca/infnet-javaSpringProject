package br.edu.infnet.carlos_araujo.adapters.out.persistence;

import br.edu.infnet.carlos_araujo.adapters.out.persistence.entity.UserEntity;
import br.edu.infnet.carlos_araujo.adapters.out.persistence.mapper.UserMapper;
import br.edu.infnet.carlos_araujo.adapters.out.persistence.repository.UserJpaRepository;
import br.edu.infnet.carlos_araujo.application.port.out.UserRepositoryPort;
import br.edu.infnet.carlos_araujo.domain.enums.Role;
import br.edu.infnet.carlos_araujo.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        if(user.getCreatedAt() == null){
            user.setCreatedAt(LocalDateTime.now());
        } else {
            user.setUpdatedAt(LocalDateTime.now());
        }
        UserEntity userEntity = userMapper.toEntity(user);
        UserEntity savedEntity = userJpaRepository.save(userEntity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByUserRole(Role role) {
        return userJpaRepository.existsByUserRole(role);
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

    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        return userJpaRepository.findAll(pageable).
                map(userMapper::toDomain);
    }
}
