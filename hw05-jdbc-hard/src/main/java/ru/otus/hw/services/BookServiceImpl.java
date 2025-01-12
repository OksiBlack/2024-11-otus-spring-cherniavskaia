package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    @Override
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book insert(String title, String description, Set<Long> authorsId, Set<Long> genresIds) {
        return save(0, title, description, authorsId, genresIds);
    }

    @Override
    public Book update(long id, String title, String description, Set<Long> authorsId, Set<Long> genresIds) {
        return save(id, title, description, authorsId, genresIds);
    }

    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public boolean existsById(long id) {
        return bookRepository.existsById(id);
    }

    @Override
    public Book upsert(long id, String title, String description, Set<Long> authorIds, Set<Long> genreIds) {
        if (existsById(id)) {
            return update(id, title, description, authorIds, genreIds);
        } else {
            return insert(title, description, authorIds, genreIds);
        }
    }

    private Book save(long id, String title, String description, Set<Long> authorsId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be empty or null");
        }

        if (isEmpty(authorsId)) {
            throw new IllegalArgumentException("Author ids must not be empty or null");
        }

        var authors = authorRepository.findAllByIds(authorsId);

        if (isEmpty(authors) || authorsId.size() != authors.size()) {
            throw new EntityNotFoundException("One or all author with ids %s not found".formatted(authorsId));
        }

        var genres = genreRepository.findAllByIds(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var book = new Book(id, title, description, authors, genres);
        return bookRepository.save(book);
    }
}
