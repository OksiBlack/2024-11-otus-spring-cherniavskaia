package ru.otus.hw.api.rest.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.request.SaveGenreRequest;
import ru.otus.hw.service.GenreService;
import ru.otus.hw.service.GenreServiceImpl;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Import({GenreServiceImpl.class})
@WebMvcTest(GenreRestController.class)
class GenreRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenreService genreService;

    @InjectMocks
    private GenreRestController genreRestController;

    @Test
    void listGenresTest() throws Exception {
        // Given
        GenreDto genre1 = GenreDto.builder()
            .name("Fantasy")
            .description("Fantasy genre")
            .build();

        GenreDto genre2 = GenreDto.builder()
            .name("Science Fiction")
            .description("Sci-fi genre")
            .build();

        when(genreService.findAll()).thenReturn(Arrays.asList(genre1, genre2));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/genres")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Fantasy"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Science Fiction"));
    }

    @Test
    void getByIdTest() throws Exception {
        // Given
        Long genreId = 1L;
        GenreDto genre = GenreDto.builder()
            .name("Fantasy")
            .description("Fantasy genre")
            .build();

        when(genreService.findById(genreId)).thenReturn(Optional.of(genre));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/genres/{id}", genreId)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Fantasy"));
    }

    @Test
    void getByIdNotFoundTest() throws Exception {
        // Given
        Long genreId = 10000L;

        when(genreService.findById(genreId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/genres/{id}", genreId)
                .accept(MediaType.APPLICATION_PROBLEM_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createGenreTest() throws Exception {
        // Given
        SaveGenreRequest saveGenreRequest = new SaveGenreRequest();
        saveGenreRequest.setName("Fantasy");
        saveGenreRequest.setDescription("Fantasy genre");

        GenreDto createdGenre = GenreDto.builder()
            .name("Fantasy")
            .description("Fantasy genre")
            .build();

        when(genreService.save(any(GenreDto.class))).thenReturn(createdGenre);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Fantasy",
                            "description": "Fantasy genre"
                        }
                        """))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Fantasy"));

        ArgumentCaptor<GenreDto> captor = ArgumentCaptor.forClass(GenreDto.class);
        verify(genreService, times(1)).save(captor.capture());
        assert "Fantasy".equals(captor.getValue().getName());
    }

    @Test
    void updateGenreTest() throws Exception {
        // Given
        Long genreId = 1L;
        GenreDto updatedGenre = GenreDto.builder()
            .name("Updated Fantasy")
            .description("Updated description")
            .build();

        when(genreService.findById(genreId)).thenReturn(Optional.of(updatedGenre));
        when(genreService.save(any(GenreDto.class))).thenReturn(updatedGenre);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/genres/{genreId}", genreId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Updated Fantasy",
                            "description": "Updated description"
                        }
                        """))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Fantasy"));

        verify(genreService, times(1)).save(any(GenreDto.class));
    }

    @Test
    void deleteGenreTest() throws Exception {
        // Given
        Long genreId = 1L;

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/genres/{genreId}", genreId))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(genreService, times(1)).deleteById(genreId);
    }
}
