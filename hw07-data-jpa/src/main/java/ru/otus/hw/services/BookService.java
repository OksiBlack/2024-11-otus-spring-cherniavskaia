package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<BookDto> findById(Long id);

    List<BookDto> findAll();

    BookDto create(String title, Long authorId, Set<Long> genresIds);

    BookDto update(Long id, String title, Long authorId, Set<Long> genresIds);

    void deleteById(Long id);

    boolean existsById(Long id);

}
