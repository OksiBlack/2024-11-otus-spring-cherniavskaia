package ru.otus.hw.services;

import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<Book> findById(long id);

    List<Book> findAll();

    Book insert(String title, String description,Set<Long> authorsId, Set<Long> genresIds);

    Book update(long id, String title,String description, Set<Long> authorsId, Set<Long> genresIds);

    void deleteById(long id);

    boolean existsById(long id);

    Book upsert(long id, String title, String description, Set<Long> authorIds, Set<Long> genreIds);
}
