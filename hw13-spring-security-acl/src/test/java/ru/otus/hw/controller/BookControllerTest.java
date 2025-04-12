package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.request.SaveBookRequest;
import ru.otus.hw.mapper.BookToSaveRequestConverter;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.CommentService;
import ru.otus.hw.service.GenreService;
import ru.otus.hw.testObjects.PredefinedGenres;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WithMockUser(roles = "READER")
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private BookToSaveRequestConverter bookToSaveRequestConverter;

    @Test
    void testListBooks() throws Exception {
        BookDto book1 = BookDto.builder()
            .id(1L)
            .title("Book Title 1")
            .author(AuthorDto.builder().id(1L).firstName("John").lastName("Doe").build())
            .genres(new HashSet<>(Collections.singletonList(PredefinedGenres.Dtos.FANTASY_GENRE)))
            .build();

        BookDto book2 = BookDto.builder()
            .id(2L)
            .title("Book Title 2")
            .author(AuthorDto.builder().id(2L).firstName("Jane").lastName("Smith").build())
            .genres(new HashSet<>(Collections.singletonList(PredefinedGenres.Dtos.BIOGRAPHY_GENRE)))
            .build();

        when(bookService.findAll()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/books/list"))
            .andExpect(status().isOk())
            .andExpect(view().name("list-books"))
            .andExpect(model().attributeExists("books"))
            .andExpect(model().attribute("books", Arrays.asList(book1, book2)));
    }

    @Test
    void testGetById() throws Exception {
        BookDto book = BookDto.builder()
            .id(1L)
            .title("Book Title")
            .author(AuthorDto.builder().id(1L).firstName("John").lastName("Doe").build())
            .genres(new HashSet<>())
            .build();

        when(bookService.findById(1L)).thenReturn(book);

        mockMvc.perform(get("/books/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("single-book"))
            .andExpect(model().attributeExists("book"))
            .andExpect(model().attribute("book", book));
    }

    @Test
    void testShowFormForAdd() throws Exception {
        // Mock authors to be returned
        when(authorService.findAll()).thenReturn(Arrays.asList(
            AuthorDto.builder().id(1L).firstName("John").lastName("Doe").build(),
            AuthorDto.builder().id(2L).firstName("Jane").lastName("Smith").build()
        ));

        when(genreService.findAll()).thenReturn(Arrays.asList(
            GenreDto.builder().id(1L).name("Genre 1").build(),
            GenreDto.builder().id(2L).name("Genre 2").build()
        ));

        mockMvc.perform(get("/books/showFormForAdd"))
            .andExpect(status().isOk())
            .andExpect(view().name("book-form"))
            .andExpect(model().attributeExists("book"))
            .andExpect(model().attributeExists("authors"))
            .andExpect(model().attributeExists("genres"));
    }

    @WithMockUser(roles = "ADMIN")

    @Test
    void testSaveBook() throws Exception {
        SaveBookRequest newBook = SaveBookRequest.builder()
            .title("New Book Title")
            .authorId(1L)
            .description("This is a description.")
            .isbn("123456789")
            .serialNumber("SN123456")
            .genreIds(Set.of(1L, 2L))  // Assuming at least one genre
            .build();

        // Post request to save the book
        mockMvc.perform(post("/books/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("title", newBook.getTitle())
                .param("authorId", String.valueOf(newBook.getAuthorId()))
                .param("description", newBook.getDescription())
                .param("isbn", newBook.getIsbn())
                .param("serialNumber", newBook.getSerialNumber())
                .param("genreIds", String.join(",", newBook.getGenreIds().stream()
                    .map(String::valueOf) // Convert each ID to String
                    .collect(Collectors.toSet())) // Collect back to a Set to ensure uniqueness

                ))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/books/list"));
    }

    @Test
    void testUpdateBook() throws Exception {
        Long bookId = 1L;

        BookDto existingBook = BookDto.builder()
            .id(bookId)
            .title("Updated Title")
            .author(AuthorDto.builder().id(1L).firstName("John").lastName("Doe").build())
            .genres(new HashSet<>(Collections.singletonList(PredefinedGenres.Dtos.FANTASY_GENRE)))
            .build();

        when(bookService.findById(bookId)).thenReturn(existingBook);
        SaveBookRequest saveBookRequest = SaveBookRequest.builder()
            .id(existingBook.getId()).title(existingBook.getTitle())
            .isbn(existingBook.getIsbn())
            .authorId(existingBook.getId())
            .description(existingBook.getDescription())
            .serialNumber(existingBook.getSerialNumber())
            .genreIds(existingBook.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet()))
            .build();

        when(bookToSaveRequestConverter.convert(existingBook)).thenReturn(saveBookRequest);

        when(authorService.findAll()).thenReturn(Arrays.asList(
            AuthorDto.builder().id(1L).firstName("John").lastName("Doe").build(),
            AuthorDto.builder().id(2L).firstName("Jane").lastName("Smith").build()
        ));

        when(genreService.findAll()).thenReturn(Arrays.asList(
            GenreDto.builder().id(1L).name("Genre 1").build(),
            GenreDto.builder().id(2L).name("Genre 2").build()
        ));

        mockMvc.perform(get("/books/showFormForUpdate?bookId=" + bookId))
            .andExpect(status().isOk())
            .andExpect(view().name("book-form"))
            .andExpect(model().attributeExists("book"))
            .andExpect(model().attribute("book", saveBookRequest))
            .andExpect(model().attributeExists("authors"))
            .andExpect(model().attributeExists("genres"));
    }

    @Test
    void testDeleteBook() throws Exception {
        mockMvc.perform(get("/books/delete?bookId=1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/books/list"));
    }

    @Test
    void testShowFormForUpdate() throws Exception {
        Long bookId = 1L;

        BookDto existingBook = BookDto.builder()
            .id(bookId)
            .title("Updated Title")
            .author(AuthorDto.builder().id(1L).firstName("John").lastName("Doe").build())
            .genres(new HashSet<>(Collections.singletonList(GenreDto.builder().id(1L).name("Genre 1").build())))
            .build();

        when(bookService.findById(bookId)).thenReturn(existingBook);
        SaveBookRequest saveBookRequest = SaveBookRequest.builder()
            .id(existingBook.getId()).title(existingBook.getTitle())
            .isbn(existingBook.getIsbn())
            .authorId(existingBook.getId())
            .description(existingBook.getDescription())
            .serialNumber(existingBook.getSerialNumber())
            .genreIds(existingBook.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet()))
            .build();

        when(bookToSaveRequestConverter.convert(existingBook)).thenReturn(saveBookRequest);

        when(authorService.findAll()).thenReturn(Arrays.asList(
            AuthorDto.builder().id(1L).firstName("John").lastName("Doe").build(),
            AuthorDto.builder().id(2L).firstName("Jane").lastName("Smith").build()
        ));

        when(genreService.findAll()).thenReturn(Arrays.asList(
            GenreDto.builder().id(1L).name("Genre 1").build(),
            GenreDto.builder().id(2L).name("Genre 2").build()
        ));

        mockMvc.perform(get("/books/showFormForUpdate?bookId=" + bookId))
            .andExpect(status().isOk())
            .andExpect(view().name("book-form"))
            .andExpect(model().attributeExists("book"))
            .andExpect(model().attribute("book", saveBookRequest))
            .andExpect(model().attributeExists("authors"))
            .andExpect(model().attributeExists("genres"));
    }
}
