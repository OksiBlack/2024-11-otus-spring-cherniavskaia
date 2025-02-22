package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.CommentDto;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Service for working with comments")
@DataJpaTest
@Import({CommentConverter.class, CommentServiceImpl.class,
    BookConverter.class, AuthorConverter.class,
    GenreConverter.class, BookServiceImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional(propagation = Propagation.NEVER)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentServiceImplTest {

    private static final int EXPECTED_NUMBER_OF_COMMENTS = 3;
    private static final long NEW_COMMENT_ID = 5L;
    private static final long COMMENT_ID = 2L;
    private static final String COMMENT_TEXT = "Comment_2_2";
    private static final LocalDate COMMENT_DATE = LocalDate.of(2024, 1, 2);
    private static final long BOOK_ID = 1L;
    private static final String INSERT_CONTENT_VALUE = "Content_7";
    private static final String UPDATE_CONTENT_VALUE = "Content_8";
    public static final String COMMENT_AUTHOR_NAME = "Comment author_2";
    public static final String COMMENT_AUTHOR_NAME_INITIAL = "Comment author name";
    public static final String COMMENT_AUTHOR_NAME_UPDATED = "Comment author name after update";

    @Autowired
    private CommentService service;

    @DisplayName("should load information about the required comment by its id with full information")
    @Test
    @Order(1)
    void findById() {
        CommentDto expectedComment = CommentDto.builder().id(COMMENT_ID)
            .text(COMMENT_TEXT)
            .created(COMMENT_DATE)
            .author(COMMENT_AUTHOR_NAME)
            .build();

        var optionalActualCommentDto = service.findById(COMMENT_ID);
        assertThat(optionalActualCommentDto).isPresent();
        assertThat(optionalActualCommentDto.get())
            .usingRecursiveComparison()
            .isEqualTo(expectedComment);

    }

    @DisplayName("should load information about the required comments by book id with full information")
    @Test
    @Order(2)
    void findAllByBookId() {
        var listBookDto = service.findAllByBookId(BOOK_ID);
        assertThat(listBookDto).isNotNull().hasSize(EXPECTED_NUMBER_OF_COMMENTS)
            .allMatch(b -> !b.getText().isEmpty());
    }

    @DisplayName("should create a comment with full information")
    @Test
    @Order(3)
    void insert() {
        var commentDto = service.create(INSERT_CONTENT_VALUE, BOOK_ID, COMMENT_AUTHOR_NAME_INITIAL);
        var optionalExpectedCommentDto = service.findById(commentDto.getId());

        assertThat(optionalExpectedCommentDto).isPresent();

        assertThat(commentDto).isEqualTo(optionalExpectedCommentDto.get());
        assertThat(commentDto).isNotNull();
        assertThat(commentDto.getText()).isEqualTo(INSERT_CONTENT_VALUE);
        assertThat(commentDto).usingRecursiveComparison().isEqualTo(optionalExpectedCommentDto.get());
    }

    @DisplayName("should update a comment with full information")
    @Test
    @Order(4)
    void update() {
        var commentDto = service.update(NEW_COMMENT_ID, UPDATE_CONTENT_VALUE, COMMENT_AUTHOR_NAME_UPDATED);
        var optionalExpectedCommentDto = service.findById(commentDto.getId());

        assertThat(optionalExpectedCommentDto).isPresent();

        assertThat(commentDto).usingRecursiveComparison().isEqualTo(optionalExpectedCommentDto.get());
        assertThat(commentDto).isNotNull();
    }

    @DisplayName("should delete a comment by its id (created in the test)")
    @Test
    @Order(5)
    void deleteById() {
        service.deleteById(NEW_COMMENT_ID);
        var optionalActualCommentDto = service.findById(NEW_COMMENT_ID);
        assertThat(optionalActualCommentDto).isEmpty();
    }
}