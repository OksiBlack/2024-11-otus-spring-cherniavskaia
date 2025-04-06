package ru.otus.hw.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.service.GenreService;

import java.util.Optional;

@EnableMethodSecurity
@WebMvcTest(GenreController.class)
public class GenreControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenreService genreService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testListGenres_WithAdminRole_ShouldReturnGenresView() throws Exception {
        mockMvc.perform(get("/genres/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-genres"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void testListGenres_WithEditorRole_ShouldReturnGenresView() throws Exception {
        mockMvc.perform(get("/genres/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-genres"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    @WithMockUser(roles = "USER") // Should not have access
    void testShowFormForAdd_WithUserRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/genres/showFormForAdd"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testShowFormForAdd_WithAdminRole_ShouldReturnAddGenreForm() throws Exception {
        mockMvc.perform(get("/genres/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(view().name("genre-form"))
                .andExpect(model().attributeExists("genre"));
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void testShowFormForUpdate_WithEditorRole_ShouldReturnUpdateGenreForm() throws Exception {
        // Given
        Long genreId = 1L;
        GenreDto genre = GenreDto.builder().id(genreId).name("Fiction").description("Fictional Books").build();

        when(genreService.findById(genreId)).thenReturn(Optional.of(genre));

        mockMvc.perform(get("/genres/showFormForUpdate").param("genreId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("genre-form"))
                .andExpect(model().attributeExists("genre"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testShowFormForUpdate_WithUserRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/genres/showFormForUpdate").param("genreId", "1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteGenre_WithAdminRole_ShouldRedirectToGenresList() throws Exception {
        mockMvc.perform(get("/genres/delete").param("genreId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/genres/list"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteGenre_WithUserRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/genres/delete").param("genreId", "1"))
                .andExpect(status().isForbidden());
    }


    @Test
    void testSaveGenre_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/genres/save")
                .with(csrf())
                .param("name", "New Genre"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void testSaveGenre_WithValidationErrors_ShouldReturnGenreFormView() throws Exception {
        mockMvc.perform(post("/genres/save")
                .with(csrf())
                .param("name", ""))  // Assuming name is a required field
                .andExpect(status().isOk())
                .andExpect(view().name("genre-form"))
                .andExpect(model().attributeHasFieldErrors("genre", "name"));
    }

}

