package mate.academy.bookapp.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookapp.exceptions.DataProcessingException;
import mate.academy.bookapp.model.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save book", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> getAllBooksQuery = session.createQuery(
                    "from Book",Book.class
            );
            return getAllBooksQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("An unexpected error occurred "
                    + "while retrieving books: " + e.getMessage());
        }
    }
}
