package mate.academy.bookapp.mapper;

import mate.academy.bookapp.config.MapperConfig;
import mate.academy.bookapp.dto.category.CategoryDto;
import mate.academy.bookapp.dto.category.CategoryRequestDto;
import mate.academy.bookapp.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper (config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CategoryRequestDto requestDto);

    void updateCategoryFromDto(CategoryRequestDto requestDto,
                               @MappingTarget Category existingCategory);
}
