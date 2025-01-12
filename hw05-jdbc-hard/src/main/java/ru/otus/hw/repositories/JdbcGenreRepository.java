package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Repository
public class JdbcGenreRepository implements GenreRepository {

    private final JdbcClient jdbcClient;

    @Override
    public List<Genre> findAll() {
        return jdbcClient.sql("SELECT id, name from genres")
            .query(new GnreRowMapper())
            .list();
    }

    @Override
    public Optional<Genre> findById(long id) {
        return jdbcClient.sql("SELECT id, name  from genres where id = :id")
            .params(Map.of("id", id))
            .query(new GnreRowMapper())
            .optional();
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        return jdbcClient.sql("SELECT id, name from genres where id in (:ids)")
            .params(Map.of("ids", ids))
            .query(new GnreRowMapper())
            .list();
    }

    @Override
    public Genre insert(Genre genre) {
        var keyHolder = new GeneratedKeyHolder();

        int update = jdbcClient.sql("insert into genres (name) values (:name)")
            .params(Map.of("name", genre.getName()))
            .update(keyHolder);

        log.info("{} row(s) inserted.", update);

        genre.setId(keyHolder.getKeyAs(Long.class));
        return genre;
    }

    @Override
    public void deleteById(long id) {
        jdbcClient.sql(
                "delete from genres where id = :id")
            .params(Map.of("id", id))
            .update();
    }

    @Override
    public boolean existsById(long id) {
        return jdbcClient.sql("SELECT count(*) from genres where id = :id")
            .param("id", id)
            .query(Long.class)
            .single() == 1;
    }

    @Override
    public Genre update(Genre genre) {
        jdbcClient.sql(
                "update genres set name = :name where id = :id")
            .param("id", genre.getId())
            .param("name", genre.getName())
            .update();

        log.info("{} updated.", genre);
        return genre;
    }

    private static class GnreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");

            return new Genre(id, name);
        }
    }
}
