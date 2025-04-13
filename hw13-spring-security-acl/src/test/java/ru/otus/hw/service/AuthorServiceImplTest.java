package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.config.AclSecurityConfig;
import ru.otus.hw.config.TestContainersConfig;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.testObjects.PredefinedAuthors;

import java.security.Permission;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Service for working with authors")
@DataJpaTest
@Import({TestContainersConfig.class,
    AuthorMapper.class, AuthorServiceImpl.class, AclBasedAuthorServiceImpl.class,
    AclSecurityConfig.class,
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional(propagation = Propagation.NEVER)
@WithMockUser(value = "admin", roles = {"ADMIN"})
class AuthorServiceImplTest {

    @Autowired
    private AclBasedAuthorService aclBasedAuthorService;

    @Autowired
    private AclPermissionEvaluator aclPermissionEvaluator;

    @Autowired
    private AuthorMapper authorMapper;

    public static final AuthorDto TOLKIEN = AuthorDto.builder()
        .id(1L) // using long number
        .firstName("John")
        .middleName("Ronald")
        .lastName("Tolkien")
        .description("English writer and academic, best known for The Hobbit and The Lord of the Rings.")
        .build();

    public static final AuthorDto PUSHKIN = AuthorDto.builder()
        .id(2L) // using long number
        .firstName("Alexander")
        .middleName(null)
        .lastName("Pushkin")
        .description("Russian poet, playwright, and novelist, considered to be the founder of modern Russian literature.")
        .build();

    @InjectMocks
    private AuthorServiceImpl authorService;

    @BeforeEach
    void setUp() {
        authorService = new AuthorServiceImpl(aclBasedAuthorService, authorMapper);
    }

    @Test
    void testSaveAndFindAll() {

        // Given
        AuthorDto authorDto = AuthorDto.builder()
            .firstName("J.K.")
            .middleName("Rowling")
            .lastName("Smith")
            .description("British author, best known for the Harry Potter series")
            .build();

        // When
        AuthorDto savedAuthor = authorService.save(authorDto);

        // Then
        List<AuthorDto> authors = authorService.findAll();
        assertThat(authors).isNotNull();

        assertThat(authors).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .contains(savedAuthor);
    }

    @WithMockUser(value = "admin", roles = {"ADMIN"})
    @Test
    void testFindById() {
        // Given
        // When

//        when(aclPermissionEvaluator.hasPermission(any(Authentication.class), any(), any(Permission.class))).thenReturn(true);
        AuthorDto found = authorService.findById(PredefinedAuthors.TOLKIEN_AUTHOR.getId());

        // Then
        assertThat(found)
            .usingRecursiveComparison().isEqualTo(TOLKIEN);
    }

    @WithMockUser(value = "frodo", roles = {"ADMIN"})
    @Test
    void testSaveAndFindById() {
        // Given
        Author savedAuthor = aclBasedAuthorService.save(Author.builder()
            .firstName("George")
            .middleName("R.")
            .lastName("Martin")
            .description("American novelist")
            .build());

        // When
        AuthorDto found = authorService.findById(savedAuthor.getId());

        // Then
        assertThat(found).extracting("firstName", "middleName", "lastName").containsExactly("George", "R.", "Martin");
    }

    @Test
    void testFindByIdNotFound() {
        // When
        // Then
        assertThatThrownBy(() -> authorService.findById(999L))
            .isInstanceOf(EntityNotFoundException.class);

    }

    @WithMockUser(value = "admin", roles = {"ADMIN"})
    @Test
    void testDeleteById() {
        // Given
        Author savedAuthor = aclBasedAuthorService.save(Author.builder()
            .firstName("Mark")
            .middleName(null)
            .lastName("Twain")
            .description("American writer")
            .build());
        Long authorId = savedAuthor.getId();

        // When
        authorService.deleteById(authorId);

        // Then
        assertThat(aclBasedAuthorService.existsById(authorId)).isFalse();
    }

    @WithMockUser(value = "admin", roles = {"ADMIN"})
    @Test
    void testExistsById() {
        // Given
        Author savedAuthor = aclBasedAuthorService.save(Author.builder()
            .firstName("Ernest")
            .middleName("H.")
            .lastName("Hemingway")
            .description("American novelist")
            .build());
        Long authorId = savedAuthor.getId();

        // When
        boolean exists = authorService.existsById(authorId);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void testFindAllByIds() {
        // Given

        // When
        List<AuthorDto> authors = authorService.findAllByIds(Set.of(1L, 2L));

        // Then
        assertThat(authors).isNotNull();

        assertThat(authors).usingRecursiveFieldByFieldElementComparator()
            .contains(TOLKIEN, PUSHKIN);

        assertThat(authors).hasSize(2);
    }

}
