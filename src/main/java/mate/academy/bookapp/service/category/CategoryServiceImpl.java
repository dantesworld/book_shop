package mate.academy.bookapp.service.category;

import lombok.RequiredArgsConstructor;
import mate.academy.bookapp.dto.category.CategoryDto;
import mate.academy.bookapp.dto.category.CategoryRequestDto;
import mate.academy.bookapp.exceptions.EntityNotFoundException;
import mate.academy.bookapp.mapper.CategoryMapper;
import mate.academy.bookapp.model.Category;
import mate.academy.bookapp.repository.category.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Category by id " + id + " not found"))
        );
    }

    @Override
    public CategoryDto save(CategoryRequestDto requestDto) {
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toModel(requestDto)));
    }

    @Override
    public CategoryDto updateById(Long id, CategoryRequestDto requestDto) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Can't find category by id: %s", id))
        );
        categoryMapper.updateCategoryFromDto(requestDto, existingCategory);
        return categoryMapper.toDto(categoryRepository.save(existingCategory));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
