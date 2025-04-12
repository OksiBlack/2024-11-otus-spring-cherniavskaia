package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.service.AuthorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthorController.class)
@EnableMethodSecurity
class AuthorControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorService authorService;

    // Test for unauthorized access to show form for adding an author
    @Test
    void testShowFormForAdd_Unauthorized() throws Exception {
        mockMvc.perform(get("/authors/showFormForAdd")
            .with(csrf()))
            .andExpect(status().isUnauthorized());
    }

    // Test for authorized access to show form for adding an author
    @WithMockUser(roles = {"ADMIN", "EDITOR"})
    @Test
    void testShowFormForAdd_Authorized() throws Exception {
        mockMvc.perform(get("/authors/showFormForAdd"))
            .andExpect(status().isOk())
            .andExpect(view().name("author-form"));
    }

    // Test for unauthorized access to save an author
    @Test
    void testSaveAuthor_Unauthorized() throws Exception {
        mockMvc.perform(post("/authors/save")
                .with(csrf())
                .with(anonymous()))
            .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {"READER"})
    @Test
    void testSaveAuthor_Forbidden() throws Exception {
        mockMvc.perform(post("/authors/save")
                .with(csrf()))
            .andExpect(status().isForbidden());
    }

    // Test for valid author saving as an authorized role
    @WithMockUser(roles = {"ADMIN"})
    @Test
    void testSaveAuthor_Authorized() throws Exception {
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
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("firstName", "John")
                .param("middleName", "H.")
                .param("lastName", "Doe")
                .param("description", "A prolific author."))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.redirectedUrl("/authors/list"));
    }

    // Test for deleting an author as an authorized role
    @WithMockUser(roles = {"ADMIN"})
    @Test
    void testDeleteAuthor_Authorized() throws Exception {
        mockMvc.perform(get("/authors/delete?authorId=1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/authors/list"));
    }

    // Test unauthorized access to delete an author
    @Test
    void testDeleteAuthor_Unauthorized() throws Exception {
        mockMvc.perform(get("/authors/delete?authorId=1"))
            .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {"EDITOR"})
    @Test
    void testDeleteAuthor_isForbidden() throws Exception {
        mockMvc.perform(get("/authors/delete?authorId=1"))
            .andExpect(status().isForbidden());
    }
}
