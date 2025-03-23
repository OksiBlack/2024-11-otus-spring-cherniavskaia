package ru.otus.hw.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.hw.model.Author;
import ru.otus.hw.testObjects.PredefinedAuthors;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.testObjects.PredefinedAuthors.PUSHKIN_AUTHOR;

@DisplayName("JPA-based repository for working with authors")
@DataJpaTest
class JpaAuthorRepositoryTest {

    private Map<String, Author> dbAuthors;

    private final Author first = Author.builder()
        .firstName("Agatha")
        .middleName(null)
        .lastName("Christie")
        .description("Mystery and detective fiction author")
        .build();
    private final Author second = Author.builder()
        .firstName("F. Scott")
        .middleName(null)
        .lastName("Fitzgerald")
        .description("American novelist")
        .build();

    @Autowired
    AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        dbAuthors = PredefinedAuthors.getAuthorMap();
    }

    @DisplayName("should save a new author")
    @Test
    void save_saveNewAuthor() {
        authorRepository.save(first);
        authorRepository.save(second);

        List<Author> all = authorRepository.findAll();

        assertThat(all)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .contains(first, second);

    }

    @DisplayName("should load the list of all authors")
    @Test
    void findAll_returnCorrectNotEmptyGenresList_genresExist() {
        var actualAuthors = authorRepository.findAll();
        var expectedAuthors = dbAuthors;

        assertThat(actualAuthors).containsExactlyInAnyOrderElementsOf(expectedAuthors.values());
        actualAuthors.forEach(System.out::println);
    }

    @DisplayName("should return an author by id")
    @Test
    void findById_correctAuthor_authorExist() {
        var actualAuthor = authorRepository.findById(2L);
        var expectedAuthor = PUSHKIN_AUTHOR;

        assertThat(actualAuthor).isPresent()
            .get()
            .isEqualTo(expectedAuthor);
    }

    @DisplayName("should return empty optional when the author with the specified id does not exist")
    @Test
    void findById_emptyOptional_authorNotExist() {
        var result = authorRepository.findById(100L);

        assertThat(result).isEmpty();
    }

}
