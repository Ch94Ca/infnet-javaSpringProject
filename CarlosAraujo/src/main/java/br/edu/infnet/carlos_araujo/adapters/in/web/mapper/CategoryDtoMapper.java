package br.edu.infnet.carlos_araujo.adapters.in.web.mapper;

import br.edu.infnet.carlos_araujo.adapters.in.web.dto.category.CategoryCreateDTO;
import br.edu.infnet.carlos_araujo.adapters.in.web.dto.category.CategoryResponseDTO;
import br.edu.infnet.carlos_araujo.adapters.in.web.dto.category.CategoryUpdateDTO;
import br.edu.infnet.carlos_araujo.application.use_case.category.CategoryCreateCommand;
import br.edu.infnet.carlos_araujo.application.use_case.category.CategoryUpdateCommand;
import br.edu.infnet.carlos_araujo.domain.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoMapper {

    public CategoryCreateCommand toCategoryCreateCommand(CategoryCreateDTO dto) {
        CategoryCreateCommand command = new CategoryCreateCommand();
        command.setName(dto.getName());
        command.setDescription(dto.getDescription());
        command.setColor(dto.getColor());
        return command;
    }

    public CategoryUpdateCommand toCategoryUpdateCommand(CategoryUpdateDTO dto) {
        CategoryUpdateCommand command = new CategoryUpdateCommand();
        command.setName(dto.getName());
        command.setDescription(dto.getDescription());
        command.setColor(dto.getColor());
        return command;
    }

    public CategoryResponseDTO toResponseDto(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setColor(category.getColor());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }
}
