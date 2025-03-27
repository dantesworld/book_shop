package mate.academy.bookapp.service.category;

import mate.academy.bookapp.dto.category.CategoryDto;
import mate.academy.bookapp.dto.category.CategoryRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryRequestDto requestDto);

    CategoryDto updateById(Long id, CategoryRequestDto requestDto);

    void deleteById(Long id);
}
