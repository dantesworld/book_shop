package mate.academy.bookapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookapp.dto.book.BookDto;
import mate.academy.bookapp.dto.book.BookSearchParametersDto;
import mate.academy.bookapp.dto.book.CreateBookRequestDto;
import mate.academy.bookapp.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoint for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description = "Get a list of all books")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Page<BookDto> getAllBooks(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Get book from DB by ID")
    @PreAuthorize("hasRole('ROLE_USER')")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Create a book", description = "Create a new book")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by ID", description = "Delete book by ID")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Update book by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BookDto updateBook(@PathVariable Long id,
                              @RequestBody @Valid CreateBookRequestDto bookRequestDto) {
        return bookService.updateBook(id, bookRequestDto);
    }

    @GetMapping("/search")
    @Operation(summary = "Search a book",
            description = "Search book by parameters: title, author, isbn")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Page<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable) {
        return bookService.search(searchParameters, pageable);
    }
}
