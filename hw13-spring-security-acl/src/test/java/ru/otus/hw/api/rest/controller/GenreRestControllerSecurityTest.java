package ru.otus.hw.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.request.SaveGenreRequest;
import ru.otus.hw.service.GenreService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableMethodSecurity
@WebMvcTest(GenreRestController.class)
class GenreRestControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    @MockitoBean
    private ClientRegistrationRepository clientRegistrationRepository;

    private GenreDto sampleGenre;

    @BeforeEach
    void setup() {
        sampleGenre = GenreDto.builder()
            .name("Fantasy")
            .id(1L)
            .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testListGenres_Admin_AccessAllowed() throws Exception {
        doReturn(Collections.singletonList(sampleGenre)).when(genreService).findAll();

        mockMvc.perform(get("/api/genres"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetGenreById_Admin_AccessAllowed() throws Exception {
        // Given
        Long genreId = 1L;

        when(genreService.findById(genreId)).thenReturn(sampleGenre);

        mockMvc.perform(get("/api/genres/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("Fantasy"));
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void testCreateGenre_Editor_AccessAllowed() throws Exception {
        SaveGenreRequest newGenre = SaveGenreRequest.builder()
            .name("Science Fiction").build();

        doReturn(sampleGenre).when(genreService).save(any(GenreDto.class));

        mockMvc.perform(post("/api/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newGenre))
                .with(csrf()))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void testUpdateGenre_Editor_AccessAllowed() throws Exception {
        SaveGenreRequest updateGenre = SaveGenreRequest.builder()
            .name("Updated Fantasy").build();

        // Given
        Long genreId = 1L;
        GenreDto updatedGenre = GenreDto.builder()
            .name("Updated Fantasy")
            .description("Updated description")
            .build();

        when(genreService.findById(genreId)).thenReturn(updatedGenre);
        when(genreService.save(any(GenreDto.class))).thenReturn(updatedGenre);

        mockMvc.perform(put("/api/genres/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateGenre))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteGenre_Admin_AccessAllowed() throws Exception {
        mockMvc.perform(delete("/api/genres/1")
                .with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void testDeleteGenre_Editor_AccessDenied() throws Exception {
        mockMvc.perform(delete("/api/genres/1")
                .with(csrf()))
            .andExpect(status().isForbidden()); // Expect a Forbidden response
    }

    @Test
    @WithMockUser(roles = "READER")
    void testListGenres_Reader_AccessAllowed() throws Exception {
        doReturn(Collections.singletonList(sampleGenre)).when(genreService).findAll();

        mockMvc.perform(get("/api/genres"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "READER")
    void testGetGenreById_Reader_AccessAllowed() throws Exception {
        // Given
        Long genreId = 1L;
        GenreDto genre = GenreDto.builder()
            .name("Fantasy")
            .description("Fantasy genre")
            .build();

        when(genreService.findById(genreId)).thenReturn(genre);

        mockMvc.perform(get("/api/genres/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("Fantasy"));
    }

    @Test
    void testListGenres_AccessDenied() throws Exception {
        mockMvc.perform(get("/api/genres")
                .with(jwt()))
            .andExpect(status().isForbidden()); // Expect an Unauthorized response without roles
    }

    @Test
    void testAllowedWithJwtAdmin() throws Exception {
        mockMvc.perform(get("/api/genres/1")
                .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
            .andExpect(status().isForbidden());
    }
}
