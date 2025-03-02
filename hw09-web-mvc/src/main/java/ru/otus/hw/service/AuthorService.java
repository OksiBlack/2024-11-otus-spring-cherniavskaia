package ru.otus.hw.service;

import ru.otus.hw.dto.AuthorDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorService {
    List<AuthorDto> findAll();

    Optional<AuthorDto> findById(Long id);

    List<AuthorDto> findAllByIds(Set<Long> ids);

    void deleteById(Long id);

    boolean existsById(Long id);

    AuthorDto save(AuthorDto theAuthor);
}