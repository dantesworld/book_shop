package mate.academy.bookapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookapp.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookapp.dto.category.CategoryDto;
import mate.academy.bookapp.dto.category.CategoryRequestDto;
import mate.academy.bookapp.service.BookService;
import mate.academy.bookapp.service.CategoryService;
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

@Tag(name = "Category management",
        description = "Endpoint for managing book categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @Operation(summary = "Get all categories", description = "Get a list of all categories")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public Page<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @Operation(summary = "Get category by id", description = "Get catehory by id")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(summary = "Create new category",
            description = "Create new category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public CategoryDto createCategory(@RequestBody
                                   @Valid CategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @Operation(summary = "Update category by id",
            description = "Update category by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(@RequestBody @Valid CategoryRequestDto requestDto,
                                      @PathVariable Long id) {
        return categoryService.updateById(id, requestDto);
    }

    @Operation(summary = "Delete the category with ID",
            description = "Delete the category with ID")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @Operation(summary = "Get all books by the category ID",
            description = "Get a list of books with the category ID")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}/books")
        public Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @PathVariable("id") Long categoryId,
            Pageable pageable) {
        return bookService.getAllByCategoryId(categoryId, pageable);
    }
}
