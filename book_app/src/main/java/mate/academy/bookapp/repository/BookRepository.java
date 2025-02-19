package mate.academy.bookapp.repository;

import java.util.List;
import mate.academy.bookapp.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
