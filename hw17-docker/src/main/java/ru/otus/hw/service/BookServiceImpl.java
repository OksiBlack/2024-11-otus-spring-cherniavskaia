package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSearchFilter;
import ru.otus.hw.dto.request.SaveBookRequest;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.model.Book;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;
import ru.otus.hw.service.spec.builder.SpecBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
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

    @Override
    public List<BookDto> findAll(String keyword) {

        return bookRepository.findAll(SpecBuilder.Book.buildByKeyword(keyword)).stream()
            .map(bookMapper::mapToDto).toList();
    }

    @Transactional
    @Override
    public List<BookDto> findAll(BookSearchFilter bookSearchFilter) {

        return bookRepository.findAll(SpecBuilder.Book.buildByFilter(bookSearchFilter)).stream()
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

    @Transactional
    @Override
    public BookDto save(SaveBookRequest saveBookRequest) {
        Long bookId = saveBookRequest.getId();
        Book book;
        if (bookId != null) {
            book = bookRepository.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException("Book with id [%s] not found"));
        } else {
            book = new Book();
        }

        var author = authorRepository.findById(saveBookRequest.getAuthorId())
            .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found"
                .formatted(saveBookRequest.getAuthorId())));
        book.setAuthor(author);
        book.setTitle(saveBookRequest.getTitle());
        book.setDescription(saveBookRequest.getDescription());
        book.setIsbn(saveBookRequest.getIsbn());
        book.setSerialNumber(saveBookRequest.getSerialNumber());

        var genres = genreRepository.findAllByIdIn(new HashSet<>(saveBookRequest.getGenreIds()));

        book.setGenres(genres);
        return bookMapper.mapToDto(bookRepository.save(book));
    }
}
