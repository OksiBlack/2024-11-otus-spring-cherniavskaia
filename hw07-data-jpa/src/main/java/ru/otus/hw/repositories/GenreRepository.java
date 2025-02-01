package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

public interface GenreRepository extends ListCrudRepository<Genre, Long>, JpaSpecificationExecutor<Genre> {
    List<Genre> findAllByIdIn(Set<Long> ids);
}
