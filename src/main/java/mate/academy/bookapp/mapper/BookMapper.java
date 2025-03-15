package mate.academy.bookapp.mapper;

import java.util.List;
import mate.academy.bookapp.config.MapperConfig;
import mate.academy.bookapp.dto.book.BookDto;
import mate.academy.bookapp.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookapp.dto.book.CreateBookRequestDto;
import mate.academy.bookapp.model.Book;
import mate.academy.bookapp.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper (config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto bookDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        List<Long> categoryIds = book.getCategories().stream()
                .map(Category::getId)
                .toList();
        bookDto.setCategoryIds(categoryIds);
    }

    void updateBook(CreateBookRequestDto requestDto, @MappingTarget Book book);
}
