package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.stereotype.Service;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AclBasedGenreServiceImpl implements AclBasedGenreService {

    private final GenreRepository genreRepository;

    private final AclPermissionEvaluator aclPermissionEvaluator;

    @PostFilter("hasPermission(filterObject, 'READ')")
    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    @Override
    public Set<Genre> findAllByIds(Set<Long> ids) {
        return genreRepository.findAllByIdIn(ids);
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'EDITOR')")
    @Override
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.model.Genre', 'DELETE')")
    @Override
    public void deleteById(Long id) {

        genreRepository.deleteById(id);
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.model.Genre', 'READ')")
    @Override
    public Genre findById(Long id) {

        return genreRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Genre [%s] not found".formatted(id)));
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'EDITOR', 'READER')")
    @Override
    public boolean existsById(Long id) {

        return genreRepository.existsById(id);
    }
}
