package ru.otus.hw.service;

import ru.otus.hw.model.Genre;

import java.util.List;
import java.util.Set;

public interface AclBasedGenreService {
    List<Genre> findAll();

    Set<Genre> findAllByIds(Set<Long> ids);

    Genre save(Genre genreDto);

    void deleteById(Long id);

    Genre findById(Long id);

    boolean existsById(Long id);
}

