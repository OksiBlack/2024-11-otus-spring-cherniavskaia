package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.service.GenreService;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WithMockUser(roles = "READER")
@WebMvcTest(GenreController.class)
 class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenreService genreService;

    @InjectMocks
    private GenreController genreController;


    @Test
    void listGenresTest() throws Exception {
        // Given
        GenreDto genre1 = GenreDto.builder().id(1L).name("Fiction").description("Fictional Books").build();
        GenreDto genre2 = GenreDto.builder().id(2L).name("Non-Fiction").description("Non-Fictional Books").build();

        when(genreService.findAll()).thenReturn(Arrays.asList(genre1, genre2));

        // When & Then
        mockMvc.perform(get("/genres/list"))
            .andExpect(status().isOk())
            .andExpect(view().name("list-genres"))
            .andExpect(model().attributeExists("genres"));
    }

    @Test
    void getByIdTest() throws Exception {
        // Given
        Long genreId = 1L;
        GenreDto genre = GenreDto.builder().id(genreId).name("Fiction").description("Fictional Books").build();

        when(genreService.findById(genreId)).thenReturn(genre);

        // When & Then
        mockMvc.perform(get("/genres/{id}", genreId))
            .andExpect(status().isOk())
            .andExpect(view().name("single-genre"))
            .andExpect(model().attributeExists("genre"));
    }

    @Test
    void showFormForAddTest() throws Exception {
        // When & Then
        mockMvc.perform(get("/genres/showFormForAdd"))
            .andExpect(status().isOk())
            .andExpect(view().name("genre-form"))
            .andExpect(model().attributeExists("genre"));
    }

    @Test
    void showFormForUpdateTest() throws Exception {
        // Given
        Long genreId = 1L;
        GenreDto genre = GenreDto.builder().id(genreId).name("Fiction").description("Fictional Books").build();

        when(genreService.findById(genreId)).thenReturn(genre);

        // When & Then
        mockMvc.perform(get("/genres/showFormForUpdate").param("genreId", String.valueOf(genreId)))
            .andExpect(status().isOk())
            .andExpect(view().name("genre-form"))
            .andExpect(model().attributeExists("genre"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void saveGenreTest() throws Exception {
        // Given
        GenreDto genreDto = GenreDto.builder().id(1L).name("Fiction").description("Fictional Books").build();

        // When & Then
        mockMvc.perform(post("/genres/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("id", "1")
                .param("name", "Fiction")
                .param("description", "Fictional Books"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/genres/list"));

        ArgumentCaptor<GenreDto> captor = ArgumentCaptor.forClass(GenreDto.class);
        verify(genreService, times(1)).save(captor.capture());

        // Assert using AssertJ
        GenreDto savedGenre = captor.getValue();
        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre).usingRecursiveComparison()
            .isEqualTo(genreDto);

        assertThat(savedGenre.getName()).isEqualTo("Fiction");
        assertThat(savedGenre.getDescription()).isEqualTo("Fictional Books");
    }

    @Test
    void deleteGenreTest() throws Exception {
        // Given
        Long genreId = 1L;

        // When & Then
        mockMvc.perform(get("/genres/delete").param("genreId", String.valueOf(genreId)))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/genres/list"));

        verify(genreService, times(1)).deleteById(genreId);
    }
}
