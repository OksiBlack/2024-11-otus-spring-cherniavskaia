package ru.otus.hw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.otus.hw.config.AclSecurityConfig;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.service.AclBasedAuthorServiceImpl;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@EnableMethodSecurity
@Import(AclSecurityConfig.class)
class AclBasedAuthorServiceImplTest {


    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AclPermissionEvaluator aclPermissionEvaluator;


    private Author author1;
    private Author author2;


    @InjectMocks
    private AclBasedAuthorServiceImpl aclBasedAuthorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        author1 = new Author();
        author2 = new Author();
    }

    private void setUpSecurityContext(Authentication authentication) {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Should filter authors based on permissions")
    void testFindAllWithPostFilter() {
        // Given
        List<Author> authors = List.of(author1, author2);
        when(authorRepository.findAll()).thenReturn(authors);

        // Mocking the filtering based on permissions
        Authentication authentication = mock(Authentication.class);
        setUpSecurityContext(authentication);
        when(authentication.getName()).thenReturn("user");

        // Assume we have permission to read only author1
        when(aclPermissionEvaluator.hasPermission(authentication, author1, "READ")).thenReturn(true);
        when(aclPermissionEvaluator.hasPermission(authentication, author2, "READ")).thenReturn(false);

        // When
        List<Author> foundAuthors = aclBasedAuthorService.findAll();

        // Then
        assertThat(foundAuthors).containsExactly(author1); // should only contain author1
    }

    @Test
    @DisplayName("Should throw AccessDeniedException on empty list due to @PostFilter")
    void testPostFilterWithNoPermission() {
        // Given
        List<Author> authors = List.of(author1, author2);
        when(authorRepository.findAll()).thenReturn(authors);

        // Mock permission check to return false for both
        Authentication authentication = mock(Authentication.class);
        setUpSecurityContext(authentication);
        when(authentication.getName()).thenReturn("user");
        when(aclPermissionEvaluator.hasPermission(authentication, author1, "READ")).thenReturn(false);
        when(aclPermissionEvaluator.hasPermission(authentication, author2, "READ")).thenReturn(false);

        // When/Then: expect an empty list returned since the user has no permissions
        List<Author> foundAuthors = aclBasedAuthorService.findAll();
        assertThat(foundAuthors).isEmpty(); // should return empty list
    }

    @Test
    @DisplayName("Should filter authors by IDs based on permissions")
    void testFindAllByIdsWithPostFilter() {
        // Given
        List<Author> authors = List.of(author1, author2);
        when(authorRepository.findAllByIdIn(Set.of(1L, 2L))).thenReturn(authors);

        // Mocking the filtering based on permissions
        Authentication authentication = mock(Authentication.class);
        setUpSecurityContext(authentication);
        when(authentication.getName()).thenReturn("user");

        // Assume we have permission to read only author1
        when(aclPermissionEvaluator.hasPermission(authentication, author1, "READ")).thenReturn(true);
        when(aclPermissionEvaluator.hasPermission(authentication, author2, "READ")).thenReturn(false);

        // When
        List<Author> foundAuthors = aclBasedAuthorService.findAllByIds(Set.of(1L, 2L));

        // Then
        assertThat(foundAuthors).containsExactly(author1); // should only contain author1
    }

    @Test
    @DisplayName("Should return an empty list when no IDs have permission")
    void testFindAllByIdsWithNoPermissions() {
        // Given
        when(authorRepository.findAllByIdIn(Set.of(1L, 2L))).thenReturn(List.of(author1, author2));

        // Mock permission check to return false
        Authentication authentication = mock(Authentication.class);
        setUpSecurityContext(authentication);
        when(authentication.getName()).thenReturn("user");
        when(aclPermissionEvaluator.hasPermission(authentication, author1, "READ")).thenReturn(false);
        when(aclPermissionEvaluator.hasPermission(authentication, author2, "READ")).thenReturn(false);

        // When
        List<Author> foundAuthors = aclBasedAuthorService.findAllByIds(Set.of(1L, 2L));

        // Then
        assertThat(foundAuthors).isEmpty(); // should return empty list
    }
}
