package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.testObjects.GeneratorData;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("JPA-based repository for working with books")
@DataJpaTest
class JpaBookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = GeneratorData.getDbAuthors();
        dbGenres = GeneratorData.getDbGenres();
        dbBooks = GeneratorData.getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("should load a book by id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void findById_correctBookById_bookExists(Book expectedBook) {
        var actualBook = bookRepository.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
            .get()
            .isEqualTo(expectedBook);
    }

    @DisplayName("should return an empty Optional")
    @Test
    void findById_emptyOptional_whenBookNotFound() {
        Optional<Book> result = bookRepository.findById(100L);
        assertThat(result).isEmpty();
    }

    @DisplayName("should load a list of all books")
    @Test
    void findAll_correctNotEmptyBooksList_booksExist() {
        var actualBooks = bookRepository.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("should save a new book")
    @Test
    void save_saveNewBook_requestIsCorrect() {
        var expectedBook = new Book(null, "BookTitle_10500", dbAuthors.get(0),
            List.of(dbGenres.get(0), dbGenres.get(2)));

        var returnedBook = bookRepository.save(expectedBook);

        assertThat(returnedBook).isNotNull()
            .matches(book -> book.getId() > 0)
            .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        Book book = em.find(Book.class, returnedBook.getId());
        assertTrue(Objects.nonNull(book));
        assertThat(book).isEqualTo(returnedBook);
    }

    @DisplayName("should save an updated book")
    @Test
    void save_saveUpdatedBook_bookExists() {
        Book initial = bookRepository.findById(1L).get();

        var expectedBook = new Book(1L, "BookTitle_10500", dbAuthors.get(2),
            List.of(dbGenres.get(4), dbGenres.get(5)));

        assertThat(bookRepository.findById(initial.getId()))
            .isPresent()
            .get()
            .usingRecursiveComparison().isNotEqualTo(expectedBook);

        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
            .matches(book -> book.getId() > 0)
            .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
            .isPresent()
            .get()
            .isEqualTo(returnedBook);
    }

    @DisplayName("should delete a book by id")
    @Test
    void deleteById_deleteBook_bookExists() {
        assertThat(bookRepository.findById(1L)).isPresent();
        bookRepository.deleteById(1L);
        assertThat(bookRepository.findById(1L)).isEmpty();
    }

    private static List<Book> getDbBooks() {
        return GeneratorData.getDbBooks();
    }
}