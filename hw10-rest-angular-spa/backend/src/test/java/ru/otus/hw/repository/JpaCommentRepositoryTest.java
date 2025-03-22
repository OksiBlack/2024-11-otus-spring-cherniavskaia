package ru.otus.hw.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;
import ru.otus.hw.testObjects.PredefinedBooks;
import ru.otus.hw.testObjects.TestData;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("JPA-based repository for working with book comments")
@DataJpaTest
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
class JpaCommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    private List<Book> dbBooks;

    private Map<Long, List<Comment>> dbCommentsByBookId;

    @BeforeEach
    void setUp() {
        dbBooks = TestData.getDbBooks();
        dbCommentsByBookId = TestData.getDbMapCommentsByIdBook();
    }

    @DisplayName("should return an empty list of comments for the given book ID")
    @Test
    void findAllByBookId_emptyCommentsList_commentsNotExist() {
        var actualComments = commentRepository.findAllByBookId(PredefinedBooks.FATALIST.getId());

        assertThat(actualComments).isEmpty();
    }

    @DisplayName("should return a non-empty list of comments for the given book ID")
    @Test
    void findAllByBookId_correctNotEmptyCommentsList_commentsExist() {
        var actualComments = commentRepository.findAllByBookId(PredefinedBooks.HOBBIT.getId());
        var expectedComments = dbCommentsByBookId.get(PredefinedBooks.HOBBIT.getId());

        assertAll(
            () -> assertThat(actualComments).isNotEmpty(),
            () -> assertThat(actualComments.size()).isEqualTo(expectedComments.size()),
            () -> assertThat(actualComments).containsExactlyElementsOf(expectedComments)
        );
    }

    @DisplayName("should return an empty optional when searching by ID")
    @Test
    void findById_emptyOptional_commentExist() {
        var actualOptionalComment = commentRepository.findById(100L);
        assertThat(actualOptionalComment).isEmpty();
    }

    @DisplayName("should return a non-empty optional when searching by ID")
    @Test
    void findById_notEmptyOptional_commentExist() {
        var expectedComment = dbCommentsByBookId.get(1L).get(0);
        var actualOptionalComment = commentRepository.findById(expectedComment.getId());

        assertThat(actualOptionalComment).isNotEmpty();
        assertThat(actualOptionalComment.get()).isEqualTo(expectedComment);
    }

    @DisplayName("should save a new comment")
    @Test
    void save_saveNewComment_requestIsCorrect() {
        var expectedComment = new Comment(null, dbBooks.get(0), "Comment to book_1",
            LocalDate.now(),
            "zzzzzz");

        var returnedComment = commentRepository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
            .matches(comment -> comment.getId() > 0)
            .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(commentRepository.findById(returnedComment.getId()))
            .isPresent()
            .get()
            .isEqualTo(returnedComment);
    }

    @DisplayName("should save an updated comment")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void save_saveUpdatedComment_commentExists() {
        var book = dbBooks.get(0);
        var expectedComment = new Comment(1L, book, "Changed Comment to book_1", LocalDate.now(), "Author 1");

        assertThat(em.find(Comment.class, expectedComment.getId()))
            .isNotNull();

        var returnedBook = commentRepository.save(expectedComment);

        assertThat(returnedBook).isNotNull()
            .matches(comment -> comment.getId() > 0)
            .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        Comment returnedComment = em.find(Comment.class, expectedComment.getId());
        assertThat(returnedComment)
            .isEqualTo(expectedComment);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteById() {
        var expectedComment = dbCommentsByBookId.get(1L).get(0);
        var actualComment = em.find(Comment.class, expectedComment.getId());

        assertThat(actualComment).isNotNull();

        commentRepository.deleteById(expectedComment.getId());

        actualComment = em.find(Comment.class, expectedComment.getId());
        assertThat(actualComment).isNull();
    }
}
