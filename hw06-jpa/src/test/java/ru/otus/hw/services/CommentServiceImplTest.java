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
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Service for working with comments")
@DataJpaTest
@Import({CommentConverter.class, CommentServiceImpl.class,
    JpaCommentRepository.class, BookConverter.class,
    AuthorConverter.class, GenreConverter.class,
    JpaBookRepository.class, JpaAuthorRepository.class,
    JpaGenreRepository.class, BookServiceImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional(propagation = Propagation.NEVER)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentServiceImplTest {

    private static final int EXPECTED_NUMBER_OF_COMMENTS = 3;
    private static final long NEW_COMMENT_ID = 5L;
    private static final long FIRST_COMMENT_ID = 2L;
    private static final long BOOK_ID = 1L;
    private static final String INSERT_CONTENT_VALUE = "Content_7";
    private static final String UPDATE_CONTENT_VALUE = "Content_8";

    @Autowired
    private CommentService service;

    @DisplayName("should load information about the required comment by its id with full information")
    @Test
    @Order(1)
    void findById() {
        var optionalActualCommentDto = service.findById(FIRST_COMMENT_ID);
        assertThat(optionalActualCommentDto).isPresent();
        assertThat(optionalActualCommentDto.get().getId()).isEqualTo(FIRST_COMMENT_ID);
        assertThat(optionalActualCommentDto.get().getBook()).isNotNull();
        assertThat(optionalActualCommentDto.get().getBook().getAuthor()).isNotNull();
        assertThat(optionalActualCommentDto.get().getBook().getGenres()).isNotNull();
        assertThat(optionalActualCommentDto.get().getBook().getGenres().size()).isEqualTo(2);
    }

    @DisplayName("should load information about the required comments by book id with full information")
    @Test
    @Order(2)
    void findAllByBookId() {
        var listBookDto = service.findAllByBookId(BOOK_ID);
        assertThat(listBookDto).isNotNull().hasSize(EXPECTED_NUMBER_OF_COMMENTS)
            .allMatch(b -> !b.getText().isEmpty())
            .allMatch(b -> b.getBook() != null)
            .allMatch(b -> !b.getBook().getGenres().isEmpty()
                && b.getBook().getGenres().size() > 1);
    }

    @DisplayName("should create a comment with full information")
    @Test
    @Order(3)
    void insert() {
        var insertCommentDto = service.insert(INSERT_CONTENT_VALUE, BOOK_ID, "zzzz");
        var optionalExpectedCommentDto = service.findById(insertCommentDto.getId());

        assertThat(insertCommentDto).isEqualTo(optionalExpectedCommentDto.get());
        assertThat(insertCommentDto).isNotNull();
        assertThat(insertCommentDto.getText()).isEqualTo(INSERT_CONTENT_VALUE);
        assertThat(insertCommentDto.getBook())
            .isNotNull();
        assertThat(insertCommentDto.getBook().getGenres()).hasSize(2)
            .allMatch(g -> !g.getName().isEmpty());
    }

    @DisplayName("should update a comment with full information")
    @Test
    @Order(4)
    void update() {
        var insertCommentDto = service.update(NEW_COMMENT_ID, UPDATE_CONTENT_VALUE, 1L, "zzz");
        var optionalExpectedCommentDto = service.findById(insertCommentDto.getId());

        assertThat(insertCommentDto).isEqualTo(optionalExpectedCommentDto.get());
        assertThat(insertCommentDto).isNotNull();
        assertThat(insertCommentDto.getText()).isEqualTo(UPDATE_CONTENT_VALUE);
        assertThat(insertCommentDto.getBook())
            .isNotNull();
        assertThat(insertCommentDto.getBook().getGenres()).hasSize(2)
            .allMatch(g -> !g.getName().isEmpty());
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