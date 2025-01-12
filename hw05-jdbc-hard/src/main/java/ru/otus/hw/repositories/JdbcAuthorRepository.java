package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private final JdbcClient jdbcClient;

    @Override
    public List<Author> findAll() {
        return jdbcClient.sql("SELECT id, full_name from authors")
            .query(new AuthorRowMapper())
            .list();
    }

    @Override
    public Optional<Author> findById(long id) {
        return jdbcClient.sql("SELECT id, full_name from authors where id = :id")
            .params(Map.of("id", id))
            .query(new AuthorRowMapper())
            .optional();
    }

    @Override
    public boolean existsById(long id) {
        return jdbcClient.sql("SELECT count(*) from authors where id = :id")
            .param("id", id)
            .query(Long.class)
            .single() == 1;
    }

    @Override
    public List<Author> findAllByIds(Set<Long> ids) {
        return jdbcClient.sql("SELECT id, full_name from authors where id in (:ids)")
            .params(Map.of("ids", ids))
            .query(new AuthorRowMapper())
            .list();
    }

    @Override
    public Author insert(Author author) {
        var keyHolder = new GeneratedKeyHolder();

        int update = jdbcClient.sql("insert into authors (full_name) values (:name)")
            .params(Map.of("name", author.getFullName()))
            .update(keyHolder);

        log.info("{} row(s) inserted.", update);

        author.setId(keyHolder.getKeyAs(Long.class));
        return author;
    }

    @Override
    public void deleteById(long id) {
        jdbcClient.sql(
                "delete from authors where id = :id")
            .params(Map.of("id", id))
            .update();
    }

    @Override
    public Author update(Author author) {

        jdbcClient.sql(
                "update authors set full_name = :name where id = :id")
            .param("id", author.getId())
            .param("name", author.getFullName())
            .update();

        log.info("{} updated.", author);
        return author;
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String fullName = rs.getString("full_name");
            return new Author(id, fullName);
        }
    }
}
