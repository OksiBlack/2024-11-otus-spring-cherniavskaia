package ru.otus.hw.service;

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
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.request.SaveBookRequest;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Book;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.testObjects.PredefinedAuthors;
import ru.otus.hw.testObjects.PredefinedBooks;
import ru.otus.hw.testObjects.PredefinedGenres;
import ru.otus.hw.testObjects.TestData;

import java.util.Set;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.testObjects.PredefinedBooks.getIdToBookDtoMapping;

@DisplayName("Service for working with books")
@DataJpaTest
@Import({BookMapper.class, AuthorMapper.class, GenreMapper.class, BookServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookServiceImplTest {

    private static final int EXPECTED_NUMBER_OF_BOOK = TestData.getDbBooks().size();

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService service;

    @DisplayName("should load a list of all books with complete information about them")
    @Test
    @Order(1)
    void findAll() {
        var listBookDto = service.findAll();
        assertThat(listBookDto).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOK);
        assertThat(listBookDto).contains(PredefinedBooks.Dtos.HOBBIT);
        Map<Long, BookDto> idToBookDtoMapping = getIdToBookDtoMapping();

        listBookDto.forEach(book -> {
            BookDto expectedDto = idToBookDtoMapping.get(book.getId());
            assertThat(book).isEqualTo(expectedDto);
        });

        assertThat(listBookDto)
            .usingRecursiveFieldByFieldElementComparator()
            .containsAll(PredefinedBooks.getBookDtoMap().values());
    }

    @DisplayName("should load information about the required book by its ID with complete information")
    @Test
    @Order(2)
    void findById() {
        var optionalActualBookDto = service.findById(PredefinedBooks.BROTHERS_KARAMAZOV.getId());

        assertThat(optionalActualBookDto).isPresent();
        assertThat(optionalActualBookDto.get()).usingRecursiveComparison()
            .isEqualTo(PredefinedBooks.Dtos.BROTHERS_KARAMAZOV);
    }

    @Order(3)
    @Test
    void testFindById_NotFound() {
        Optional<BookDto> foundBook = service.findById(999L); // Non-existent ID

        assertThat(foundBook).isNotPresent();
    }

    @DisplayName("should create a book with complete information")
    @Test
    @Order(4)
    void testSave() {
        SaveBookRequest saveBookRequest = SaveBookRequest.builder()
            .title("Another Test Book")
            .description("This is another test book description.")
            .isbn("ISBN-789012")
            .serialNumber("SN-003")
            .authorId(PredefinedAuthors.LERMONTOV_AUTHOR.getId())
            .genreIds(Set.of(PredefinedGenres.FANTASY_GENRE.getId()))
            .build();

        BookDto createdBookDto = service.save(saveBookRequest);

        assertThat(createdBookDto).isNotNull();
        assertThat(createdBookDto.getTitle()).isEqualTo("Another Test Book");
        assertThat(createdBookDto.getId()).isNotNull();

        Optional<Book> createdBook = bookRepository.findById(createdBookDto.getId());
        assertThat(createdBook).isPresent();
        assertThat(createdBook.get().getTitle()).isEqualTo("Another Test Book");
    }

    @Order(5)
    @Test
    void testExistsById() {
        assertThat(service.existsById(PredefinedBooks.BROTHERS_KARAMAZOV.getId())).isTrue();
        assertThat(service.existsById(999L)).isFalse(); // Non-existent ID
    }

    @Order(6)
    @Test
    void testDeleteById() {
        Long idToDelete = PredefinedBooks.HOBBIT.getId();
        service.deleteById(idToDelete);

        assertThat(bookRepository.findById(idToDelete)).isNotPresent();
    }
}
