package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.services.BookService;

import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@Slf4j
@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
            .map(bookConverter::bookToString)
            .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(long id) {
        return bookService.findById(id)
            .map(bookConverter::bookToString)
            .orElse("Book with id %d not found".formatted(id));
    }

    // bins newBook 1 1,6
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, Long authorId, Set<Long> genreIds) {
        var savedBook = bookService.insert(title, authorId, genreIds);
        return bookConverter.bookToString(savedBook);
    }

    // bupd 4 editedBook 3 2,5
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(long id, String title, Long authorId, Set<Long> genreIds) {
        var savedBook = bookService.update(id, title, authorId, genreIds);
        return bookConverter.bookToString(savedBook);
    }

    // bdel 4
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(long id) {
        bookService.deleteById(id);
    }

    @ShellMethod(value = "Exists by id", key = "bexst")
    public boolean existsById(long id) {
        boolean existsById = bookService.existsById(id);

        log.info("exists: {} for book with id {}", id, existsById);
        return existsById;
    }

    @ShellMethod(value = "Upsert book", key = "bups")
    public String upsertBook(long id, String title, Long authorId, Set<Long> genreIds) {
        var savedBook = bookService.upsert(id, title, authorId, genreIds);
        return bookConverter.bookToString(savedBook);
    }
}
