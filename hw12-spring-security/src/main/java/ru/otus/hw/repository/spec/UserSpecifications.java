package ru.otus.hw.repository.spec;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.management.User;

public class UserSpecifications {

    public static Specification<User> hasId(Long id) {
        return (root, query, criteriaBuilder) ->
            id != null ? criteriaBuilder.equal(root.get("id"), id) : null;
    }

    public static Specification<User> hasLogin(String login) {
        return (root, query, criteriaBuilder) ->
            !StringUtils.isEmpty(login) ? criteriaBuilder.like(root.get("login"), "%" + login + "%") : null;
    }

    public static Specification<Book> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) ->
            !StringUtils.isEmpty(firstName) ? criteriaBuilder.equal(root.get("firstName"), firstName) : null;
    }

    public static Specification<Book> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) ->
            !StringUtils.isEmpty(lastName) ?
                criteriaBuilder.equal(root.get("lastName"), lastName) : null;
    }
}
