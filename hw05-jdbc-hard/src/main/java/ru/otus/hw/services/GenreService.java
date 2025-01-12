package ru.otus.hw.services;

import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreService {
    List<Genre> findAll();

    List<Genre> findAllByIds(Set<Long> ids);

    Genre insert(String genreName);

    void deleteById(long id);

    Optional<Genre> findById(long id);

    Genre updateGenre(long id, String name);

    Genre upsertGenre(long id, String name);

    boolean existsById(long id);
}
