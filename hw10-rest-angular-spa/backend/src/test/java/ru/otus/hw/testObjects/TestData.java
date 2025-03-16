package ru.otus.hw.testObjects;

import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;
import ru.otus.hw.model.Genre;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TestData {

    public static List<Author> getDbAuthors() {
        return PredefinedAuthors.getAuthorMap().values().stream().sorted(Comparator.comparing(Author::getId)).toList();
    }

    public static List<Genre> getDbGenres() {
        return PredefinedGenres.getGenreMap().values().stream().sorted(Comparator.comparing(Genre::getId)).toList();
    }

    public static List<Book> getDbBooks() {

        return PredefinedBooks.getBooksMap()
            .values().stream().sorted(Comparator.comparing(Book::getId)).toList();
    }

    public static Map<Long, List<Comment>> getDbMapCommentsByIdBook() {
        return PredefinedComments.getBookCommentMap();
    }
}


