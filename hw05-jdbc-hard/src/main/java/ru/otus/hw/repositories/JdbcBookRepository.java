package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.EntityNotUniqueException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final AuthorRepository authorRepository;

    private final JdbcClient jdbcClient;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Book> findById(long id) {
        Optional<Book> optionalBook = jdbcClient.sql("SELECT id, title, description from books where id = :id")
            .params(Map.of("id", id))
            .query(new BookResultSetExtractor());

        optionalBook.ifPresent(book -> {
            var genres = genreRepository.findAll();
            var authors = authorRepository.findAll();
            List<BookGenreRelation> genreRelations = getAllGenreRelations();
            List<BookAuthorRelation> authorRelations = getAllAuthorRelations();

            enrichBookWithAuthors(authors, authorRelations, book);
            enrichBookWithGenres(genres, genreRelations, book);
        });
        return optionalBook;
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var authors = authorRepository.findAll();
        List<BookGenreRelation> genreRelations = getAllGenreRelations();
        List<BookAuthorRelation> authorRelations = getAllAuthorRelations();
        var books = getAllBooksWithoutRelations();

        enrichBooksWithAuthors(books, authors, authorRelations);
        enrichBooksWithGenres(books, genres, genreRelations);
        return books;
    }

    private List<BookAuthorRelation> getAllAuthorRelations() {
        return jdbcClient.sql("SELECT book_id, author_id from books_authors")
            .query(new BookAuthorRelationRowMapper())
            .list();
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
                "delete from books where id = :id")
            .params(Map.of("id", id))
            .update();
    }

    @Override
    public boolean existsById(long id) {
        return jdbcClient.sql("SELECT count(*) from books where id = :id")
            .param("id", id)
            .query(Long.class)
            .single() == 1;
    }

    private List<Book> getAllBooksWithoutRelations() {
        return jdbcClient.sql("SELECT id, title, description from books").query(new BookRowMapper()).list();
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbcClient.sql("SELECT book_id, genre_id from books_genres")
            .query(new BookGenreRelationRowMapper())
            .list();
    }

    private void enrichBooksWithAuthors(List<Book> books, List<Author> authors,
                                        List<BookAuthorRelation> authorRelations) {
        books.forEach(book -> enrichBookWithAuthors(authors, authorRelations, book));
    }

    private void enrichBookWithAuthors(List<Author> authors, List<BookAuthorRelation> authorRelations, Book book) {
        List<Author> authorsForBook = authorRelations.stream()
            .filter(it -> it.bookId == book.getId())
            .map(bookAuthorRelation -> authors.stream()
                .filter(au -> au.getId() == bookAuthorRelation.authorId)
                .findFirst()
                .orElseThrow(() ->
                    new EntityNotFoundException("Author with id " + bookAuthorRelation.authorId + " not found"))
            ).toList();

        book.setAuthors(authorsForBook);
    }

    private void enrichBooksWithGenres(List<Book> books, List<Genre> genres, List<BookGenreRelation> genreRelations) {
        // Добавить книгам (booksWithoutGenres) жанры (genres) в соответствии со связями (relations)
        books.forEach(book -> enrichBookWithGenres(genres, genreRelations, book));
    }

    private void enrichBookWithGenres(List<Genre> genres, List<BookGenreRelation> genreRelations, Book book) {
        List<Genre> genresForBook = genreRelations.stream()
            .filter(it -> it.bookId == book.getId())
            .map(bookGenreRelation -> genres.stream()
                .filter(genre -> genre.getId() == bookGenreRelation.genreId)
                .findFirst()
                .orElseThrow(() ->
                    new EntityNotFoundException("Genre with id " + bookGenreRelation.genreId + " not found"))
            ).toList();
        book.setGenres(genresForBook);
    }

    @Override
    public Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("insert into books (title, description) values (:title, :description)")
            .params(Map.of("title", book.getTitle(), "description", book.getDescription()))
            .update(keyHolder);

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        batchInsertAuthorsRelationsFor(book);
        return book;
    }

    private void batchInsertAuthorsRelationsFor(Book book) {

        List<Author> authors = book.getAuthors();

        int[] updates = jdbcTemplate.batchUpdate("insert into books_authors (book_id, author_id) values (?, ?)",
            new BatchPreparedStatementSetter() {

                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Author author = authors.get(i);
                    ps.setLong(1, book.getId());
                    ps.setLong(2, author.getId());
                }

                @Override
                public int getBatchSize() {
                    return authors.size();
                }
            }

        );

        log.debug("Updated rows: {} ", updates);
    }

    @Override
    public Book update(Book book) {
        //...
        jdbcClient.sql("update books set title = :title, description = :description where id = :id")
            .params(Map.of(
                "title", book.getTitle(),
                "description", book.getDescription(),
                "id", book.getId()
            ))
            .update();

        removeAllGenresRelationsFor(book);
        removeAllAuthorsRelationsFor(book);
        batchInsertGenresRelationsFor(book);
        batchInsertAuthorsRelationsFor(book);

        return book;
    }

    private void removeAllAuthorsRelationsFor(Book book) {

        final String sql = "DELETE FROM books_authors WHERE book_id = :bookId";

        MapSqlParameterSource[] mapSqlParameterSources = book.getAuthors()
            .stream()
            .map(author -> new MapSqlParameterSource("bookId", book.getId()))
            .toArray(MapSqlParameterSource[]::new);

        int[] batchUpdate = namedParameterJdbcTemplate.batchUpdate(sql, mapSqlParameterSources);

        log.debug("Updated rows: {} ", batchUpdate);
    }

    private void batchInsertGenresRelationsFor(Book book) {
        // Использовать метод batchUpdate
        List<Genre> genres = book.getGenres();

        int[] updates = jdbcTemplate.batchUpdate("insert into books_genres (book_id, genre_id) values (?, ?)",
            new BatchPreparedStatementSetter() {

                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Genre genre = genres.get(i);
                    ps.setLong(1, book.getId());
                    ps.setLong(2, genre.getId());
                }

                @Override
                public int getBatchSize() {
                    return genres.size();
                }
            }

        );

        log.debug("Updated rows: {} ", updates);
    }

    private void removeAllGenresRelationsFor(Book book) {

        MapSqlParameterSource[] mapSqlParameterSources = book.getGenres()
            .stream()
            .map(genre -> new MapSqlParameterSource("bookId", book.getId()))
            .toArray(MapSqlParameterSource[]::new);

        final String sql = "DELETE FROM books_genres WHERE book_id = :bookId";

        namedParameterJdbcTemplate.batchUpdate(sql, mapSqlParameterSources);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            String description = rs.getString("description");

            Book book = new Book();
            book.setId(id);
            book.setTitle(title);
            book.setDescription(description);

            return book;
        }
    }

    // Использовать для findById
    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Optional<Book>> {

        @Override
        public Optional<Book> extractData(ResultSet rs) throws SQLException {
            BookRowMapper bookRowMapper = new BookRowMapper();
            if (rs.next()) {
                Book book = bookRowMapper.mapRow(rs, 0);
                if (rs.next()) {
                    throw new EntityNotUniqueException("Not single entity found");
                }
                return Optional.of(book);
            }

            return Optional.empty();
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }

    private record BookAuthorRelation(long bookId, long authorId) {
    }

    private static class BookAuthorRelationRowMapper implements RowMapper<BookAuthorRelation> {

        @Override
        public BookAuthorRelation mapRow(ResultSet rs, int i) throws SQLException {
            long authorId = rs.getLong("author_id");
            long bookId = rs.getLong("book_id");
            return new BookAuthorRelation(bookId, authorId);
        }
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
