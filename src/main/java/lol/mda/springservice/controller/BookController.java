package lol.mda.springservice.controller;

import lol.mda.springservice.model.Book;
import lol.mda.springservice.model.BookRequest;
import lol.mda.springservice.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> listBooks() {
        return bookService.findAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable long id) {
        return bookService.findBookById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody BookRequest request) {
        validateRequiredFields(request);
        Book book = bookService.addBook(request.title(), request.author());
        return ResponseEntity.created(URI.create("/api/books/" + book.id())).body(book);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable long id, @RequestBody BookRequest request) {
        validatePatchFields(request);
        return bookService.updateBook(id, request.title(), request.author())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id) {
        return bookService.deleteBook(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private void validateRequiredFields(BookRequest request) {
        if (request == null || isBlank(request.title()) || isBlank(request.author())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title and author are required.");
        }
    }

    private void validatePatchFields(BookRequest request) {
        if (request == null || (request.title() == null && request.author() == null)
                || (request.title() != null && request.title().isBlank())
                || (request.author() != null && request.author().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Provide a non-blank title, author, or both.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
