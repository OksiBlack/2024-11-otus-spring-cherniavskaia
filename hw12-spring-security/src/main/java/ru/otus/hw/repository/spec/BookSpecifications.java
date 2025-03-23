package ru.otus.hw.repository.spec;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.hw.model.Book;

public class BookSpecifications {

    public static Specification<Book> hasId(Long id) {
        return (root, query, criteriaBuilder) ->
            id != null ? criteriaBuilder.equal(root.get("id"), id) : null;
    }

    public static Specification<Book> hasTitleLike(String title) {
        return (root, query, criteriaBuilder) ->
            !StringUtils.isEmpty(title) ? criteriaBuilder.like(root.get("title"), "%" + title + "%") : null;
    }

    public static Specification<Book> hasIsbn(String isbn) {
        return (root, query, criteriaBuilder) ->
            !StringUtils.isEmpty(isbn) ? criteriaBuilder.equal(root.get("isbn"), isbn) : null;
    }

    public static Specification<Book> hasSerialNumber(String serialNumber) {
        return (root, query, criteriaBuilder) ->
            !StringUtils.isEmpty(serialNumber) ?
                criteriaBuilder.equal(root.get("serialNumber"), serialNumber) : null;
    }

    public static Specification<Book> hasDescriptionLike(String description) {
        return (root, query, criteriaBuilder) ->
            !StringUtils.isEmpty(description) ?
                criteriaBuilder.like(root.get("description"), "%" + description + "%") : null;
    }

    /**
     * Specification to combine author names with OR logic.
     * This method checks if any of the author names match the given input.
     */
    public static Specification<Book> hasAuthorNameLike(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return null; // return no predicates if name is null or empty
            }

            return Specification.anyOf(hasAuthorFirstNameLike(name),
                hasAuthorLastNameLike(name),
                hasAuthorMiddleNameLike(name)
            ).toPredicate(root, query, criteriaBuilder);
        };
    }

    public static Specification<Book> hasAuthorMiddleNameLike(String authorName) {
        return (root, query, criteriaBuilder) ->
            authorName == null || authorName.isEmpty() ? criteriaBuilder.conjunction() :
                criteriaBuilder.like(root.join("author").get("middleName"), "%" + authorName + "%");
    }

    public static Specification<Book> hasAuthorLastNameLike(String authorName) {
        return (root, query, criteriaBuilder) ->
            authorName == null || authorName.isEmpty() ? criteriaBuilder.conjunction() :
                criteriaBuilder.like(root.join("author").get("lastName"), "%" + authorName + "%");
    }

    public static Specification<Book> hasAuthorFirstNameLike(String authorName) {
        return (root, query, criteriaBuilder) ->
            authorName == null || authorName.isEmpty() ? criteriaBuilder.conjunction() :
                criteriaBuilder.like(root.join("author").get("firstName"), "%" + authorName + "%");
    }

    public static Specification<Book> hasGenreName(String genreName) {
        return (root, query, criteriaBuilder) -> {
            if (genreName == null || genreName.isEmpty()) {
                return criteriaBuilder.conjunction(); // No genre filter applied
            }
            return criteriaBuilder.isTrue(root.join("genres").get("name").in(genreName));
        };
    }
}
