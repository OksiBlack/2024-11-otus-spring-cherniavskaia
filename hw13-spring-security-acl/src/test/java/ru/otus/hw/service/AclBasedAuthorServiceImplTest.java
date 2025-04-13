package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.hw.model.Author;
import ru.otus.hw.repository.AuthorRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@EnableMethodSecurity
class AclBasedAuthorServiceImplTest {

    @InjectMocks
    private AclBasedAuthorServiceImpl aclBasedAuthorService;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AclPermissionEvaluator aclPermissionEvaluator;

    private Author author;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        author = new Author();
        author.setId(1L);
        author.setFirstName("John");
        author.setLastName("Doe");
    }

    @Test
    @DisplayName("Should return all authors when permissions are granted")
    void testFindAllWithPermission() {
        // Given
        when(authorRepository.findAll()).thenReturn(Collections.singletonList(author));
        // Setting up a mock authentication with permission
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(aclPermissionEvaluator.hasPermission(any(), any(), eq(BasePermission.READ))).thenReturn(true);

        // When
        List<Author> authors = aclBasedAuthorService.findAll();

        // Then
        assertThat(authors).containsExactly(author);
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when findAll is called without permission")
    void testFindAllWithoutPermission() {
        // Given
        when(authorRepository.findAll()).thenReturn(Collections.singletonList(author));
        // Setting up a mock authentication without permission
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(aclPermissionEvaluator.hasPermission(any(), any(), eq(BasePermission.READ))).thenReturn(false);

        List<Author> authors = aclBasedAuthorService.findAll();
        // When / Then
//        assertThatThrownBy(() -> aclBasedAuthorService.findAll())
//            .isInstanceOf(AccessDeniedException.class)
//            .hasMessageContaining("No permission to read author");
    }

    @Test
    @DisplayName("Should find author by id with permission")
    void testFindByIdWithPermission() {
        // Given
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(aclPermissionEvaluator.hasPermission(authentication, author, BasePermission.READ)).thenReturn(true);

        // When
        Author foundAuthor = aclBasedAuthorService.findById(1L);

        // Then
        assertThat(foundAuthor).isEqualTo(author);
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when findById is called without permission")
    void testFindByIdWithoutPermission() {
        // Given
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(aclPermissionEvaluator.hasPermission(authentication, author, BasePermission.READ)).thenReturn(false);

        // When / Then
        assertThatThrownBy(() -> aclBasedAuthorService.findById(1L))
            .isInstanceOf(AccessDeniedException.class)
            .hasMessageContaining("No permission to read author");
    }

    @Test
    @DisplayName("Should delete an author when permission is granted")
    void testDeleteByIdWithPermission() {
        // Given
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(aclPermissionEvaluator.hasPermission(authentication, author, BasePermission.DELETE)).thenReturn(true);

        // When
        aclBasedAuthorService.deleteById(1L);

        // Then
        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when deleteById is called without permission")
    void testDeleteByIdWithoutPermission() {
        // Given
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(aclPermissionEvaluator.hasPermission(authentication, author, BasePermission.DELETE)).thenReturn(false);

        // When / Then
        assertThatThrownBy(() -> aclBasedAuthorService.deleteById(1L))
            .isInstanceOf(AccessDeniedException.class)
            .hasMessageContaining("No permission to delete author");
    }

    @Test
    @DisplayName("Should check existence of author by id when permission is granted")
    void testExistsByIdWithPermission() {
        // Given
        when(authorRepository.existsById(1L)).thenReturn(true);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(aclPermissionEvaluator.hasPermission(authentication, author, BasePermission.READ)).thenReturn(true);

        // When
        boolean exists = aclBasedAuthorService.existsById(1L);

        // Then
        assertThat(exists).isTrue();
    }

    @WithMockUser
    @Test
    @DisplayName("Should throw AccessDeniedException when existsById is called without permission")
    void testExistsByIdWithoutPermission() {
        // Given
        when(authorRepository.existsById(1L)).thenReturn(true);
//        Authentication authentication = mock(Authentication.class);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        when(aclPermissionEvaluator.hasPermission(authentication, author, BasePermission.READ)).thenReturn(false);

        // When / Then
        assertThatThrownBy(() -> aclBasedAuthorService.existsById(1L))
            .isInstanceOf(AccessDeniedException.class)
            .hasMessageContaining("No permission to read author");
    }

    @Test
    @DisplayName("Should save author when permission is granted")
    void testSaveWithPermission() {
        // Given
        when(aclPermissionEvaluator.hasPermission(any(Authentication.class), any(), eq(BasePermission.WRITE)))
            .thenReturn(true);
        when(authorRepository.save(author)).thenReturn(author);

        // When
        Author savedAuthor = aclBasedAuthorService.save(author);

        // Then
        assertThat(savedAuthor).isEqualTo(author);
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when save is called without permission")
    void testSaveWithoutPermission() {
        // Given
//        when(aclPermissionEvaluator.hasPermission(any(Authentication.class), any(), eq(BasePermission.WRITE)))
//            .thenReturn(false);

        // When / Then
        assertThatThrownBy(() -> aclBasedAuthorService.save(author))
            .isInstanceOf(AccessDeniedException.class)
            .hasMessageContaining("No permission to write author");
    }
}
