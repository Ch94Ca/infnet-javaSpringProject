package br.edu.infnet.carlos_araujo.application.port.in;

import br.edu.infnet.carlos_araujo.application.use_case.category.CategoryCreateCommand;
import br.edu.infnet.carlos_araujo.application.use_case.category.CategoryUpdateCommand;
import br.edu.infnet.carlos_araujo.domain.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryCreateCommand command);
    List<Category> findCategoriesByUserId(Long userId);
    Category findCategoryById(Long userId, Long categoryId);
    Category updateCategory(CategoryUpdateCommand command);
    void deleteCategory(Long userId, Long categoryId);
}
