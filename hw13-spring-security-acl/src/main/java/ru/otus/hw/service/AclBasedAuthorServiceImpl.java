package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Author;
import ru.otus.hw.repository.AuthorRepository;

import java.util.List;
import java.util.Set;

@EnableMethodSecurity
@Service
@RequiredArgsConstructor
public class AclBasedAuthorServiceImpl implements AclBasedAuthorService {

    private final AuthorRepository authorRepository;

    @PostFilter("hasPermission(filterObject, 'READ')")
    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.model.Author', 'READ')")
    @Override
    public Author findById(Long id) {

        return authorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Author [%s] not found"
                .formatted(id))
            );
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    @Override
    public List<Author> findAllByIds(Set<Long> ids) {
        return authorRepository.findAllByIdIn(ids);
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.model.Author', 'DELETE')")
    @Override
    public void deleteById(Long id) {

        authorRepository.deleteById(id);
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.model.Author', 'READ')")
    @Override
    public boolean existsById(Long id) {

        return authorRepository.existsById(id);
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'EDITOR')")
    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }
}
