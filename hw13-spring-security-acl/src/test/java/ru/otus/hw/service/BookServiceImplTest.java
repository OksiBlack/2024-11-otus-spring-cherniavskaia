package ru.otus.hw.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSearchFilter;
import ru.otus.hw.dto.request.SaveBookRequest;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.testObjects.PredefinedBooks;

class BookServiceImplTest {

    @Mock
    private AclBasedAuthorService aclBasedAuthorService;

    @Mock
    private AclBasedGenreService aclBasedGenreService;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private AclBasedBookService aclBasedBookService;

    @InjectMocks
    private BookServiceImpl bookService;

    private final Book book = PredefinedBooks.LORD_FELLOWSHIP;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        when(aclBasedBookService.findById(1L)).thenReturn(book);
        when(bookMapper.mapToDto(book)).thenReturn(PredefinedBooks.Dtos.LORD_FELLOWSHIP);

        BookDto result = bookService.findById(1L);

        verify(aclBasedBookService).findById(1L);
        assertEquals(2L, result.getId());
    }

    @Test
    void testFindAll() {
        when(aclBasedBookService.findAll()).thenReturn(List.of(book));
        when(bookMapper.mapToDto(book)).thenReturn(PredefinedBooks.Dtos.LORD_FELLOWSHIP);

        List<BookDto> result = bookService.findAll();

        verify(aclBasedBookService).findAll();
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getId());
    }

    @Test
    void testFindAllWithKeyword() {
        when(aclBasedBookService.findAll("keyword")).thenReturn(List.of(book));
        when(bookMapper.mapToDto(book)).thenReturn(PredefinedBooks.Dtos.LORD_FELLOWSHIP);

        List<BookDto> result = bookService.findAll("keyword");

        verify(aclBasedBookService).findAll("keyword");
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getId());
    }

    @Test
    void testFindAllWithFilter() {
        BookSearchFilter filter = new BookSearchFilter();
        when(aclBasedBookService.findAll(any(BookSearchFilter.class))).thenReturn(List.of(book));
        when(bookMapper.mapToDto(book)).thenReturn(PredefinedBooks.Dtos.LORD_FELLOWSHIP);

        List<BookDto> result = bookService.findAll(filter);

        verify(aclBasedBookService).findAll(filter);
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getId());
    }

    @Test
    void testDeleteById() {
        bookService.deleteById(1L);

        verify(aclBasedBookService).deleteById(1L);
    }

    @Test
    void testExistsById() {
        when(aclBasedBookService.existsById(1L)).thenReturn(true);

        boolean exists = bookService.existsById(1L);

        verify(aclBasedBookService).existsById(1L);
        assertTrue(exists);
    }

    @Test
    void testSaveNewBook() {
        SaveBookRequest request = new SaveBookRequest();
        request.setTitle("New Book");
        request.setDescription("New Book Description");
        request.setSerialNumber("SN002");
        request.setIsbn("67890");
        request.setAuthorId(1L);
        request.setGenreIds(new HashSet<>(Arrays.asList(1L, 2L)));

        Author author = new Author();
        author.setId(1L);

        Genre genre1 = new Genre();
        genre1.setId(1L);
        Genre genre2 = new Genre();
        genre2.setId(2L);

        when(aclBasedAuthorService.findById(1L)).thenReturn(author);
        when(aclBasedBookService.save(any(Book.class))).thenReturn(book);
        when(aclBasedGenreService.findAllByIds(any())).thenReturn(new HashSet<>(Arrays.asList(genre1, genre2)));
        when(bookMapper.mapToDto(any(Book.class))).thenReturn(PredefinedBooks.Dtos.LORD_FELLOWSHIP);

        BookDto savedBookDto = bookService.save(request);

        verify(aclBasedBookService).save(any(Book.class));
        verify(bookMapper).mapToDto(any(Book.class));

    }

    @Test
    void testSaveExistingBook() {
        SaveBookRequest request = new SaveBookRequest();
        request.setId(1L);
        request.setTitle("Updated Book");
        request.setDescription("Updated Description");
        request.setSerialNumber("SN001");
        request.setIsbn("12345");
        request.setAuthorId(1L);
        request.setGenreIds(new HashSet<>(List.of(1L)));

        Author author = new Author();
        author.setId(1L);
        Genre genre = new Genre();
        genre.setId(1L);

        when(aclBasedBookService.findById(1L)).thenReturn(book);
        when(aclBasedAuthorService.findById(1L)).thenReturn(author);
        when(aclBasedBookService.save(any(Book.class))).thenReturn(book);
        when(aclBasedGenreService.findAllByIds(any())).thenReturn(new HashSet<>(List.of(genre)));
        when(bookMapper.mapToDto(any(Book.class))).thenReturn(PredefinedBooks.Dtos.LORD_FELLOWSHIP);

        BookDto savedBookDto = bookService.save(request);

        verify(aclBasedBookService).save(book);  // Save should save the existing book
        assertEquals(2L, savedBookDto.getId());
    }
}
