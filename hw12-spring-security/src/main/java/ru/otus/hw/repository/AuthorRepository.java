package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;
import ru.otus.hw.model.Author;

import java.util.List;
import java.util.Set;

public interface AuthorRepository extends ListCrudRepository<Author, Long>, JpaSpecificationExecutor<Author> {
    List<Author> findAllByIdIn(Set<Long> ids);
}
