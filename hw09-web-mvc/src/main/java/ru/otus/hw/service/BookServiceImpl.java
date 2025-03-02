package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.controller.mapper.BookMapper;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.model.Book;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> findById(Long id) {
        return bookRepository.findById(id)
            .map(bookMapper::mapToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
            .stream()
            .map(bookMapper::mapToDto)
            .toList();
    }

    @Transactional
    @Override
    public BookDto save(BookDto bookDto) {
        Book book = bookMapper.mapToModel(bookDto);
        authorRepository.save(book.getAuthor());
        book.getGenres().forEach(genreRepository::save);
        return bookMapper.mapToDto(bookRepository.save(book));
    }

    @Transactional
    @Override
    public List<BookDto> findAll(Specification<Book> spec) {

        return bookRepository.findAll(spec).stream()
            .map(bookMapper::mapToDto).toList();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }

}
