package br.edu.infnet.carlos_araujo.adapters.in.web.controller;

import br.edu.infnet.carlos_araujo.adapters.in.web.dto.category.CategoryCreateDTO;
import br.edu.infnet.carlos_araujo.adapters.in.web.dto.category.CategoryResponseDTO;
import br.edu.infnet.carlos_araujo.adapters.in.web.dto.category.CategoryUpdateDTO;
import br.edu.infnet.carlos_araujo.adapters.in.web.mapper.CategoryDtoMapper;
import br.edu.infnet.carlos_araujo.application.port.in.CategoryService;
import br.edu.infnet.carlos_araujo.application.use_case.category.CategoryCreateCommand;
import br.edu.infnet.carlos_araujo.application.use_case.category.CategoryUpdateCommand;
import br.edu.infnet.carlos_araujo.domain.Category;
import br.edu.infnet.carlos_araujo.domain.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Category", description = "Categories endpoints")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryDtoMapper categoryDtoMapper;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CategoryCreateDTO categoryCreateDTO) {

        CategoryCreateCommand command = categoryDtoMapper.toCategoryCreateCommand(categoryCreateDTO);
        command.setUserId(user.getUserId());

        Category category = categoryService.createCategory(command);
        CategoryResponseDTO result = categoryDtoMapper.toResponseDto(category);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponseDTO>> getMyCategories(
            @AuthenticationPrincipal User user,
            @PageableDefault(sort = "name") Pageable pageable) {

        Long userId = user.getUserId();
        List<Category> allCategories = categoryService.findCategoriesByUserId(userId);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allCategories.size());
        List<Category> pageContent = start >= allCategories.size() ?
                List.of() : allCategories.subList(start, end);

        Page<Category> categoriesPage = new PageImpl<>(pageContent, pageable, allCategories.size());
        Page<CategoryResponseDTO> result = categoriesPage.map(categoryDtoMapper::toResponseDto);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @AuthenticationPrincipal User user,
            @PathVariable Long categoryId) {

        Long userId = user.getUserId();
        Category category = categoryService.findCategoryById(userId, categoryId);
        CategoryResponseDTO result = categoryDtoMapper.toResponseDto(category);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @AuthenticationPrincipal User user,
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryUpdateDTO categoryUpdateDTO) {

        CategoryUpdateCommand command = categoryDtoMapper.toCategoryUpdateCommand(categoryUpdateDTO);
        command.setUserId(user.getUserId());
        command.setId(categoryId);

        Category category = categoryService.updateCategory(command);
        CategoryResponseDTO result = categoryDtoMapper.toResponseDto(category);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @AuthenticationPrincipal User user,
            @PathVariable Long categoryId) {

        Long userId = user.getUserId();
        categoryService.deleteCategory(userId, categoryId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
