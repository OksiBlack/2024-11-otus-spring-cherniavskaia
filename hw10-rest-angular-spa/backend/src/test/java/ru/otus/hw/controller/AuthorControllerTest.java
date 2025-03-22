package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.AuthorServiceImpl;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Import({AuthorServiceImpl.class})
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;


    @Test
    void listAuthorsTest() throws Exception {
        // Given
        AuthorDto author1 = AuthorDto.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .middleName("H.")
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
        mockMvc.perform(get("/authors/list"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("list-authors"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("authors"));
    }

    @Test
    void getByIdTest() throws Exception {
        // Given
        Long authorId = 1L;
        AuthorDto author = AuthorDto.builder()
            .id(authorId)
            .firstName("John")
            .lastName("Doe")
            .middleName("H.")
            .description("A prolific author.")
            .build();

        when(authorService.findById(authorId)).thenReturn(author);

        // When & Then
        mockMvc.perform(get("/authors/{authorId}", authorId))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("single-author"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("author"));
    }

    @Test
    void showFormForAddTest() throws Exception {
        // When & Then
        mockMvc.perform(get("/authors/showFormForAdd"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("author-form"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("author"));
    }

    @Test
    void showFormForUpdateTest() throws Exception {
        // Given
        Long authorId = 1L;
        AuthorDto author = AuthorDto.builder()
            .id(authorId)
            .firstName("John")
            .lastName("Doe")
            .middleName("H.")
            .description("A prolific author.")
            .build();

        when(authorService.findById(authorId)).thenReturn(author);

        // When & Then
        mockMvc.perform(get("/authors/showFormForUpdate").param("authorId", String.valueOf(authorId)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("author-form"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("author"));
    }

    @Test
    void saveAuthorTest() throws Exception {
        // Given
        AuthorDto authorDto = AuthorDto.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .middleName("H.")
            .description("A prolific author.")
            .build();

        when(authorService.save(any(AuthorDto.class))).thenReturn(authorDto);

        // When & Then
        mockMvc.perform(post("/authors/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("firstName", "John")
                .param("middleName", "H.")
                .param("lastName", "Doe")
                .param("description", "A prolific author."))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.redirectedUrl("/authors/list"));

        ArgumentCaptor<AuthorDto> captor = ArgumentCaptor.forClass(AuthorDto.class);
        verify(authorService, times(1)).save(captor.capture());

        // Validate that the correct author is saved
        AuthorDto savedAuthor = captor.getValue();
        assert (savedAuthor.getFirstName()).equals("John");
        assert (savedAuthor.getMiddleName()).equals("H.");
        assert (savedAuthor.getLastName()).equals("Doe");
        assert (savedAuthor.getDescription()).equals("A prolific author.");
    }

    @Test
    void deleteTest() throws Exception {
        // Given
        Long authorId = 1L;

        // When & Then
        mockMvc.perform(get("/authors/delete").param("authorId", String.valueOf(authorId)))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.redirectedUrl("/authors/list"));

        verify(authorService, times(1)).deleteById(authorId);
    }
}
