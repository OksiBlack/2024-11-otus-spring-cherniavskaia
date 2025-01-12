package ru.otus.hw.repositories;

import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository {
    List<Genre> findAll();

    Optional<Genre> findById(long id);

    List<Genre> findAllByIds(Set<Long> ids);

    Genre insert(Genre genre);

    void deleteById(long id);

    boolean existsById(long id);

    Genre update(Genre genre);
}
