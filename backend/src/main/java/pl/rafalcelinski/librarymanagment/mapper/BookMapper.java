package pl.rafalcelinski.librarymanagment.mapper;

import org.springframework.stereotype.Component;
import pl.rafalcelinski.librarymanagment.dto.BookDTO;
import pl.rafalcelinski.librarymanagment.entity.Book;

import java.time.LocalDate;

@Component
public class BookMapper {

    public BookDTO toDTO(Book book) {
        if (book == null) {
            return null;
        }

        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getReleaseDate().toString()
        );
    }

    public Book toEntity(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        }

        return new Book(
                bookDTO.title(),
                bookDTO.author(),
                bookDTO.publisher(),
                LocalDate.parse(bookDTO.releaseDate())
        );
    }

    public Book updateEntity(Book book, BookDTO bookDTO) {
        if (bookDTO.title() != null) {
            book.setTitle(bookDTO.title());
        }

        if (bookDTO.author() != null) {
            book.setAuthor(bookDTO.author());
        }

        if (bookDTO.publisher() != null) {
            book.setPublisher(bookDTO.publisher());
        }

        if (bookDTO.releaseDate() != null) {
            book.setReleaseDate(LocalDate.parse(bookDTO.releaseDate()));
        }

        return book;
    }
}
