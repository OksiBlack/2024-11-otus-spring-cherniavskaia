package ru.otus.hw.repositories;

import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> findById(long id);

    List<Book> findAllSingleQuery();

    List<Book> findAll();

    Book save(Book book);

    void deleteById(long id);

    boolean existsById(long id);

    Book insert(Book book);

    Book update(Book book);
}
