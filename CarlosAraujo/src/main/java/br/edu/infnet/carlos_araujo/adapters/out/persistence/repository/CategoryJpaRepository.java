package br.edu.infnet.carlos_araujo.adapters.out.persistence.repository;

import br.edu.infnet.carlos_araujo.adapters.out.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findByUserId(Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
    boolean existsByNameAndUserId(String name, Long userId);
}
