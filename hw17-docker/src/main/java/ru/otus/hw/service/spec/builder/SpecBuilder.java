package ru.otus.hw.service.spec.builder;

import org.springframework.data.jpa.domain.Specification;
import ru.otus.hw.dto.BookSearchFilter;

import static org.apache.commons.lang3.math.NumberUtils.createLong;
import static org.apache.commons.lang3.math.NumberUtils.isCreatable;
import static ru.otus.hw.repository.spec.BookSpecifications.hasAuthorNameLike;
import static ru.otus.hw.repository.spec.BookSpecifications.hasDescriptionLike;
import static ru.otus.hw.repository.spec.BookSpecifications.hasGenreName;
import static ru.otus.hw.repository.spec.BookSpecifications.hasId;
import static ru.otus.hw.repository.spec.BookSpecifications.hasIsbn;
import static ru.otus.hw.repository.spec.BookSpecifications.hasSerialNumber;
import static ru.otus.hw.repository.spec.BookSpecifications.hasTitleLike;

public class SpecBuilder {
    private SpecBuilder() {
    }

    public static class Book {
        private Book() {
        }

        public static Specification<ru.otus.hw.model.Book> buildByKeyword(String filterWord) {

            return Specification.anyOf(
                hasId(isCreatable(filterWord) ? createLong(filterWord) : null),
                hasTitleLike(filterWord),
                hasIsbn(filterWord),
                hasSerialNumber(filterWord),
                hasAuthorNameLike(filterWord),
                hasGenreName(filterWord)
            );
        }

        public static Specification<ru.otus.hw.model.Book> buildByFilter(BookSearchFilter filter) {
            return Specification.allOf(
                hasId(filter.getId()),
                hasTitleLike(filter.getTitle()),
                hasIsbn(filter.getIsbn()),
                hasSerialNumber(filter.getSerialNumber()),
                hasAuthorNameLike(filter.getAuthorName()),
                hasGenreName(filter.getGenreName()),
                hasDescriptionLike(filter.getDescription())
            );
        }
    }
}
