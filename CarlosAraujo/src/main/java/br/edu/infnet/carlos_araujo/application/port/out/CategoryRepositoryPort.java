package br.edu.infnet.carlos_araujo.application.port.out;

import br.edu.infnet.carlos_araujo.domain.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryPort {
    Category save(Category category);
    Optional<Category> findById(Long id);
    List<Category> findByUserId(Long userId);
    List<Category> findAll();
    void deleteById(Long id);
    boolean existsByIdAndUserId(Long id, Long userId);
    boolean existsByNameAndUserId(String name, Long userId);
}
