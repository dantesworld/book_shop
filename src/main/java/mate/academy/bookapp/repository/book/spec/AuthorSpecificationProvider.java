package mate.academy.bookapp.repository.book.spec;

import java.util.Arrays;
import mate.academy.bookapp.model.Book;
import mate.academy.bookapp.repository.SpecificationProvider;
import mate.academy.bookapp.repository.book.BookSpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public String getKey() {
        return BookSpecificationBuilder.AUTHOR_KEY;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder)
                -> root.get(BookSpecificationBuilder.AUTHOR_KEY)
                .in(Arrays.stream(params).toArray());
    }
}
