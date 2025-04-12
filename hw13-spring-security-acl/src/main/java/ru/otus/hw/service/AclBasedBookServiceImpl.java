package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.AclPermissionEvaluator;
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

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.model.Book', 'READ')")
    @Override
    public Book findById(Long id) {

        return bookRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Book [%s] not found"
                .formatted(id))
            );
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    @Override
    public List<Book> findAllByIdsIn(Set<Long> ids) {
        return bookRepository.findAllById(ids);
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.model.Book', 'DELETE')")
    @Override
    public void deleteById(Long id) {

        bookRepository.deleteById(id);
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.model.Book', 'READ')")
    @Override
    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'EDITOR')")
    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }
}
