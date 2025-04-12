package ru.otus.hw.repository.management;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.hw.model.management.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import static ru.otus.hw.repository.EntityGraphNames.USER_ROLES_ENTITY_GRAPH;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    @EntityGraph(value = USER_ROLES_ENTITY_GRAPH, type = EntityGraphType.FETCH)
    Optional<User> findByLogin(String login);

    @EntityGraph(value = USER_ROLES_ENTITY_GRAPH, type = EntityGraphType.FETCH)
    @Override
    Optional<User> findOne(@Nonnull Specification<User> spec);

    @EntityGraph(value = USER_ROLES_ENTITY_GRAPH, type = EntityGraphType.FETCH)
    @Override
    List<User> findAll(Specification<User> spec);

    @EntityGraph(value = USER_ROLES_ENTITY_GRAPH, type = EntityGraphType.FETCH)
    @Override
    Page<User> findAll(Specification<User> spec, @Nonnull Pageable pageable);

    @EntityGraph(value = USER_ROLES_ENTITY_GRAPH, type = EntityGraphType.FETCH)
    @Override
    List<User> findAll(Specification<User> spec, @Nonnull Sort sort);
}
