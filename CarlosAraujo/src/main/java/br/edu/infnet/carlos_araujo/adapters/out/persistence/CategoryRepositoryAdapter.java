package br.edu.infnet.carlos_araujo.adapters.out.persistence;

import br.edu.infnet.carlos_araujo.adapters.out.persistence.entity.UserEntity;
import br.edu.infnet.carlos_araujo.adapters.out.persistence.mapper.CategoryMapper;
import br.edu.infnet.carlos_araujo.adapters.out.persistence.repository.CategoryJpaRepository;
import br.edu.infnet.carlos_araujo.adapters.out.persistence.repository.UserJpaRepository;
import br.edu.infnet.carlos_araujo.application.port.out.CategoryRepositoryPort;
import br.edu.infnet.carlos_araujo.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final CategoryJpaRepository categoryJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category save(Category category) {
        UserEntity userEntity = userJpaRepository.findById(category.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var entity = categoryMapper.toEntity(category, userEntity);
        var savedEntity = categoryJpaRepository.save(entity);
        return categoryMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryJpaRepository.findById(id)
                .map(categoryMapper::toDomain);
    }

    @Override
    public List<Category> findByUserId(Long userId) {
        return categoryJpaRepository.findByUserId(userId)
                .stream()
                .map(categoryMapper::toDomain)
                .toList();
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll()
                .stream()
                .map(categoryMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        categoryJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByIdAndUserId(Long id, Long userId) {
        return categoryJpaRepository.existsByIdAndUserId(id, userId);
    }

    @Override
    public boolean existsByNameAndUserId(String name, Long userId) {
        return categoryJpaRepository.existsByNameAndUserId(name, userId);
    }
}
