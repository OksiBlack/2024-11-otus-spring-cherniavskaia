package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.GenreConverter;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Service for working with genres")
@DataJpaTest
@Import({GenreConverter.class, GenreServiceImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional(propagation = Propagation.NEVER)
class GenreServiceImplTest {

    private static final int EXPECTED_NUMBER_OF_GENRES = 6;

    @Autowired
    private GenreService service;

    @DisplayName("should load the list of all genres")
    @Test
    void findAll() {
        var listGenreDto = service.findAll();
        assertThat(listGenreDto).isNotNull().hasSize(EXPECTED_NUMBER_OF_GENRES)
            .allMatch(g -> !g.getName().isEmpty());
    }
}
