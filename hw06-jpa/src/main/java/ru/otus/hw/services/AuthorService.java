package ru.otus.hw.services;

import ru.otus.hw.dto.AuthorDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorService {
    List<AuthorDto> findAll();

    AuthorDto create(String fullName);

    Optional<AuthorDto> findById(Long id);

    List<AuthorDto> findAllByIds(Set<Long> ids);

    void deleteById(Long id);

    AuthorDto update(Long id, String fullName);

    boolean existsById(Long id);
}