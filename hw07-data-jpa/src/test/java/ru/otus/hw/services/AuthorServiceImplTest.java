package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Service for working with authors")
@DataJpaTest
@Import({AuthorConverter.class, AuthorServiceImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional(propagation = Propagation.NEVER)
class AuthorServiceImplTest {

    private static final int EXPECTED_NUMBER_OF_AUTHORS = 3;

    @Autowired
    private AuthorService service;

    @DisplayName("should load a list of all authors")
    @Test
    void findAll() {

        var listAuthorDto = service.findAll();

        assertThat(listAuthorDto).isNotNull().hasSize(EXPECTED_NUMBER_OF_AUTHORS)
            .allMatch(a -> !a.getFullName().isEmpty());
    }
}
