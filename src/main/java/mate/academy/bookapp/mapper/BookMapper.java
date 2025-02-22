package mate.academy.bookapp.mapper;

import mate.academy.bookapp.config.MapperConfig;
import mate.academy.bookapp.dto.BookDto;
import mate.academy.bookapp.dto.CreateBookRequestDto;
import mate.academy.bookapp.model.Book;
import org.mapstruct.Mapper;

@Mapper (config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto bookDto);
}
