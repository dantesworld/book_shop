package mate.academy.bookapp.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookapp.model.Book;
import mate.academy.bookapp.repository.SpecificationProvider;
import mate.academy.bookapp.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "No specification provider found for key: " + key));
    }
}
