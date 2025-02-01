package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.hw.models.Author;
import ru.otus.hw.testObjects.GeneratorData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA-based repository for working with authors")
@DataJpaTest
class JpaAuthorRepositoryTest {

    @Autowired
    AuthorRepository authorRepository;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = GeneratorData.getDbAuthors();
    }

    @DisplayName("should load the list of all authors")
    @Test
    void findAll_returnCorrectNotEmptyGenresList_genresExist() {
        var actualAuthors = authorRepository.findAll();
        var expectedAuthors = dbAuthors;

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    @DisplayName("should return an author by id")
    @Test
    void findById_correctAuthor_authorExist() {
        var actualAuthor = authorRepository.findById(2L);
        var expectedAuthor = dbAuthors.get(1);

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
