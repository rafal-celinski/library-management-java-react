package pl.rafalcelinski.librarymanagment.controller;

import jakarta.validation.groups.Default;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import pl.rafalcelinski.librarymanagment.dto.BookDTO;
import pl.rafalcelinski.librarymanagment.dto.validation.groups.OnCreate;
import pl.rafalcelinski.librarymanagment.dto.validation.groups.OnUpdate;
import pl.rafalcelinski.librarymanagment.service.BookService;


@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<?> getBooks(Pageable pageable) {
        Page<BookDTO> books = bookService.getBooks(pageable);

        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }

    @PostMapping()
    public ResponseEntity<?> addBook(@Validated({OnCreate.class, Default.class}) @RequestBody BookDTO bookDTO) {
        BookDTO newBookDTO = bookService.addBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBookDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Validated({OnUpdate.class, Default.class}) @RequestBody BookDTO bookDTO) {
        BookDTO updatedBookDTO = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(updatedBookDTO);

    }

}
