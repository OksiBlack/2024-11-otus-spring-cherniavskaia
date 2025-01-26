package ru.otus.hw.testObjects;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class GeneratorData {

    public static List<Author> getDbAuthors() {
        return LongStream.range(1, 4)
            .boxed()
            .map(id -> new Author(id, "Author_" + id))
            .toList();
    }

    public static List<Genre> getDbGenres() {
        return LongStream.range(1, 7)
            .boxed()
            .map(id -> new Genre(id, "Genre_" + id))
            .toList();
    }

    public static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return LongStream.range(1L, 4L)
            .boxed()
            .map(id -> new Book(id,
                "BookTitle_" + id,
                dbAuthors.get(id.intValue() - 1),
                dbGenres.subList((id.intValue() - 1) * 2, (id.intValue() - 1) * 2 + 2)
            ))
            .toList();
    }

    public static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    public static Map<Long, List<Comment>> getDbMapCommentsByIdBook() {
        return getDbComments().stream().collect(Collectors.groupingBy(com -> com.getBook().getId()));
    }

    public static List<Comment> getDbComments() {
        var result = new ArrayList<Comment>();
        var dbBooks = getDbBooks().stream().collect(Collectors.toMap(Book::getId, book -> book));

        result.add(new Comment(1L, dbBooks.get(1L), "Comment_1_1", LocalDate.parse("2024-01-01"), "Comment author_1"));
        result.add(new Comment(2L, dbBooks.get(2L), "Comment_2_2", LocalDate.parse("2024-01-02"), "Comment author_2"));
        result.add(new Comment(3L, dbBooks.get(1L), "Comment_1_2", LocalDate.parse("2024-01-04"), "Comment author_2"));
        result.add(new Comment(4L, dbBooks.get(1L), "Comment_1_3", LocalDate.parse("2024-01-04"), "Comment author_3"));

        return result;
    }
}