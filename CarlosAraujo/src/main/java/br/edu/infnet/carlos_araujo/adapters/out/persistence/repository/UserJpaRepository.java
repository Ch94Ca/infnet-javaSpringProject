package br.edu.infnet.carlos_araujo.adapters.out.persistence.repository;

import br.edu.infnet.carlos_araujo.adapters.out.persistence.entity.UserEntity;
import br.edu.infnet.carlos_araujo.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
    boolean existsByUserRole(Role role);
    Long deleteByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
}

