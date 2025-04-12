package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.otus.hw.config.TestContainersConfig;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repository.CommentRepository;
import ru.otus.hw.security.keycloak.KeycloakUserService;
import ru.otus.hw.testObjects.PredefinedBooks;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Service for working with comments")
@DataJpaTest
@Import({TestContainersConfig.class,
    CommentMapper.class, BookMapper.class, AuthorMapper.class, GenreMapper.class, BookServiceImpl.class,
    CommentServiceImpl.class, AclBasedAuthorServiceImpl.class, AclBasedGenreServiceImpl.class, AclBasedBookServiceImpl.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Testcontainers
@Transactional(propagation = Propagation.NEVER)
class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private CommentRepository commentRepository;

    @MockitoBean
    private AclPermissionEvaluator aclPermissionEvaluator;

    @Autowired
    private AclBasedGenreService aclBasedGenreService;

    @Autowired
    private AclBasedAuthorService aclBasedAuthorService;

    @MockitoBean
    private KeycloakUserService keycloakUserService;

    private Comment comment;

    @BeforeEach
    void setUp() {
        // Initialize and persist a Comment object using Lombok Builder
        comment = Comment.builder()
            .book(PredefinedBooks.HOBBIT)
            .text("Great book!")
            .build();
        commentRepository.save(comment);  // Save the comment to the in-memory database
    }

    @Test
    void testFindById() {
        CommentDto foundComment = commentService.findById(comment.getId());

        assertThat(foundComment).isNotNull();
        assertThat(foundComment.getText()).isEqualTo(comment.getText());
    }

    @Test
    void testFindById_NotFound() {

        assertThatThrownBy(() -> {
            commentService.findById(999L);
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void testFindAllByBookId() {
        List<CommentDto> comments = commentService.findAllByBookId(comment.getBook().getId());

        assertThat(comments).isNotNull();
        assertThat(comments).isNotEmpty();
        assertThat(comments).hasSize(6);
        assertThat(comments).usingRecursiveFieldByFieldElementComparator()
            .extracting("text")
            .contains("Great book!");
    }

    @Test
    void testSave() {
        CommentDto newCommentDto = CommentDto.builder()
            .author("Some author")
            .text("Awesome read!")
            .bookId(5L)
            .build();

        CommentDto savedCommentDto = commentService.save(newCommentDto);

        assertThat(savedCommentDto).isNotNull();
        assertThat(savedCommentDto.getId()).isNotNull();
        assertThat(savedCommentDto.getText()).isEqualTo("Awesome read!");

        Optional<Comment> foundComment = commentRepository.findById(savedCommentDto.getId());
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getText()).isEqualTo("Awesome read!");
    }

    @Test
    void testDeleteById() {
        Long idToDelete = comment.getId();
        commentService.deleteById(idToDelete);

        assertThat(commentRepository.findById(idToDelete)).isNotPresent();
    }

    @Test
    void testExistsById() {
        assertThat(commentService.existsById(comment.getId())).isTrue();
        assertThat(commentService.existsById(999L)).isFalse(); // Non-existent ID
    }
}
