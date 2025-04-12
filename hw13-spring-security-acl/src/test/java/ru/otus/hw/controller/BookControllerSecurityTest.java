package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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

@WebMvcTest(BookController.class)
@EnableMethodSecurity
public class BookControllerSecurityTest {

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
    @WithMockUser(roles = "ADMIN")
    void testListBooks_WithAdminRole_ShouldReturnBooksView() throws Exception {
        mockMvc.perform(get("/books/list"))
            .andExpect(status().isOk())
            .andExpect(view().name("list-books"))
            .andExpect(model().attributeExists("books"));
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void testListBooks_WithEditorRole_ShouldReturnBooksView() throws Exception {
        mockMvc.perform(get("/books/list"))
            .andExpect(status().isOk())
            .andExpect(view().name("list-books"))
            .andExpect(model().attributeExists("books"));
    }

    @Test
    @WithMockUser(roles = "USER")
        // Should not have access
    void testShowFormForAdd_WithUserRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/books/showFormForAdd"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testShowFormForAdd_WithAdminRole_ShouldReturnAddBookForm() throws Exception {
        mockMvc.perform(get("/books/showFormForAdd"))
            .andExpect(status().isOk())
            .andExpect(view().name("book-form"))
            .andExpect(model().attributeExists("book"))
            .andExpect(model().attributeExists("authors"))
            .andExpect(model().attributeExists("genres"));
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void testShowFormForUpdate_WithEditorRole_ShouldReturnUpdateBookForm() throws Exception {
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

        mockMvc.perform(get("/books/showFormForUpdate").param("bookId", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("book-form"))
            .andExpect(model().attributeExists("book"))
            .andExpect(model().attributeExists("authors"))
            .andExpect(model().attributeExists("genres"));
    }

    @Test
    void testShowFormForAdd_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/books/showFormForAdd"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteBook_WithUserRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/books/delete").param("bookId", "1"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteBook_WithAdminRole_ShouldRedirectToBooksList() throws Exception {
        mockMvc.perform(get("/books/delete").param("bookId", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/books/list"));
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void testSaveBook_WithEditorRole_ShouldRedirectToBooksList() throws Exception {
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
    void testSaveBook_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/books/save")
                .param("title", "New Book Title")
                .param("authorId", "1")
                .param("genreId", "1")
                .param("description", "Description of the book")
                .with(csrf())
            )
            .andExpect(status().isUnauthorized());
    }

}

