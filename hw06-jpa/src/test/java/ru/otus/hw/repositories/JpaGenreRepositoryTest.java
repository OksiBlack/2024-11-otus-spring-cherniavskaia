package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.hw.models.Genre;
import ru.otus.hw.testObjects.GeneratorData;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for JPA genre repositories")
@DataJpaTest
@ActiveProfiles("test")
@Import({JpaGenreRepository.class})
class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository genreRepository;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = GeneratorData.getDbGenres();
    }

    @DisplayName("should load all genres")
    @Test
    void findAll_correctNotEmptyGenresList_genresExist() {
        var actualGenres = genreRepository.findAll();
        var expectedGenres = dbGenres;

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    @DisplayName("should load a non-empty list of genres by specified ids")
    @Test
    void findAllByIds_correctNotEmptyGenresList_genresExist() {
        var actualGenres = genreRepository.findAllByIds(Set.of(1L, 3L));
        var expectedGenres = List.of(dbGenres.get(0), dbGenres.get(2));

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        assertThat(actualGenres).hasSize(2);
    }

    @DisplayName("should load an empty list of genres by specified ids")
    @Test
    void findAllByIds_emptyGenresList_genresNotExist() {
        var actualGenres = genreRepository.findAllByIds(Set.of(100L));

        assertThat(actualGenres).isEmpty();
    }
}
