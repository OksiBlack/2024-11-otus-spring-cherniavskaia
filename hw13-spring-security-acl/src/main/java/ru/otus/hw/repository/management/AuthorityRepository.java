package ru.otus.hw.repository.management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.hw.model.management.Authority;

import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<Authority, UUID>, JpaSpecificationExecutor<Authority> {
}
