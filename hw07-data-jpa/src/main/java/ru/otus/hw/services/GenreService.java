package ru.otus.hw.services;

import ru.otus.hw.dto.GenreDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreService {
    List<GenreDto> findAll();

    List<GenreDto> findAllByIds(Set<Long> ids);

    GenreDto create(String genreName);

    void deleteById(Long id);

    Optional<GenreDto> findById(Long id);

    GenreDto update(Long id, String name);

    boolean existsById(Long id);
}
