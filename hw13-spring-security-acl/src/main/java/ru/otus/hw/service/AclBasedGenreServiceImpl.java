package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PreAuthorize("hasPermission(#genre, 'WRITE')")
    @Override
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public void deleteById(Long id) {
        // 1. Fetch the domain object
        Genre genre = genreRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Genre [%s] not found".formatted(id)));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verify permission
        if (!aclPermissionEvaluator.hasPermission(authentication, genre, BasePermission.DELETE)) {
            throw new AccessDeniedException("No permission to delete genre [%s].".formatted(id));
        }

        genreRepository.deleteById(id);
    }

    @Override
    public Genre findById(Long id) {
        // 1. Fetch the domain object

        Genre genre = genreRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Genre [%s] not found".formatted(id)));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verify permission
        if (!aclPermissionEvaluator.hasPermission(authentication, genre, BasePermission.READ)) {
            throw new AccessDeniedException
                ("No permission to read genre with id [%s].".formatted(id)
                );
        }
        return genre;
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'EDITOR', 'READER')")
    @Override
    public boolean existsById(Long id) {

        return genreRepository.existsById(id);
    }
}
