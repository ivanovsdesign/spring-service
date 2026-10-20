package lol.mda.springservice.service;

import lol.mda.springservice.model.Book;
import lol.mda.springservice.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> findBookById(long id) {
        return bookRepository.findById(id);
    }

    public Book addBook(String title, String author) {
        return bookRepository.save(title.trim(), author.trim());
    }

    public Optional<Book> updateBook(long id, String title, String author) {
        return bookRepository.updateById(id,
                title == null ? null : title.trim(),
                author == null ? null : author.trim());
    }

    public boolean deleteBook(long id) {
        return bookRepository.deleteById(id);
    }
}
