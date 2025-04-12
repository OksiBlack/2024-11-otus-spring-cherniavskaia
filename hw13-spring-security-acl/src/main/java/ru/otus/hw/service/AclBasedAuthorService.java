package ru.otus.hw.service;

import ru.otus.hw.model.Author;

import java.util.List;
import java.util.Set;

public interface AclBasedAuthorService {
    List<Author> findAll();

    Author findById(Long id);

    List<Author> findAllByIds(Set<Long> ids);

    void deleteById(Long id);

    boolean existsById(Long id);

    Author save(Author author);
}