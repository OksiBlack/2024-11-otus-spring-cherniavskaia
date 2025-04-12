package ru.otus.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
import ru.otus.hw.model.Book;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.testObjects.PredefinedBooks;

@SpringBootTest
@Transactional
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class AclBasedBookServiceImplTest {

    @ServiceConnection
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = TestContainerRegistry.getPostgres();

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AclBasedBookServiceImpl aclBasedBookService;

    @Test
    @WithMockUser(roles = "READER")
    public void testFindAllWithReadPermission() {

        // Execute findAll
        var books = aclBasedBookService.findAll();

        // Assert books are returned correctly
        assertThat(books).isNotEmpty();
        assertThat(books).hasSize(7);
    }

    @Test
    @WithMockUser(roles = "READER")
    public void testFindByIdWithReadPermission() {

        // Execute findById
        Book book = aclBasedBookService.findById(PredefinedBooks.LORD_TWO_TOWERS.getId());

        // Assert that the correct book is returned
        assertThat(book).isEqualTo(PredefinedBooks.LORD_TWO_TOWERS);
    }

    @Test
    @WithMockUser(roles = "READER")
    public void testDeleteByIdWithoutDeletePermission() {

        // Assert that attempting to delete raises access denied exception
        assertThatExceptionOfType(AccessDeniedException.class)
            .isThrownBy(() -> aclBasedBookService.deleteById(PredefinedBooks.LORD_FELLOWSHIP.getId()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSaveWithAdminRole() {
        // Build and save a new book using the builder pattern
        Book book = Book.builder()
            .title("New Book")
            .description("New Description")
            .serialNumber("New Serial")
            .isbn("New ISBN")
            .build();

        // Execute save
        Book savedBook = aclBasedBookService.save(book);

        // Assert that the book was saved correctly
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("New Book");
    }

    @Test
    @WithMockUser(roles = "READER")
    public void testSaveWithoutAdminRole() {
        assertThatExceptionOfType(AccessDeniedException.class)
            .isThrownBy(() -> aclBasedBookService.save(new Book()));
    }
}