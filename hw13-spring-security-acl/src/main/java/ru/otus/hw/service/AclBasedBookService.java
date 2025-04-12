package ru.otus.hw.service;

import ru.otus.hw.dto.BookSearchFilter;
import ru.otus.hw.model.Book;

import java.util.List;
import java.util.Set;

public interface AclBasedBookService {
    Book findById(Long id);

    List<Book> findAll();

    List<Book> findAll(BookSearchFilter filter);

    List<Book> findAll(String keyword);

    List<Book> findAllByIdsIn(Set<Long> ids);

    void deleteById(Long id);

    boolean existsById(Long id);

    Book save(Book book);
}
