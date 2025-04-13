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
import ru.otus.hw.dto.BookSearchFilter;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Book;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.service.spec.builder.SpecBuilder;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AclBasedBookServiceImpl implements AclBasedBookService {

    private final BookRepository bookRepository;

    private final AclPermissionEvaluator aclPermissionEvaluator;

    @PostFilter("hasPermission(filterObject, 'READ')")
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    @Override
    public List<Book> findAll(BookSearchFilter filter) {
        return bookRepository.findAll(SpecBuilder.Book.buildByFilter(filter));
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    @Override
    public List<Book> findAll(String keyword) {
        return bookRepository.findAll(SpecBuilder.Book.buildByKeyword(keyword));
    }

    @Override
    public Book findById(Long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Book [%s] not found"
                .formatted(id))
            );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!aclPermissionEvaluator.hasPermission(authentication, book, BasePermission.READ)) {
            throw new AccessDeniedException("No permission to read book [%s].".formatted(id));
        }
        return book;
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    @Override
    public List<Book> findAllByIdsIn(Set<Long> ids) {
        return bookRepository.findAllById(ids);
    }

    @Override
    public void deleteById(Long id) {
        // 1. Fetch the domain object
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Book [%s] not found".formatted(id)));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verify permission
        if (!aclPermissionEvaluator.hasPermission(authentication, book, BasePermission.DELETE)) {
            throw new AccessDeniedException("No permission to delete book [%s].".formatted(id));
        }

        bookRepository.deleteById(id);
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'EDITOR', 'READER')")
    @Override
    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }

    @PreAuthorize("hasPermission(#book, 'WRITE')")
    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }
}
