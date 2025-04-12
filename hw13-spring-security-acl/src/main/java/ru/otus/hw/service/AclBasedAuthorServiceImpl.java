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
import ru.otus.hw.model.Author;
import ru.otus.hw.repository.AuthorRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AclBasedAuthorServiceImpl implements AclBasedAuthorService {

    private final AuthorRepository authorRepository;

    private final AclPermissionEvaluator aclPermissionEvaluator;

    @PostFilter("hasPermission(filterObject, 'READ')")
    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author findById(Long id) {
        Author author = authorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Author [%s] not found"
                .formatted(id))
            );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!aclPermissionEvaluator.hasPermission(authentication, author, BasePermission.READ)) {
            throw new AccessDeniedException("No permission to read author [%s].".formatted(id));
        }
        return author;
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    @Override
    public List<Author> findAllByIds(Set<Long> ids) {
        return authorRepository.findAllByIdIn(ids);
    }

    @Override
    public void deleteById(Long id) {
        // 1. Fetch the domain object
        Author author = authorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Author [%s] not found".formatted(id)));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verify permission
        if (!aclPermissionEvaluator.hasPermission(authentication, author, BasePermission.DELETE)) {
            throw new AccessDeniedException("No permission to delete author [%s].".formatted(id));
        }

        authorRepository.deleteById(id);
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'EDITOR', 'READER')")
    @Override
    public boolean existsById(Long id) {
        return authorRepository.existsById(id);
    }

    @PreAuthorize("hasPermission(#author, 'WRITE')")
    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }
}
