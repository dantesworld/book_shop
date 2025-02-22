package mate.academy.bookapp.service;

import java.util.List;
import mate.academy.bookapp.dto.BookDto;
import mate.academy.bookapp.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    BookDto getById(Long id);

    List<BookDto> findAll();
}
