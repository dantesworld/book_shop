package mate.academy.bookapp.service.book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import mate.academy.bookapp.dto.book.BookDto;
import mate.academy.bookapp.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookapp.dto.book.BookSearchParametersDto;
import mate.academy.bookapp.dto.book.CreateBookRequestDto;
import mate.academy.bookapp.exceptions.EntityNotFoundException;
import mate.academy.bookapp.mapper.BookMapper;
import mate.academy.bookapp.model.Book;
import mate.academy.bookapp.model.Category;
import mate.academy.bookapp.repository.book.BookRepository;
import mate.academy.bookapp.repository.book.BookSpecificationBuilder;
import mate.academy.bookapp.repository.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @Mock
    private CategoryRepository categoryRepository;

    private Book book;
    private BookDto bookDto;
    private CreateBookRequestDto createBookRequestDto;
    private BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds;
    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        category1 = new Category();
        category1.setId(1L);
        category1.setName("Fiction");

        category2 = new Category();
        category2.setId(2L);
        category2.setName("Science");

        book = new Book();
        book.setId(1L);
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setIsbn("9789669782441");
        book.setPrice(new BigDecimal("40.00"));
        book.setDescription("Some interesting book");
        book.setCoverImage("https://example.com/cover.jpg");
        book.setCategories(new HashSet<>(Set.of(category1, category2)));

        bookDto = new BookDto()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage())
                .setCategoryIds(List.of(1L, 2L));

        createBookRequestDto = new CreateBookRequestDto()
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage())
                .setCategoryIds(List.of(1L, 2L));

        bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage());
    }

    @Test
    @DisplayName("Save book with valid request DTO returns book DTO")
    void save_WithValidRequestDto_ReturnBookDto() {
        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category2));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.save(createBookRequestDto);

        assertThat(result).isEqualTo(bookDto);
        verify(bookMapper).toModel(createBookRequestDto);
        verify(categoryRepository, times(2)).findById(anyLong());
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Save book with invalid category ID throws EntityNotFoundException")
    void save_WithInvalidCategoryId_ThrowsException() {
        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.save(createBookRequestDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Category not found by id: 1");

        verify(categoryRepository).findById(1L);
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update book with valid ID and request DTO returns updated book DTO")
    void updateById_ValidIdAndRequestDto_ReturnsUpdatedBookDto() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category2));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.updateBook(bookId, createBookRequestDto);

        assertThat(result).isEqualTo(bookDto);
        verify(bookRepository).findById(bookId);
        verify(bookMapper).updateBook(createBookRequestDto, book);
        verify(categoryRepository, times(2)).findById(anyLong());
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Find all books with pagination returns page of book DTOs")
    void findAll_WithPageable_ReturnsPageOfBookDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.findAll(pageable);

        assertThat(result.getContent()).hasSize(1).contains(bookDto);
        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Get books by category ID returns page of DTOs without category IDs")
    void getAllByCategoryId_ValidCategoryId_ReturnsPageOfDtosWithoutCategories() {
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);
        when(bookRepository.findAllByCategoryId(categoryId, pageable)).thenReturn(bookPage);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategoryIds);

        Page<BookDtoWithoutCategoryIds> result =
                bookService.getAllByCategoryId(categoryId, pageable);

        assertThat(result.getContent()).hasSize(1).contains(bookDtoWithoutCategoryIds);
        verify(bookRepository).findAllByCategoryId(categoryId, pageable);
        verify(bookMapper).toDtoWithoutCategories(book);
    }

    @Test
    @DisplayName("Search books with multiple parameters returns correctly filtered books")
    void search_WithMultipleParameters_ReturnsFilteredBooks() {
        BookSearchParametersDto params = new BookSearchParametersDto(
                new String[]{"Title"},
                new String[]{"Author"},
                new String[]{"978*"}
        );
        Pageable pageable = PageRequest.of(0, 10);

        Specification<Book> mockSpec = mock(Specification.class);
        when(bookSpecificationBuilder.build(params)).thenReturn(mockSpec);

        Page<Book> filteredPage = new PageImpl<>(List.of(book), pageable, 1);
        when(bookRepository.findAll(mockSpec, pageable)).thenReturn(filteredPage);

        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.search(params, pageable);

        assertThat(result.getContent())
                .hasSize(1)
                .containsExactly(bookDto);

        verify(bookSpecificationBuilder).build(params);
        verify(bookRepository).findAll(mockSpec, pageable);
        verify(bookMapper).toDto(book);
    }
}
