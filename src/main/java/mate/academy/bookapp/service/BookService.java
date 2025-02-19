package mate.academy.bookapp.service;

import java.util.List;
import mate.academy.bookapp.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
