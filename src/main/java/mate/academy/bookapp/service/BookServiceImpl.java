package mate.academy.bookapp.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookapp.dto.BookDto;
import mate.academy.bookapp.dto.BookSearchParametersDto;
import mate.academy.bookapp.dto.CreateBookRequestDto;
import mate.academy.bookapp.exceptions.EntityNotFoundException;
import mate.academy.bookapp.mapper.BookMapper;
import mate.academy.bookapp.model.Book;
import mate.academy.bookapp.repository.book.BookRepository;
import mate.academy.bookapp.repository.book.BookSpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto book) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toModel(book)));
    }

    @Override
    public BookDto getById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book with id " + id + " not found")
        );
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book with id " + id + " not found")
        );
        bookMapper.updateBook(requestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto params) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
