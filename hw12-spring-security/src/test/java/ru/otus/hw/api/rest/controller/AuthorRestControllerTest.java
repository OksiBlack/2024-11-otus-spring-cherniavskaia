package ru.otus.hw.api.rest.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.AuthorServiceImpl;
import ru.otus.hw.testObjects.TestData;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WithMockUser(roles = {TestData.RoleNames.READER})
@Import({AuthorServiceImpl.class})
@WebMvcTest(AuthorRestController.class)
class AuthorRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorService authorService;

    @InjectMocks
    private AuthorRestController authorRestController;

    @Test
    void listAuthorsTest() throws Exception {
        // Given
        AuthorDto author1 = AuthorDto.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .middleName("M.")
            .description("First author")
            .build();

        AuthorDto author2 = AuthorDto.builder()
            .id(2L)
            .firstName("Jane")
            .lastName("Smith")
            .middleName("A.")
            .description("Second author")
            .build();

        when(authorService.findAll()).thenReturn(Arrays.asList(author1, author2));

        // When & Then
        mockMvc.perform(get("/api/authors")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("John"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    void getByIdTest() throws Exception {
        // Given
        Long authorId = 1L;
        AuthorDto author = AuthorDto.builder()
            .id(authorId)
            .firstName("John")
            .lastName("Doe")
            .middleName("M.")
            .description("A prolific author.")
            .build();

        when(authorService.findById(authorId)).thenReturn(author);

        // When & Then
        mockMvc.perform(get("/api/authors/{authorId}", authorId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));
    }

    @Test
    void createAuthorTest() throws Exception {
        // Given
        AuthorDto createdAuthor = AuthorDto.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .middleName("M.")
            .description("Some description")
            .build();

        when(authorService.save(any(AuthorDto.class))).thenReturn(createdAuthor);

        // When & Then
        mockMvc.perform(post("/api/authors")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "firstName": "John",
                        "lastName": "Doe",
                        "middleName": "M.",
                        "description": "Some description"
                    }
                    """))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));

        ArgumentCaptor<AuthorDto> captor = ArgumentCaptor.forClass(AuthorDto.class);
        verify(authorService, times(1)).save(captor.capture());

        // Validate that the correct author is saved
        AuthorDto savedAuthor = captor.getValue();
        assert "John".equals(savedAuthor.getFirstName());
        assert "M.".equals(savedAuthor.getMiddleName());
        assert "Doe".equals(savedAuthor.getLastName());
        assert "Some description".equals(savedAuthor.getDescription());
    }

    @Test
    void updateAuthorTest() throws Exception {
        // Given
        Long authorId = 1L;
        AuthorDto updatedAuthor = AuthorDto.builder()
            .id(authorId)
            .firstName("Updated")
            .lastName("Doe")
            .middleName("M.")
            .description("Updated description")
            .build();

        when(authorService.save(any(AuthorDto.class))).thenReturn(updatedAuthor);

        // When & Then
        mockMvc.perform(put("/api/authors/{authorId}", authorId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content("""
                    {
                        "firstName": "Updated",
                        "lastName": "Doe",
                        "middleName": "M.",
                        "description": "Updated description"
                    }
                    """))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Updated"));

        verify(authorService, times(1)).save(any(AuthorDto.class));
    }

    @Test
    void deleteTest() throws Exception {
        // Given
        Long authorId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/authors/{authorId}", authorId)
                .with(csrf())
            )
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(authorService, times(1)).deleteById(authorId);
    }
}
