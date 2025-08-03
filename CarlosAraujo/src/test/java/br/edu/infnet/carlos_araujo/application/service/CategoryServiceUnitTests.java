package br.edu.infnet.carlos_araujo.application.service;

import br.edu.infnet.carlos_araujo.application.exception.CategoryNotFoundException;
import br.edu.infnet.carlos_araujo.application.exception.DuplicateCategoryNameException;
import br.edu.infnet.carlos_araujo.application.port.out.CategoryRepositoryPort;
import br.edu.infnet.carlos_araujo.application.use_case.category.CategoryCreateCommand;
import br.edu.infnet.carlos_araujo.application.use_case.category.CategoryUpdateCommand;
import br.edu.infnet.carlos_araujo.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceUnitTests {

    @Mock
    private CategoryRepositoryPort categoryRepositoryPort;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category sampleCategory;
    private CategoryCreateCommand createCommand;
    private CategoryUpdateCommand updateCommand;

    @BeforeEach
    void setUp() {
        sampleCategory = new Category(
                1L,
                "Work",
                "Work related tasks",
                "#FF5733",
                1L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        createCommand = new CategoryCreateCommand(
                "Work",
                "Work related tasks",
                "#FF5733",
                1L
        );

        updateCommand = new CategoryUpdateCommand(
                1L,
                "Work Updated",
                "Updated work tasks",
                "#00FF00",
                1L
        );
    }

    @Nested
    @DisplayName("createCategory Tests")
    class CreateCategoryTests {

        @Test
        @DisplayName("Should create category successfully when name is unique")
        void createCategory_ShouldSucceed_WhenNameIsUnique() {
            // Arrange
            when(categoryRepositoryPort.existsByNameAndUserId("Work", 1L)).thenReturn(false);
            when(categoryRepositoryPort.save(any(Category.class))).thenReturn(sampleCategory);
            ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);

            // Act
            Category result = categoryService.createCategory(createCommand);

            // Assert
            assertNotNull(result);
            assertEquals(sampleCategory.getId(), result.getId());
            assertEquals(sampleCategory.getName(), result.getName());
            assertEquals(sampleCategory.getDescription(), result.getDescription());
            assertEquals(sampleCategory.getColor(), result.getColor());
            assertEquals(sampleCategory.getUserId(), result.getUserId());

            verify(categoryRepositoryPort, times(1)).existsByNameAndUserId("Work", 1L);
            verify(categoryRepositoryPort, times(1)).save(categoryCaptor.capture());

            Category savedCategory = categoryCaptor.getValue();
            assertEquals("Work", savedCategory.getName());
            assertEquals("Work related tasks", savedCategory.getDescription());
            assertEquals("#FF5733", savedCategory.getColor());
            assertEquals(1L, savedCategory.getUserId());
            assertNotNull(savedCategory.getCreatedAt());
            assertNotNull(savedCategory.getUpdatedAt());
        }

        @Test
        @DisplayName("Should throw DuplicateCategoryNameException when name already exists for user")
        void createCategory_ShouldThrowException_WhenNameAlreadyExists() {
            // Arrange
            when(categoryRepositoryPort.existsByNameAndUserId("Work", 1L)).thenReturn(true);

            // Act & Assert
            DuplicateCategoryNameException exception = assertThrows(
                    DuplicateCategoryNameException.class,
                    () -> categoryService.createCategory(createCommand)
            );

            assertEquals("Category name 'Work' already exists for this user", exception.getMessage());
            verify(categoryRepositoryPort, times(1)).existsByNameAndUserId("Work", 1L);
            verify(categoryRepositoryPort, never()).save(any(Category.class));
        }

        @Test
        @DisplayName("Should create category with null description and color")
        void createCategory_ShouldSucceed_WithNullDescriptionAndColor() {
            // Arrange
            CategoryCreateCommand commandWithNulls = new CategoryCreateCommand(
                    "Simple Category",
                    null,
                    null,
                    1L
            );

            Category expectedCategory = new Category(
                    2L,
                    "Simple Category",
                    null,
                    null,
                    1L,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            when(categoryRepositoryPort.existsByNameAndUserId("Simple Category", 1L)).thenReturn(false);
            when(categoryRepositoryPort.save(any(Category.class))).thenReturn(expectedCategory);

            // Act
            Category result = categoryService.createCategory(commandWithNulls);

            // Assert
            assertNotNull(result);
            assertEquals("Simple Category", result.getName());
            assertNull(result.getDescription());
            assertNull(result.getColor());
            assertEquals(1L, result.getUserId());

            verify(categoryRepositoryPort, times(1)).existsByNameAndUserId("Simple Category", 1L);
            verify(categoryRepositoryPort, times(1)).save(any(Category.class));
        }
    }

    @Nested
    @DisplayName("updateCategory Tests")
    class UpdateCategoryTests {

        @Test
        @DisplayName("Should update category successfully when all fields are provided")
        void updateCategory_ShouldSucceed_WhenAllFieldsProvided() {
            // Arrange
            Category updatedCategory = new Category(
                    1L,
                    "Work Updated",
                    "Updated work tasks",
                    "#00FF00",
                    1L,
                    sampleCategory.getCreatedAt(),
                    LocalDateTime.now()
            );

            when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.of(sampleCategory));
            when(categoryRepositoryPort.existsByNameAndUserId("Work Updated", 1L)).thenReturn(false);
            when(categoryRepositoryPort.save(any(Category.class))).thenReturn(updatedCategory);
            ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);

            // Act
            Category result = categoryService.updateCategory(updateCommand);

            // Assert
            assertNotNull(result);
            assertEquals("Work Updated", result.getName());
            assertEquals("Updated work tasks", result.getDescription());
            assertEquals("#00FF00", result.getColor());

            verify(categoryRepositoryPort, times(1)).findById(1L);
            verify(categoryRepositoryPort, times(1)).existsByNameAndUserId("Work Updated", 1L);
            verify(categoryRepositoryPort, times(1)).save(categoryCaptor.capture());

            Category savedCategory = categoryCaptor.getValue();
            assertNotNull(savedCategory.getUpdatedAt());
        }

        @Test
        @DisplayName("Should update category with partial data")
        void updateCategory_ShouldSucceed_WithPartialData() {
            // Arrange
            CategoryUpdateCommand partialCommand = new CategoryUpdateCommand(
                    1L,
                    null,
                    "New description only",
                    null,
                    1L
            );

            Category updatedCategory = new Category(
                    1L,
                    "Work",
                    "New description only",
                    "#FF5733",
                    1L,
                    sampleCategory.getCreatedAt(),
                    LocalDateTime.now()
            );

            when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.of(sampleCategory));
            when(categoryRepositoryPort.save(any(Category.class))).thenReturn(updatedCategory);

            // Act
            Category result = categoryService.updateCategory(partialCommand);

            // Assert
            assertNotNull(result);
            assertEquals("Work", result.getName());
            assertEquals("New description only", result.getDescription());
            assertEquals("#FF5733", result.getColor());

            verify(categoryRepositoryPort, times(1)).findById(1L);
            verify(categoryRepositoryPort, never()).existsByNameAndUserId(anyString(), anyLong());
            verify(categoryRepositoryPort, times(1)).save(any(Category.class));
        }

        @Test
        @DisplayName("Should throw CategoryNotFoundException when category not found")
        void updateCategory_ShouldThrowException_WhenCategoryNotFound() {
            // Arrange
            when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            CategoryNotFoundException exception = assertThrows(
                    CategoryNotFoundException.class,
                    () -> categoryService.updateCategory(updateCommand)
            );

            assertEquals("Category not found with id: 1", exception.getMessage());
            verify(categoryRepositoryPort, times(1)).findById(1L);
            verify(categoryRepositoryPort, never()).save(any(Category.class));
        }

        @Test
        @DisplayName("Should throw CategoryNotFoundException when category belongs to different user")
        void updateCategory_ShouldThrowException_WhenCategoryBelongsToDifferentUser() {
            // Arrange
            Category categoryFromDifferentUser = new Category(
                    1L, "Work", "Work tasks", "#FF5733", 2L,
                    LocalDateTime.now(), LocalDateTime.now()
            );
            when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.of(categoryFromDifferentUser));

            // Act & Assert
            CategoryNotFoundException exception = assertThrows(
                    CategoryNotFoundException.class,
                    () -> categoryService.updateCategory(updateCommand)
            );

            assertEquals("Category not found with id: 1 for user: 1", exception.getMessage());
            verify(categoryRepositoryPort, times(1)).findById(1L);
            verify(categoryRepositoryPort, never()).save(any(Category.class));
        }

        @Test
        @DisplayName("Should throw DuplicateCategoryNameException when new name already exists")
        void updateCategory_ShouldThrowException_WhenNewNameAlreadyExists() {
            // Arrange
            when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.of(sampleCategory));
            when(categoryRepositoryPort.existsByNameAndUserId("Work Updated", 1L)).thenReturn(true);

            // Act & Assert
            DuplicateCategoryNameException exception = assertThrows(
                    DuplicateCategoryNameException.class,
                    () -> categoryService.updateCategory(updateCommand)
            );

            assertEquals("Category name 'Work Updated' already exists for this user", exception.getMessage());
            verify(categoryRepositoryPort, times(1)).findById(1L);
            verify(categoryRepositoryPort, times(1)).existsByNameAndUserId("Work Updated", 1L);
            verify(categoryRepositoryPort, never()).save(any(Category.class));
        }

        @Test
        @DisplayName("Should not check name uniqueness when name is not being changed")
        void updateCategory_ShouldNotCheckUniqueness_WhenNameNotChanged() {
            // Arrange
            CategoryUpdateCommand sameNameCommand = new CategoryUpdateCommand(
                    1L,
                    "Work",
                    "Updated description",
                    "#00FF00",
                    1L
            );

            Category updatedCategory = new Category(
                    1L,
                    "Work",
                    "Updated description",
                    "#00FF00",
                    1L,
                    sampleCategory.getCreatedAt(),
                    LocalDateTime.now()
            );

            when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.of(sampleCategory));
            when(categoryRepositoryPort.save(any(Category.class))).thenReturn(updatedCategory);

            // Act
            Category result = categoryService.updateCategory(sameNameCommand);

            // Assert
            assertNotNull(result);
            assertEquals("Work", result.getName());
            assertEquals("Updated description", result.getDescription());

            verify(categoryRepositoryPort, times(1)).findById(1L);
            verify(categoryRepositoryPort, never()).existsByNameAndUserId(anyString(), anyLong());
            verify(categoryRepositoryPort, times(1)).save(any(Category.class));
        }
    }

    @Nested
    @DisplayName("deleteCategory Tests")
    class DeleteCategoryTests {

        @Test
        @DisplayName("Should delete category successfully when category exists")
        void deleteCategory_ShouldSucceed_WhenCategoryExists() {
            // Arrange
            when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.of(sampleCategory));
            doNothing().when(categoryRepositoryPort).deleteById(1L);

            // Act & Assert
            assertDoesNotThrow(() -> categoryService.deleteCategory(1L, 1L));

            verify(categoryRepositoryPort, times(1)).findById(1L);
            verify(categoryRepositoryPort, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw CategoryNotFoundException when category not found")
        void deleteCategory_ShouldThrowException_WhenCategoryNotFound() {
            // Arrange
            when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            CategoryNotFoundException exception = assertThrows(
                    CategoryNotFoundException.class,
                    () -> categoryService.deleteCategory(1L, 1L)
            );

            assertEquals("Category not found with id: 1", exception.getMessage());
            verify(categoryRepositoryPort, times(1)).findById(1L);
            verify(categoryRepositoryPort, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Should throw CategoryNotFoundException when category belongs to different user")
        void deleteCategory_ShouldThrowException_WhenCategoryBelongsToDifferentUser() {
            // Arrange
            Category categoryFromDifferentUser = new Category(
                    1L, "Work", "Work tasks", "#FF5733", 2L,
                    LocalDateTime.now(), LocalDateTime.now()
            );
            when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.of(categoryFromDifferentUser));

            // Act & Assert
            CategoryNotFoundException exception = assertThrows(
                    CategoryNotFoundException.class,
                    () -> categoryService.deleteCategory(1L, 1L)
            );

            assertEquals("Category not found with id: 1 for user: 1", exception.getMessage());
            verify(categoryRepositoryPort, times(1)).findById(1L);
            verify(categoryRepositoryPort, never()).deleteById(anyLong());
        }
    }

    @Nested
    @DisplayName("findCategoryById Tests")
    class FindCategoryByIdTests {

        @Test
        @DisplayName("Should return category when found")
        void findCategoryById_ShouldReturnCategory_WhenFound() {
            // Arrange
            when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.of(sampleCategory));

            // Act
            Category result = categoryService.findCategoryById(1L, 1L);

            // Assert
            assertNotNull(result);
            assertEquals(sampleCategory.getId(), result.getId());
            assertEquals(sampleCategory.getName(), result.getName());
            assertEquals(sampleCategory.getDescription(), result.getDescription());
            assertEquals(sampleCategory.getColor(), result.getColor());
            assertEquals(sampleCategory.getUserId(), result.getUserId());

            verify(categoryRepositoryPort, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should throw CategoryNotFoundException when category not found")
        void findCategoryById_ShouldThrowException_WhenNotFound() {
            // Arrange
            when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            CategoryNotFoundException exception = assertThrows(
                    CategoryNotFoundException.class,
                    () -> categoryService.findCategoryById(1L, 1L)
            );

            assertEquals("Category not found with id: 1", exception.getMessage());
            verify(categoryRepositoryPort, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should throw CategoryNotFoundException when category belongs to different user")
        void findCategoryById_ShouldThrowException_WhenCategoryBelongsToDifferentUser() {
            // Arrange
            Category categoryFromDifferentUser = new Category(
                    1L, "Work", "Work tasks", "#FF5733", 2L,
                    LocalDateTime.now(), LocalDateTime.now()
            );
            when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.of(categoryFromDifferentUser));

            // Act & Assert
            CategoryNotFoundException exception = assertThrows(
                    CategoryNotFoundException.class,
                    () -> categoryService.findCategoryById(1L, 1L)
            );

            assertEquals("Category not found with id: 1 for user: 1", exception.getMessage());
            verify(categoryRepositoryPort, times(1)).findById(1L);
        }
    }

    @Nested
    @DisplayName("findCategoriesByUserId Tests")
    class FindCategoriesByUserIdTests {

        @Test
        @DisplayName("Should return list of categories for user")
        void findCategoriesByUserId_ShouldReturnCategories() {
            // Arrange
            List<Category> categoryList = List.of(
                    sampleCategory,
                    new Category(2L, "Personal", "Personal tasks", "#00FF00", 1L, LocalDateTime.now(), LocalDateTime.now()),
                    new Category(3L, "Home", "Home tasks", "#0000FF", 1L, LocalDateTime.now(), LocalDateTime.now())
            );

            when(categoryRepositoryPort.findByUserId(1L)).thenReturn(categoryList);

            // Act
            List<Category> result = categoryService.findCategoriesByUserId(1L);

            // Assert
            assertNotNull(result);
            assertEquals(3, result.size());
            assertEquals(categoryList, result);

            verify(categoryRepositoryPort, times(1)).findByUserId(1L);
        }

        @Test
        @DisplayName("Should return empty list when user has no categories")
        void findCategoriesByUserId_ShouldReturnEmptyList_WhenNoCategories() {
            // Arrange
            when(categoryRepositoryPort.findByUserId(1L)).thenReturn(List.of());

            // Act
            List<Category> result = categoryService.findCategoriesByUserId(1L);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(categoryRepositoryPort, times(1)).findByUserId(1L);
        }
    }
}
