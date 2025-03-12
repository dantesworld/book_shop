package mate.academy.bookapp.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.bookapp.dto.book.BookSearchParametersDto;
import mate.academy.bookapp.model.Book;
import mate.academy.bookapp.repository.SpecificationBuilder;
import mate.academy.bookapp.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    public static final String ISBN_KEY = "isbn";
    public static final String TITLE_KEY = "title";
    public static final String AUTHOR_KEY = "author";

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.title() != null && searchParameters.title().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(TITLE_KEY)
                    .getSpecification(searchParameters.title()));
        }
        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(searchParameters.author()));
        }
        if (searchParameters.isbn() != null && searchParameters.isbn().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(ISBN_KEY)
                    .getSpecification(searchParameters.isbn()));
        }
        return spec;
    }
}
