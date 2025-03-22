package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Genre;
import ru.otus.hw.testObjects.PredefinedGenres;
import ru.otus.hw.testObjects.TestData;

import java.util.Comparator;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Service for working with genres")
@DataJpaTest
@Import({GenreMapper.class, GenreServiceImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional(propagation = Propagation.NEVER)
class GenreServiceImplTest {

    private static final int EXPECTED_NUMBER_OF_GENRES = 14;

    @Autowired
    private GenreService service;

    @DisplayName("should load the list of all genres")
    @Test
    void findAll() {
        var listGenreDto = service.findAll().stream().sorted(Comparator.comparing(GenreDto::getId)).collect(Collectors.toList());
        assertThat(listGenreDto).isNotNull().hasSize(EXPECTED_NUMBER_OF_GENRES)
            .allMatch(g -> !g.getName().isEmpty());

        assertThat(listGenreDto)
            .usingRecursiveComparison()
            .isEqualTo(TestData.getDbGenres().stream().sorted(Comparator.comparing(Genre::getId)).collect(Collectors.toList()));
    }
}
