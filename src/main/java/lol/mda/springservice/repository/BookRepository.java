package lol.mda.springservice.repository;

import lol.mda.springservice.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    List<Book> findAll();

    Optional<Book> findById(long id);

    Book save(String title, String author);

    Optional<Book> updateById(long id, String title, String author);

    boolean deleteById(long id);
}
