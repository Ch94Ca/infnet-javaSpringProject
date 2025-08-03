package br.edu.infnet.carlos_araujo.adapters.out.persistence.mapper;

import br.edu.infnet.carlos_araujo.adapters.out.persistence.entity.CategoryEntity;
import br.edu.infnet.carlos_araujo.adapters.out.persistence.entity.UserEntity;
import br.edu.infnet.carlos_araujo.domain.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toDomain(CategoryEntity entity) {
        if (entity == null) return null;

        Category category = new Category();
        category.setId(entity.getId());
        category.setName(entity.getName());
        category.setDescription(entity.getDescription());
        category.setColor(entity.getColor());
        category.setUserId(entity.getUser().getId());
        category.setCreatedAt(entity.getCreatedAt());
        category.setUpdatedAt(entity.getUpdatedAt());

        return category;
    }

    public CategoryEntity toEntity(Category category, UserEntity userEntity) {
        if (category == null) return null;

        CategoryEntity entity = new CategoryEntity();
        entity.setId(category.getId());
        entity.setName(category.getName());
        entity.setDescription(category.getDescription());
        entity.setColor(category.getColor());
        entity.setUser(userEntity);
        entity.setCreatedAt(category.getCreatedAt());
        entity.setUpdatedAt(category.getUpdatedAt());

        return entity;
    }
}
