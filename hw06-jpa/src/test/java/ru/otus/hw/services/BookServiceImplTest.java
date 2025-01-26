package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.internal.util.collections.CollectionHelper.setOf;

@DisplayName("Service for working with books")
@DataJpaTest
@Import({BookConverter.class, AuthorConverter.class, GenreConverter.class,
    JpaBookRepository.class, JpaAuthorRepository.class,
    JpaGenreRepository.class, BookServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookServiceImplTest {

    private static final int EXPECTED_NUMBER_OF_BOOK = 3;
    private static final long FIRST_BOOK_ID = 2L;
    private static final long NEW_BOOK_ID = 4L;
    private static final Set<String> AUTHOR_FULL_NAME = setOf("Author_1", "Author_2", "Author_3");

    @Autowired
    private BookService service;

    @DisplayName("should load a list of all books with complete information about them")
    @Test
    @Order(1)
    void findAll() {
        var listBookDto = service.findAll();
        assertThat(listBookDto).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOK)
            .allMatch(b -> !b.getTitle().isEmpty())
            .allMatch(b -> b.getAuthor() != null)
            .allMatch(b -> AUTHOR_FULL_NAME.contains(b.getAuthor().getFullName()))
            .allMatch(b -> b.getGenres().size() == 2);
    }

    @DisplayName("should load information about the required book by its ID with complete information")
    @Test
    @Order(2)
    void findById() {
        var optionalActualBookDto = service.findById(FIRST_BOOK_ID);
        assertThat(optionalActualBookDto).isPresent();
        assertThat(optionalActualBookDto.get().getId()).isEqualTo(FIRST_BOOK_ID);
        assertThat(optionalActualBookDto.get().getAuthor()).isNotNull();
        assertThat(optionalActualBookDto.get().getAuthor().getFullName()).isEqualTo("Author_2");
        assertThat(optionalActualBookDto.get().getGenres()).isNotNull()
            .allMatch(g -> !g.getName().isEmpty());
        assertThat(optionalActualBookDto.get().getGenres().size()).isEqualTo(2);
    }

    @DisplayName("should create a book with complete information")
    @Test
    @Order(3)
    void insert() {
        var insertBookDto = service.insert("BookTitle_4", 1L, setOf(3L));
        var optionalExpectedBookDto = service.findById(insertBookDto.getId());

        assertThat(insertBookDto).isEqualTo(optionalExpectedBookDto.get());
        assertThat(insertBookDto).isNotNull();
        assertThat(insertBookDto.getAuthor())
            .isNotNull();
        assertThat(insertBookDto.getGenres()).hasSize(1)
            .allMatch(g -> !g.getName().isEmpty());
    }

    @DisplayName("should update a book with complete information")
    @Test
    @Order(4)
    void update() {
        var insertBookDto = service.update(2L, "BookTitle_5", 1L, setOf(3L));
        var optionalExpectedBookDto = service.findById(insertBookDto.getId());

        assertThat(insertBookDto).isEqualTo(optionalExpectedBookDto.get());
        assertThat(insertBookDto).isNotNull();
        assertThat(insertBookDto.getAuthor())
            .isNotNull();
        assertThat(insertBookDto.getGenres()).hasSize(1)
            .allMatch(g -> !g.getName().isEmpty());
    }

    @DisplayName("should delete a book by its ID")
    @Test
    @Order(5)
    void deleteById() {
        service.deleteById(NEW_BOOK_ID);
        var optionalActualBookDto = service.findById(NEW_BOOK_ID);
        assertThat(optionalActualBookDto).isEmpty();
    }
}
