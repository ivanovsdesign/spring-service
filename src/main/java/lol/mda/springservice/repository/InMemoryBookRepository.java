package lol.mda.springservice.repository;

import lol.mda.springservice.model.Book;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryBookRepository implements BookRepository {

    private final AtomicLong nextId = new AtomicLong(1);
    private final ConcurrentHashMap<Long, Book> books = new ConcurrentHashMap<>();

    public InMemoryBookRepository() {
        save("Clean Code", "Robert C. Martin");
        save("The Pragmatic Programmer", "Andrew Hunt and David Thomas");
    }

    @Override
    public List<Book> findAll() {
        return books.values().stream()
                .sorted(Comparator.comparingLong(Book::id))
                .toList();
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(books.get(id));
    }

    @Override
    public Book save(String title, String author) {
        Book book = new Book(nextId.getAndIncrement(), title, author);
        books.put(book.id(), book);
        return book;
    }

    @Override
    public Optional<Book> updateById(long id, String title, String author) {
        Book updatedBook = books.computeIfPresent(id, (bookId, existingBook) -> new Book(
                bookId,
                title == null ? existingBook.title() : title,
                author == null ? existingBook.author() : author));
        return Optional.ofNullable(updatedBook);
    }

    @Override
    public boolean deleteById(long id) {
        return books.remove(id) != null;
    }
}
