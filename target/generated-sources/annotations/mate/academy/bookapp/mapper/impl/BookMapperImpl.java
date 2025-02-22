package mate.academy.bookapp.mapper.impl;

import javax.annotation.processing.Generated;
import mate.academy.bookapp.dto.BookDto;
import mate.academy.bookapp.dto.CreateBookRequestDto;
import mate.academy.bookapp.mapper.BookMapper;
import mate.academy.bookapp.model.Book;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-22T11:28:46+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public BookDto toDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDto bookDto = new BookDto();

        if ( book.getId() != null ) {
            bookDto.setId( book.getId() );
        }
        if ( book.getTitle() != null ) {
            bookDto.setTitle( book.getTitle() );
        }
        if ( book.getAuthor() != null ) {
            bookDto.setAuthor( book.getAuthor() );
        }
        if ( book.getIsbn() != null ) {
            bookDto.setIsbn( book.getIsbn() );
        }
        if ( book.getPrice() != null ) {
            bookDto.setPrice( book.getPrice() );
        }
        if ( book.getDescription() != null ) {
            bookDto.setDescription( book.getDescription() );
        }
        if ( book.getCoverImage() != null ) {
            bookDto.setCoverImage( book.getCoverImage() );
        }

        return bookDto;
    }

    @Override
    public Book toModel(CreateBookRequestDto bookDto) {
        if ( bookDto == null ) {
            return null;
        }

        Book book = new Book();

        if ( bookDto.getTitle() != null ) {
            book.setTitle( bookDto.getTitle() );
        }
        if ( bookDto.getAuthor() != null ) {
            book.setAuthor( bookDto.getAuthor() );
        }
        if ( bookDto.getIsbn() != null ) {
            book.setIsbn( bookDto.getIsbn() );
        }
        if ( bookDto.getPrice() != null ) {
            book.setPrice( bookDto.getPrice() );
        }
        if ( bookDto.getDescription() != null ) {
            book.setDescription( bookDto.getDescription() );
        }
        if ( bookDto.getCoverImage() != null ) {
            book.setCoverImage( bookDto.getCoverImage() );
        }

        return book;
    }
}
