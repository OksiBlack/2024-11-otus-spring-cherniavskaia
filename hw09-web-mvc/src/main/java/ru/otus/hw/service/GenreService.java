package ru.otus.hw.service;

import ru.otus.hw.dto.GenreDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreService {
    List<GenreDto> findAll();

    List<GenreDto> findAllByIds(Set<Long> ids);

    GenreDto save(GenreDto genreDto);

    void deleteById(Long id);

    Optional<GenreDto> findById(Long id);

    boolean existsById(Long id);
}
