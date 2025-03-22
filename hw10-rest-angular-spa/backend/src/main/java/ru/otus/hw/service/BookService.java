package ru.otus.hw.service;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSearchFilter;
import ru.otus.hw.dto.request.SaveBookRequest;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(Long id);

    List<BookDto> findAll();

    List<BookDto> findAll(String keyword);

    List<BookDto> findAll(BookSearchFilter bookSearchFilter);

    void deleteById(Long id);

    boolean existsById(Long id);

    BookDto save(SaveBookRequest saveBookRequest);
}
