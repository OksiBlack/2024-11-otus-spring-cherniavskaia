package ru.otus.hw.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSearchFilter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.request.SaveBookRequest;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.CommentService;
import ru.otus.hw.testObjects.TestData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(roles = {TestData.RoleNames.READER})
@Import({TestConfig.class})
@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    @MockitoBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @MockitoBean
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        // Prepare any required initial setup for your tests here
    }

    @Test
    void testListBooks() throws Exception {
        BookDto book1 = BookDto.builder().id(1L).title("Book Title 1").build();
        BookDto book2 = BookDto.builder().id(2L).title("Book Title 2").build();

        when(bookService.findAll()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
            .andExpect(jsonPath("$[0].title").value("Book Title 1"))
            .andExpect(jsonPath("$[1].title").value("Book Title 2"));
    }

    @Test
    void testGetById() throws Exception {
        BookDto book = BookDto.builder().id(1L).title("Book Title").build();

        when(bookService.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("Book Title"));
    }

    @Test
    void testGetById_NotFound() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/1"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @WithMockUser(roles = {TestData.RoleNames.EDITOR})
    @Test
    void testCreateBook() throws Exception {
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
                .content(objectMapper.writeValueAsString(saveBookRequest)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(3L))
            .andExpect(jsonPath("$.title").value("New Book Title"));
    }

    @WithMockUser(roles = {TestData.RoleNames.EDITOR})
    @Test
    void testUpdateBook() throws Exception {
        Long bookId = 1L;
        SaveBookRequest saveBookRequest = SaveBookRequest.builder()
            .title("Updated Book Title")
            .authorId(1L)
            .description("This is an updated book.")
            .isbn("123456790")
            .serialNumber("SN123789")
            .genreIds(Collections.singleton(2L))
            .build();

        BookDto updatedBook = BookDto.builder().id(bookId).title("Updated Book Title").build();

        when(bookService.save(any(SaveBookRequest.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/{bookId}", bookId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveBookRequest)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("Updated Book Title"));
    }

    @WithMockUser(roles = {TestData.RoleNames.ADMIN})
    @Test
    void testDeleteBook() throws Exception {
        Long bookId = 1L;
        doNothing().when(bookService).deleteById(bookId);

        mockMvc.perform(delete("/api/books/{bookId}", bookId)
                .with(csrf())
            )
            .andExpect(status().isNoContent());

        // Verify that the deleteById method is called
    }

    @Test
    void testFindBookByFilter() throws Exception {
        BookSearchFilter filter = new BookSearchFilter();
        // Set any filter criteria you want to test
        List<BookDto> filteredBooks = Collections.singletonList(BookDto.builder().title("Filtered Book").build());

        when(bookService.findAll(any(BookSearchFilter.class))).thenReturn(filteredBooks);

        mockMvc.perform(post("/api/books/search")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
            .andExpect(jsonPath("$[0].title").value("Filtered Book"));
    }

    @Test
    void testListCommentsForBook() throws Exception {
        CommentDto comment = CommentDto.builder().id(1L).text("Great book!").build();

        when(commentService.findAllByBookId(1L)).thenReturn(Collections.singletonList(comment));

        mockMvc.perform(get("/api/books/{bookId}/comments", 1L)
                .with(csrf())
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
            .andExpect(jsonPath("$[0].text").value("Great book!"));
    }
}
