package ru.otus.hw.service;

import org.springframework.data.jpa.domain.Specification;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.SaveBookRequest;
import ru.otus.hw.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(Long id);

    List<BookDto> findAll();

    List<BookDto>  findAll(Specification<Book> spec);

    void deleteById(Long id);

    boolean existsById(Long id);

    BookDto save(SaveBookRequest saveBookRequest);
}
