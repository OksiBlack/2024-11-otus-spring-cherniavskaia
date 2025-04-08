package ru.otus.hw.repository.spec;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.hw.model.management.Authority;

public class AuthoritySpecifications {

    public static Specification<Authority> hasId(Long id) {
        return (root, query, criteriaBuilder) ->
            id != null ? criteriaBuilder.equal(root.get("id"), id) : null;
    }

    public static Specification<Authority> hasName(String name) {
        return (root, query, criteriaBuilder) ->
            !StringUtils.isEmpty(name) ? criteriaBuilder.like(root.get("login"), "%" + name + "%") : null;
    }
}
