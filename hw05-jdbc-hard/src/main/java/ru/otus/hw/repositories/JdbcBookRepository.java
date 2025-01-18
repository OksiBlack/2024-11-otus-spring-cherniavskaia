package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final JdbcClient jdbcClient;

    @Override
    public Optional<Book> findById(long id) {

        var sql = """
            SELECT b.id, b.title, a.id as author_id, a.full_name as author_name, g.id as genre_id, g.name as genre_name
            FROM books b LEFT JOIN authors a on b.author_id = a.id left join books_genres bg
            ON b.id = bg.book_id LEFT JOIN genres g on bg.genre_id = g.id
            WHERE b.id =:id""";

        return jdbcClient.sql(sql)
            .param("id", id)
            .query(new SingleBookResultSetExtractor());
    }

    @Override
    public List<Book> findAllSingleQuery() {
        return jdbcClient.sql("""
                SELECT
                 b.id as id,
                 b.title as title,
                 b.author_id as author_id,
                 a.full_name as author_name,
                 g.id as genre_id,
                 g.name as genre_name,
                 FROM books b
                 LEFT JOIN authors a on b.author_id = a.id
                 LEFT JOIN books_genres bg on b.id = bg.book_id
                 LEFT JOIN genres g on g.id = bg.genre_id""")
            .query(new BookListResultExtractor());
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbcClient.sql(
                "DELETE FROM books where id = :id")
            .params(Map.of("id", id))
            .update();
    }

    @Override
    public boolean existsById(long id) {
        return jdbcClient.sql("SELECT count(*) FROM books where id = :id")
            .param("id", id)
            .query(Long.class)
            .single() == 1;
    }

    private List<Book> getAllBooksWithoutGenres() {
        var sql = """
            SELECT b.id, b.title, a.id as author_id, a.full_name as author_name 
            FROM books b left join authors a on b.author_id = a.id""";

        return jdbcClient.sql(sql)
            .query(new BookRowMapper())
            .list();
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbcClient.sql("SELECT book_id, genre_id FROM books_genres")
            .query(new BookGenreRelationRowMapper())
            .list();
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        Map<Long, Book> booksMap = booksWithoutGenres.stream()
            .collect(Collectors.toMap(Book::getId, it -> it));

        Map<Long, Genre> genreMap = genres.stream()
            .collect(Collectors.toMap(Genre::getId, it -> it));

        for (BookGenreRelation bookGenreRelation : relations) {
            Book book = booksMap.get(bookGenreRelation.bookId());
            if (book.getGenres() == null) {
                book.setGenres(new ArrayList<>());
            }
            book.getGenres().add(genreMap.get(bookGenreRelation.genreId()));
        }
        // Добавить книгам (booksWithoutGenres) жанры (genres) в соответствии со связями (relations)
    }

    public Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("INSERT INTO books (title, author_id) VALUES (:title, :authorId)")
            .params(
                Map.of("title", book.getTitle(),
                    "authorId", book.getAuthor().getId())
            )
            .update(keyHolder);

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);

        return book;
    }

    public Book update(Book book) {
        jdbcClient.sql("UPDATE books SET title = :title, author_id = :authorId WHERE id = :id")
            .params(Map.of(
                "title", book.getTitle(),
                "authorId", book.getAuthor().getId(),
                "id", book.getId()
            ))
            .update();
        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        var relations = book.getGenres().stream()
            .map(genre -> new BookGenreRelation(book.getId(), genre.getId()))
            .collect(Collectors.toCollection(ArrayList::new));
        var params = SqlParameterSourceUtils.createBatch(relations);

        final String sql = "INSERT INTO books_genres (book_id, genre_id) VALUES (:bookId, :genreId)";

        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

    private void removeGenresRelationsFor(Book book) {
        jdbcClient.sql("DELETE FROM books_genres WHERE book_id = :bookId ")
            .param("bookId", book.getId())
            .update();
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));

            Author author = new Author();
            author.setId(rs.getLong("author_id"));
            author.setFullName(rs.getString("full_name"));

            book.setAuthor(author);
            book.setGenres(null);

            return book;
        }
    }

    // Использовать для findById
    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class SingleBookResultSetExtractor implements ResultSetExtractor<Optional<Book>> {

        @Override
        public Optional<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = new Book();
            if (!rs.next()) {
                return Optional.empty();
            }

            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));

            Author author = new Author();
            author.setId(rs.getLong("author_id"));
            author.setFullName(rs.getString("author_name"));

            final Map<Long, Genre> mapGenres = new HashMap<>();

            do {
                extractedGenre(rs, mapGenres);
            } while (rs.next());

            book.setAuthor(author);
            book.setGenres(mapGenres.values().stream().toList());
            return Optional.of(book);
        }

        private void extractedGenre(ResultSet rs, Map<Long, Genre> mapGenres) throws SQLException {
            var genreId = rs.getLong("genre_id");
            if (!mapGenres.containsKey(genreId)) {
                var genreName = rs.getString("genre_name");
                mapGenres.put(genreId, new Genre(genreId, genreName));
            }
        }
    }

    private static class BookListResultExtractor implements ResultSetExtractor<List<Book>> {

        @Override
        public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            var booksByIdMap = new HashMap<Long, Book>();
            while (rs.next()) {
                var bookId = rs.getLong("id");
                booksByIdMap.putIfAbsent(bookId, extractBook(rs));
                booksByIdMap.get(bookId).getGenres().add(extractGenre(rs));
            }
            return booksByIdMap.values().stream().toList();
        }

        private Book extractBook(ResultSet rs) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));
            book.setGenres(new ArrayList<>());
            book.setAuthor(extractAuthor(rs));
            return book;
        }

        private Author extractAuthor(ResultSet rs) throws SQLException {
            return new Author(
                rs.getLong("author_id"),
                rs.getString("author_name"));
        }

        private Genre extractGenre(ResultSet rs) throws SQLException {
            var genreId = rs.getLong("genre_id");
            var genreName = rs.getString("genre_name");
            return new Genre(genreId, genreName);
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }

    private static class BookGenreRelationRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int i) throws SQLException {
            long genreId = rs.getLong("genre_id");
            long bookId = rs.getLong("book_id");
            return new BookGenreRelation(bookId, genreId);
        }
    }
}
