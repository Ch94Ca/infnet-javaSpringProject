package com.infnet.simpleExpenseManager.adapters.out.persistence.repository;

import com.infnet.simpleExpenseManager.adapters.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
}

