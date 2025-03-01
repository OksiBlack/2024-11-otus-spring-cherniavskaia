package ru.otus.hw.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends ListCrudRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @EntityGraph(value = EntityGraphNames.FULL_BOOK_ENTITY_GRAPH)
    @Override
    Optional<Book> findById(Long id);

    @EntityGraph(value = EntityGraphNames.FULL_BOOK_ENTITY_GRAPH)
    @Override
    Optional<Book> findOne(Specification<Book> spec);

    @EntityGraph(value = EntityGraphNames.BOOK_AUTHOR_ENTITY_GRAPH)
    @Override
    List<Book> findAll();

    @EntityGraph(value = EntityGraphNames.BOOK_AUTHOR_ENTITY_GRAPH)
    @Override
    List<Book> findAll(Specification<Book> spec);

    @EntityGraph(value = EntityGraphNames.BOOK_AUTHOR_ENTITY_GRAPH)
    @Override
    Page<Book> findAll(Specification<Book> spec, Pageable pageable);
}
