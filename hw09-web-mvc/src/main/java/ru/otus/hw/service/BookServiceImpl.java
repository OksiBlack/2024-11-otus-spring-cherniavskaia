package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSaveRequest;
import ru.otus.hw.dto.CreateBookRequest;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Book;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;

import java.util.HashSet;
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
//        authorRepository.save(book.getAuthor());
//        book.getGenres().forEach(genreRepository::save);
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

    @Transactional
    @Override
    public BookDto create(CreateBookRequest createBookRequest) {
        Book book = new Book();

        var author = authorRepository.findById(createBookRequest.getAuthorId())
            .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(createBookRequest.getAuthorId())));
        book.setAuthor(author);

        book.setTitle(createBookRequest.getTitle());
        book.setDescription(createBookRequest.getDescription());
        book.setIsbn(createBookRequest.getIsbn());
        book.setSerialNumber(createBookRequest.getSerialNumber());

        var genres = genreRepository.findAllByIdIn(new HashSet<>(createBookRequest.getGenreIds()));

        book.setGenres(genres);
        return bookMapper.mapToDto(bookRepository.save(book));
    }

    @Transactional
    @Override
    public BookDto save(BookSaveRequest bookSaveRequest) {
        Long bookId = bookSaveRequest.getId();
        Book book;
        if (bookId != null) {
            book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book with id [%s] not found"));
        } else {
            book = new Book();
        }

        var author = authorRepository.findById(bookSaveRequest.getAuthorId())
            .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(bookSaveRequest.getAuthorId())));
        book.setAuthor(author);
        book.setTitle(bookSaveRequest.getTitle());
        book.setDescription(bookSaveRequest.getDescription());
        book.setIsbn(bookSaveRequest.getIsbn());
        book.setSerialNumber(bookSaveRequest.getSerialNumber());

        var genres = genreRepository.findAllByIdIn(new HashSet<>(bookSaveRequest.getGenreIds()));

        book.setGenres(genres);
        return bookMapper.mapToDto(bookRepository.save(book));
    }

}
