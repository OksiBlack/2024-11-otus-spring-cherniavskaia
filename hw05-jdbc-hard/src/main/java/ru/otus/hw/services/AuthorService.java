package ru.otus.hw.services;

import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorService {
    List<Author> findAll();

    Author insert(String fullName);

    Optional<Author> findById(long id);

    List<Author> findAllByIds(Set<Long> ids);

    void deleteById(long id);

    Author updateAuthor(long id, String fullName);

    Author upsert(long id, String fullName);

    boolean existsById(long id);
}