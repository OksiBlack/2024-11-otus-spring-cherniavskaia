package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;
import ru.otus.hw.model.Genre;

import java.util.Set;

public interface GenreRepository extends ListCrudRepository<Genre, Long>, JpaSpecificationExecutor<Genre> {
    Set<Genre> findAllByIdIn(Set<Long> ids);
}
