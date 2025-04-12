package ru.otus.hw.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.request.SaveAuthorRequest;
import ru.otus.hw.service.AuthorService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableMethodSecurity
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@WebMvcTest(AuthorRestController.class)
public class AuthorRestControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    @MockitoBean
    private ClientRegistrationRepository clientRegistrationRepository;

    private AuthorDto sampleAuthor;

    @BeforeEach
    void setup() {
        sampleAuthor = AuthorDto.builder()
            .firstName("John")
            .lastName("Doe")
            .description("A sample author")
            .id(1L)
            .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
        // User with ADMIN role to test access
    void testListAuthors() throws Exception {
        doReturn(Collections.singletonList(sampleAuthor)).when(authorService).findAll();

        mockMvc.perform(get("/api/authors"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
        // User with ADMIN role to test access
    void testGetAuthorById() throws Exception {
        doReturn(sampleAuthor).when(authorService).findById(1L);

        mockMvc.perform(get("/api/authors/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @WithMockUser(roles = "EDITOR")
        // User with EDITOR role to test access
    void testCreateAuthor() throws Exception {
        SaveAuthorRequest newAuthor = SaveAuthorRequest.builder()
            .firstName("Jane").lastName("Smith").description("Sample author").build();

        doReturn(sampleAuthor).when(authorService).save(any(AuthorDto.class));

        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAuthor))
                .with(csrf()))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "EDITOR")
        // User with EDITOR role to test access
    void testUpdateAuthor() throws Exception {
        SaveAuthorRequest updateAuthor = SaveAuthorRequest.builder()
            .firstName("Jane").lastName("Smith").description("Updated author").build();

        mockMvc.perform(put("/api/authors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAuthor))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
        // User with ADMIN role to test authorization
    void testDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/api/authors/1")
                .with(csrf())
            )
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void testDeleteAuthor_isForbidden() throws Exception {
        mockMvc.perform(delete("/api/authors/1")
                .with(csrf())
            )
            .andExpect(status().isForbidden()); // Expect a Forbidden response
    }

    @WithMockUser
    @Test
    void testListAuthors_isForbidden() throws Exception {
        mockMvc.perform(get("/api/authors"))
            .andExpect(status().isForbidden()); // Expect an Unauthorized response without user
    }
}
