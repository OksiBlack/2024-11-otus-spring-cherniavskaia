package ru.otus.hw.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.request.SaveBookRequest;
import ru.otus.hw.dto.request.SaveCommentRequest;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.CommentService;
import ru.otus.hw.service.GenreService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookRestController.class)
@EnableMethodSecurity
class BookRestControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    @MockitoBean
    private ClientRegistrationRepository clientRegistrationRepository;

    private final SaveBookRequest saveBookRequest = SaveBookRequest.builder()
        .title("New Book Title")
        .authorId(1L)
        .description("This is a new book.")
        .isbn("987654321")
        .serialNumber("SN987654")
        .genreIds(Collections.singleton(1L))
        .build();

    private final SaveCommentRequest saveCommentRequest = SaveCommentRequest.builder()
        .bookId(1L)
        .author("Some author")
        .created(LocalDate.now())
        .text("Some text")
        .build();

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Test
    @WithMockUser(roles = "READER")
    void listBooks_ShouldAllowAccess_ForReaderRole() throws Exception {
        mockMvc.perform(get("/api/books")
                .with(csrf())
            )
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "READER")
    void getById_ShouldAllowAccess_ForReaderRole() throws Exception {
        BookDto book = BookDto.builder().id(1L).title("Book Title").build();

        when(bookService.findById(1L)).thenReturn(Optional.of(book));
        mockMvc.perform(get("/api/books/1")
                .with(csrf())
            )
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void createBook_ShouldAllowAccess_ForEditorRole() throws Exception {
        String bookJson = jacksonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(saveBookRequest);
        mockMvc.perform(
                post("/api/books")
                    .with(csrf())
                    .contentType("application/json")
                    .content(bookJson))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteBook_ShouldAllowAccess_ForAdminRole() throws Exception {
        mockMvc.
            perform(delete("/api/books/1")
                .with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"EDITOR"})
    void updateBook_ShouldAllowAccess_ForEditorRole() throws Exception {
        String bookUpdateJson = jacksonObjectMapper.writeValueAsString(
            saveBookRequest.withDescription("updated description"));

        when(bookService.save(any(SaveBookRequest.class))).thenReturn(BookDto.builder().build());

        mockMvc.perform(put("/api/books/1")
                .with(csrf())
                .contentType("application/json")
                .content(bookUpdateJson))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "READER")
    void createBook_ShouldDenyAccess_ForReaderRole() throws Exception {
        SaveBookRequest saveBookRequest = SaveBookRequest.builder()
            .title("New Book Title")
            .authorId(1L)
            .description("This is a new book.")
            .isbn("987654321")
            .serialNumber("SN987654")
            .genreIds(Collections.singleton(1L))
            .build();

        BookDto newBook = BookDto.builder()
            .id(3L)
            .title(saveBookRequest.getTitle())
            .build();

        when(bookService.save(any(SaveBookRequest.class))).thenReturn(newBook);

        mockMvc.perform(post("/api/books")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(saveBookRequest)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser()
    void deleteBook_ShouldDenyAccess_ForEditorRole() throws Exception {
        mockMvc.perform(delete("/api/books/1")
                .with(csrf())
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "READER")
    void updateBook_ShouldDenyAccess_ForReaderRole() throws Exception {
        String bookUpdateJson = jacksonObjectMapper.writeValueAsString(
            saveBookRequest.withDescription("updated description")
        );
        mockMvc.perform(put("/api/books/1")
                .contentType("application/json")
                .with(csrf())
                .content(bookUpdateJson))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listComments_ShouldAllowAccess_ForAdminRole() throws Exception {
        mockMvc.perform(get("/api/books/1/comments")
                .with(csrf())
            )
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void createComment_ShouldAllowAccess_ForEditorRole() throws Exception {
        String commentJson = jacksonObjectMapper.writeValueAsString(saveCommentRequest);
        mockMvc.perform(post("/api/books/1/comments")
                .with(csrf())
                .contentType("application/json")
                .content(commentJson))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "READER")
    void createComment_ShouldDenyAccess_ForReaderRole() throws Exception {
        String commentJson = jacksonObjectMapper.writeValueAsString(saveCommentRequest);
        mockMvc.perform(post("/api/books/1/comments")
                .with(csrf())

                .contentType("application/json")
                .content(commentJson))
            .andExpect(status().isForbidden());
    }
}
