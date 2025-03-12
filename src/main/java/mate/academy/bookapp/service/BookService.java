package mate.academy.bookapp.service;

import mate.academy.bookapp.dto.book.BookDto;
import mate.academy.bookapp.dto.book.BookSearchParametersDto;
import mate.academy.bookapp.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    BookDto getById(Long id);

    Page<BookDto> findAll(Pageable pageable);

    void deleteById(Long id);

    BookDto updateBook(Long id, CreateBookRequestDto requestDto);

    public Page<BookDto> search(BookSearchParametersDto params, Pageable pageable);
}
