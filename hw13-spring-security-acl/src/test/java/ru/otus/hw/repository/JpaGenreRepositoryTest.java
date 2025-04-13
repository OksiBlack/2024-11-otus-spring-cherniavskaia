package ru.otus.hw.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.config.TestContainersConfig;
import ru.otus.hw.model.Genre;
import ru.otus.hw.testObjects.TestData;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.testObjects.PredefinedGenres.ADVENTURE_GENRE;
import static ru.otus.hw.testObjects.PredefinedGenres.CLASSIC_LITERATURE_GENRE;
import static ru.otus.hw.testObjects.PredefinedGenres.FANTASY_GENRE;
import static ru.otus.hw.testObjects.PredefinedGenres.FICTION_GENRE;

@DisplayName("Tests for JPA genre repositories")
@DataJpaTest
@Import(value = {TestContainersConfig.class})
class JpaGenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = TestData.getDbGenres();
    }

    @DisplayName("should load all genres")
    @Test
    void findAll_correctNotEmptyGenresList_genresExist() {
        var actualGenres = genreRepository.findAll();
        var expectedGenres = dbGenres;

        assertThat(actualGenres).containsAll(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    @DisplayName("should load a non-empty list of genres by specified ids")
    @Test
    void findAllByIds_correctNotEmptyGenresList_genresExist() {
        var actualGenres = genreRepository.findAllByIdIn(Set.of(1L, 2L, 3L, 5L));
        var expectedGenres = List.of(ADVENTURE_GENRE, FANTASY_GENRE, FICTION_GENRE,
            CLASSIC_LITERATURE_GENRE);

        assertThat(actualGenres).containsAll(expectedGenres)
            .hasSize(4);
    }

    @DisplayName("should load an empty list of genres by specified ids")
    @Test
    void findAllByIds_emptyGenresList_genresNotExist() {
        var actualGenres = genreRepository.findAllByIdIn(Set.of(100L));

        assertThat(actualGenres).isEmpty();
    }
}
