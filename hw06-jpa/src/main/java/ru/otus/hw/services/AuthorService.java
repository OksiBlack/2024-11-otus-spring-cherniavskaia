package ru.otus.hw.services;

import ru.otus.hw.dto.AuthorDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorService {
    List<AuthorDto> findAll();

    AuthorDto insert(String fullName);

    Optional<AuthorDto> findById(Long id);

    List<AuthorDto> findAllByIds(Set<Long> ids);

    void deleteById(Long id);

    AuthorDto updateAuthor(Long id, String fullName);

    AuthorDto upsert(Long id, String fullName);

    boolean existsById(Long id);
}