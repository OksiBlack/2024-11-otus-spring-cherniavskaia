package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final Converter<Book, BookDto> bookBookDtoConverter;

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> findById(Long id) {
        return bookRepository.findById(id)
            .map(bookBookDtoConverter::convert);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
            .stream()
            .map(bookBookDtoConverter::convert)
            .toList();
    }

    @Transactional
    @Override
    public BookDto create(String title, Long authorId, Set<Long> genresIds) {

        return save(new Book(), title, authorId, genresIds);
    }

    @Transactional
    @Override
    public BookDto update(Long id, String title, Long authorId, Set<Long> genresIds) {
        Book book = bookRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Book with id [%s] not found");
        });

        return save(book, title, authorId, genresIds);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    private BookDto save(Book book, String title, Long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
            .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findAllByIdIn(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }
        book.setGenres(genres);
        book.setTitle(title);
        book.setAuthor(author);

        return bookBookDtoConverter.convert(bookRepository.save(book));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }
}
