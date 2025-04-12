package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.otus.hw.config.TestContainerRegistry;
import ru.otus.hw.model.Author;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.testObjects.PredefinedAuthors;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class AclBasedAuthorServiceImplTest {

    @ServiceConnection
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = TestContainerRegistry.getPostgres();

    @Autowired
    private AuthorRepository authorRepository;


    @Autowired
    private AclBasedAuthorServiceImpl aclBasedAuthorService;

    @Test
    @WithMockUser(username = "user", roles = {"READER"})
    void testFindById_withReadPermission() {

        var foundAuthor = aclBasedAuthorService.findById(PredefinedAuthors.TOLKIEN_AUTHOR.getId());

        assertThat(foundAuthor).isEqualTo(PredefinedAuthors.TOLKIEN_AUTHOR);
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void testDeleteById_withDeletePermission() {
        // Using builder to create Author instance
        Author author = PredefinedAuthors.TOLKIEN_AUTHOR;

        aclBasedAuthorService.deleteById(author.getId());

        assertThat(authorRepository.findById(author.getId())).isNotPresent();
    }

    @Test
    @WithMockUser(username = "aragorn")
    void testFindAll_withReadPermission() {

        var authors = aclBasedAuthorService.findAll();

        assertThat(authors).hasSize(1);
        assertThat(authors.get(0)).usingRecursiveComparison()
            .isEqualTo(PredefinedAuthors.TOLKIEN_AUTHOR);
    }


    @Test
    @WithMockUser(username = "user", roles = {"READER"})
    void testFindAllByIds_withReadPermission() {

        Set<Long> ids = Set.of(PredefinedAuthors.TOLKIEN_AUTHOR.getId(),
            PredefinedAuthors.LERMONTOV_AUTHOR.getId());
        var authors = aclBasedAuthorService.findAllByIds(ids);

        assertThat(authors).containsExactly(PredefinedAuthors.TOLKIEN_AUTHOR,
            PredefinedAuthors.LERMONTOV_AUTHOR
        );
    }

    @Test
    @WithMockUser(username = "some", roles = "EDITOR")
    void testSave_withWritePermission() {
        // Using builder to create Author instance
        Author author = Author.builder()
            .firstName("John")
            .middleName("A.")
            .lastName("Doe")
            .description("Sample author description.")
            .build();

        Author savedAuthor = aclBasedAuthorService.save(author);

        assertThat(savedAuthor).isNotNull();
        assertThat(savedAuthor.getFirstName()).isEqualTo("John");
        assertThat(savedAuthor.getMiddleName()).isEqualTo("A.");
        assertThat(savedAuthor.getLastName()).isEqualTo("Doe");
        assertThat(savedAuthor.getDescription()).isEqualTo("Sample author description.");
    }

    @Test
    @WithMockUser(username = "frodo", authorities = {"ROLE_READER"})
    void testExistsById_withReadPermission() {

        boolean exists = aclBasedAuthorService.existsById(PredefinedAuthors.TOLKIEN_AUTHOR.getId());

        assertThat(exists).isTrue();
    }

    @Test
    @WithMockUser(username = "user", roles = {"READER"})
    void testDeleteById_withoutDeletePermission() {
        // Using builder to create Author instance
        Author author = PredefinedAuthors.TOLKIEN_AUTHOR;

        // Expecting an exception to be thrown when trying to delete without delete permission
        assertThatThrownBy(() -> aclBasedAuthorService.deleteById(author.getId()))
            .isInstanceOf(AccessDeniedException.class); 
    }

    @Test
    @WithMockUser(username = "user", roles = {"READER"})
    void testSave_withoutWritePermission() {
        // Attempt to save a new author without the necessary permission
        Author author = Author.builder()
            .firstName("Jane")
            .middleName("B.")
            .lastName("Doe")
            .description("Another sample author description.")
            .build();

        // Expecting an exception to be thrown
        assertThatThrownBy(() -> aclBasedAuthorService.save(author))
            .isInstanceOf(AccessDeniedException.class); 
    }

    @Test
    @WithMockUser(username = "user")
    void testFindAllByIds_withoutReadPermission() {
        // Testing findAllByIds without read permission
        Set<Long> ids = Set.of(PredefinedAuthors.TOLKIEN_AUTHOR.getId());

        // Expecting an exception to be thrown
        List<Author> allByIds = aclBasedAuthorService.findAllByIds(ids);
        assertThat(allByIds).isEmpty();
    }

    @Test
    @WithMockUser(username = "user")
    void testFindById_withoutReadPermission() {
        // Expecting an exception to be thrown when trying to find an author by ID without permission
        assertThatThrownBy(() -> aclBasedAuthorService.findById(PredefinedAuthors.LERMONTOV_AUTHOR.getId()))
            .isInstanceOf(AccessDeniedException.class); 
    }

    @Test
    @WithMockUser(username = "some")
    void testExistsById_withoutReadPermission() {
        // Testing existsById without read permission
        assertThatThrownBy(() -> aclBasedAuthorService.existsById(PredefinedAuthors.TOLKIEN_AUTHOR.getId()))
            .isInstanceOf(AccessDeniedException.class); 
    }

}
