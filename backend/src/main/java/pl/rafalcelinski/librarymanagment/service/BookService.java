package pl.rafalcelinski.librarymanagment.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.rafalcelinski.librarymanagment.dto.BookDTO;
import pl.rafalcelinski.librarymanagment.dto.ValidationErrorDTO;
import pl.rafalcelinski.librarymanagment.entity.Book;
import pl.rafalcelinski.librarymanagment.exception.EntityNotFoundException;
import pl.rafalcelinski.librarymanagment.exception.ValidationException;
import pl.rafalcelinski.librarymanagment.mapper.BookMapper;
import pl.rafalcelinski.librarymanagment.repository.BookRepository;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public Page<BookDTO> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDTO);
    }

    public BookDTO getBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
        return bookMapper.toDTO(book);
    }

    public BookDTO addBook(BookDTO bookDTO) {
        Book addedBook = bookRepository.save(bookMapper.toEntity(bookDTO));
        return bookMapper.toDTO(addedBook);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public BookDTO updateBook(Long bookId, BookDTO bookDTO) {
        if (!bookId.equals(bookDTO.id())) {
            List<ValidationErrorDTO> validationErrors = Collections.singletonList(
                    new ValidationErrorDTO("id", "ID in path and body must match")
            );
            throw new ValidationException(validationErrors);
        }

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));

        Book updatedBook = bookRepository.save(bookMapper.updateEntity(book, bookDTO));
        return bookMapper.toDTO(updatedBook);
    }
}
