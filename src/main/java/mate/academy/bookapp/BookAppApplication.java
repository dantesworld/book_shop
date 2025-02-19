package mate.academy.bookapp;

import java.math.BigDecimal;
import mate.academy.bookapp.model.Book;
import mate.academy.bookapp.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(BookService bookService) {
        return args -> {
            Book book = new Book();
            book.setTitle("Book Title");
            book.setAuthor("Book Author");
            book.setIsbn("Book Isbn");
            book.setDescription("Book Description");
            book.setPrice(BigDecimal.valueOf(13));

            bookService.save(book);
        };
    }

}
