package mate.academy.bookapp.service.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import mate.academy.bookapp.dto.category.CategoryDto;
import mate.academy.bookapp.dto.category.CategoryRequestDto;
import mate.academy.bookapp.exceptions.EntityNotFoundException;
import mate.academy.bookapp.mapper.CategoryMapper;
import mate.academy.bookapp.model.Category;
import mate.academy.bookapp.repository.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;
    private CategoryRequestDto requestDto;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Fiction");
        category.setDescription("Fictional books");

        categoryDto = new CategoryDto()
                .setId(category.getId())
                .setName(category.getName())
                .setDescription(category.getDescription());

        requestDto = new CategoryRequestDto()
                .setName(category.getName())
                .setDescription(category.getDescription());
    }

    @Test
    @DisplayName("Find all categories returns paginated results")
    void findAll_ValidPageable_ReturnsPageOfCategoryDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(List.of(category), pageable, 1);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        Page<CategoryDto> result = categoryService.findAll(pageable);

        assertThat(result.getContent())
                .hasSize(1)
                .containsExactly(categoryDto);
        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Get existing category by ID returns category DTO")
    void getById_ExistingId_ReturnsCategoryDto() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(1L);

        assertThat(result).isEqualTo(categoryDto);
        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Get non-existent category by ID throws exception")
    void getById_NonExistentId_ThrowsException() {
        Long invalidId = 999L;
        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getById(invalidId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Category by id " + invalidId + " not found");
        verify(categoryRepository).findById(invalidId);
    }

    @Test
    @DisplayName("Save category with valid request returns created category")
    void save_ValidRequest_ReturnsCreatedCategory() {
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.save(requestDto);

        assertThat(result).isEqualTo(categoryDto);
        verify(categoryMapper).toModel(requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Update existing category returns updated category")
    void update_ExistingCategory_ReturnsUpdatedCategory() {
        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("Updated Name");
        updatedCategory.setDescription("Updated Description");

        CategoryDto updatedDto = new CategoryDto()
                .setId(1L)
                .setName("Updated Name")
                .setDescription("Updated Description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(updatedDto);

        CategoryDto result = categoryService.updateById(1L, requestDto);

        assertThat(result).isEqualTo(updatedDto);
        verify(categoryRepository).findById(1L);
        verify(categoryMapper).updateCategoryFromDto(requestDto, category);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(updatedCategory);
    }

    @Test
    @DisplayName("Update non-existent category throws exception")
    void update_NonExistentCategory_ThrowsException() {
        Long invalidId = 999L;
        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.updateById(invalidId, requestDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can't find category by id: " + invalidId);
        verify(categoryRepository).findById(invalidId);
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    @DisplayName("Delete category executes repository delete")
    void delete_ValidId_ExecutesDelete() {
        Long validId = 1L;
        doNothing().when(categoryRepository).deleteById(validId);

        categoryService.deleteById(validId);

        verify(categoryRepository).deleteById(validId);
        verifyNoMoreInteractions(categoryMapper);
    }
}
