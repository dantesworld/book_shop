package mate.academy.bookapp.service.book;

import mate.academy.bookapp.dto.book.BookDto;
import mate.academy.bookapp.dto.book.BookDtoWithoutCategoryIds;
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

    Page<BookDto> search(BookSearchParametersDto params, Pageable pageable);

    Page<BookDtoWithoutCategoryIds> getAllByCategoryId(Long id, Pageable pageable);
}
