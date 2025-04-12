package ru.otus.hw.service;

import java.util.HashSet;
import java.util.Set;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.otus.hw.config.TestContainerRegistry;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.GenreRepository;


@SpringBootTest
@Transactional
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class AclBasedGenreServiceImplSecurityTest {

    @ServiceConnection
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = TestContainerRegistry.getPostgres();

    @Autowired
    private AclBasedGenreService aclBasedGenreService;

    @Autowired
    private GenreRepository genreRepository;

    private Genre genre;

    @BeforeEach
    public void setUp() {
        genre = new Genre();
        genre.setId(1L);
        genre.setName("Test Genre");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldSaveGenre_WhenUserIsAdmin() {
        Genre savedGenre = aclBasedGenreService.save(genre);
        assertThat(genre.getId()).isEqualTo(savedGenre.getId());
        assertThat(genreRepository.existsById(genre.getId())).isTrue();
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    public void shouldSaveGenre_WhenUserIsEditor() {
        Genre savedGenre = aclBasedGenreService.save(genre);
        assertThat(genre.getId()).isEqualTo(savedGenre.getId());
        assertThat(genreRepository.existsById(genre.getId())).isTrue();
    }

    @Test
    @WithMockUser(roles = "READER")
    public void shouldNotSaveGenre_WhenUserIsReader() {
        Assertions.assertThatThrownBy(() -> {
            aclBasedGenreService.save(genre);
        }).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldDeleteGenre_WhenUserIsAdmin() {
        Assertions.assertThatNoException().isThrownBy(() -> {
            aclBasedGenreService.deleteById(1L);
        });
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    public void shouldNotDeleteGenre_WhenUserIsEditor() {
        Assertions.assertThatException().isThrownBy(() -> {
                aclBasedGenreService.deleteById(1L);
            })
            .isInstanceOf(AuthorizationDeniedException.class);
    }

    @Test
    @WithMockUser(roles = "READER")
    public void shouldNotDeleteGenre_WhenUserIsReader() {
        Assertions.assertThatException().isThrownBy(() -> {
            aclBasedGenreService.deleteById(1L);
        })
            .isInstanceOf(AuthorizationDeniedException.class);
    }

    @Test
    @WithMockUser(roles = "READER")
    public void shouldFindById_WhenUserIsReader() {
        Assertions.assertThatNoException().isThrownBy(() -> {
            aclBasedGenreService.findById(1L);
        });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldFindAllByIds_WhenUserIsAdmin() {
        Assertions.assertThatNoException().isThrownBy(() -> {
            aclBasedGenreService.findAllByIds(new HashSet<>(Set.of(1L, 2L)));
        });

    }

    @Test
    @WithMockUser(roles = "READER")
    public void shouldFindAll_WhenUserIsReader() {
        Assertions.assertThatNoException().isThrownBy(() -> {
            aclBasedGenreService.findAll();
        });
    }

    @Test
    @WithMockUser(roles = "READER")
    public void shouldReturnExistsById_WhenUserIsReader() {
        Assertions.assertThatNoException().isThrownBy(() -> {
            aclBasedGenreService.existsById(1L);
        });
    }
}
