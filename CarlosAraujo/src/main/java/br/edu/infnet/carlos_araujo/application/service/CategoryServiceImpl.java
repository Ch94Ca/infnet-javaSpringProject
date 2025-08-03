package br.edu.infnet.carlos_araujo.application.service;

import br.edu.infnet.carlos_araujo.application.exception.CategoryNotFoundException;
import br.edu.infnet.carlos_araujo.application.exception.DuplicateCategoryNameException;
import br.edu.infnet.carlos_araujo.application.port.in.CategoryService;
import br.edu.infnet.carlos_araujo.application.port.out.CategoryRepositoryPort;
import br.edu.infnet.carlos_araujo.application.use_case.category.CategoryCreateCommand;
import br.edu.infnet.carlos_araujo.application.use_case.category.CategoryUpdateCommand;
import br.edu.infnet.carlos_araujo.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepositoryPort categoryRepositoryPort;

    @Override
    public Category createCategory(CategoryCreateCommand command) {
        validateCategoryNameUniqueness(command.getName(), command.getUserId());

        Category newCategory = new Category();
        newCategory.setName(command.getName());
        newCategory.setDescription(command.getDescription());
        newCategory.setUserId(command.getUserId());
        newCategory.setColor(command.getColor());
        newCategory.setCreatedAt(LocalDateTime.now());
        newCategory.setUpdatedAt(LocalDateTime.now());

        return categoryRepositoryPort.save(newCategory);
    }

    @Override
    public Category updateCategory(CategoryUpdateCommand command) {
        Category category = findCategoryByIdAndUserId(command.getId(), command.getUserId());

        if (command.getName() != null && !command.getName().equals(category.getName())) {
            validateCategoryNameUniqueness(command.getName(), command.getUserId());
            category.setName(command.getName());
        }

        if (command.getDescription() != null) {
            category.setDescription(command.getDescription());
        }

        if (command.getColor() != null) {
            category.setColor(command.getColor());
        }

        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepositoryPort.save(category);
    }

    @Override
    public Category findCategoryById(Long userId, Long categoryId) {
        return findCategoryByIdAndUserId(categoryId, userId);
    }

    @Override
    public List<Category> findCategoriesByUserId(Long userId) {
        return categoryRepositoryPort.findByUserId(userId);
    }

    @Override
    public void deleteCategory(Long userId, Long categoryId) {
        findCategoryByIdAndUserId(categoryId, userId);
        categoryRepositoryPort.deleteById(categoryId);
    }

    private Category findCategoryByIdAndUserId(Long categoryId, Long userId) {
        Category category = categoryRepositoryPort.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));

        if (!category.getUserId().equals(userId)) {
            throw new CategoryNotFoundException("Category not found with id: " + categoryId + " for user: " + userId);
        }

        return category;
    }

    private void validateCategoryNameUniqueness(String name, Long userId) {
        if (categoryRepositoryPort.existsByNameAndUserId(name, userId)) {
            throw new DuplicateCategoryNameException("Category name '" + name + "' already exists for this user");
        }
    }
}
