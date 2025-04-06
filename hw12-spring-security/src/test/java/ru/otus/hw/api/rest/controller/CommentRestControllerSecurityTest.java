package ru.otus.hw.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.request.SaveCommentRequest;
import ru.otus.hw.service.CommentService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableMethodSecurity
@WebMvcTest(CommentRestController.class)
public class CommentRestControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    @MockitoBean
    private ClientRegistrationRepository clientRegistrationRepository;

    private CommentDto sampleComment;

    @BeforeEach
    void setup() {
        sampleComment = CommentDto.builder()
            .id(1L)
            .author("John Doe")
            .bookId(1L)
            .text("Great book!")
            .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetCommentById_Admin_AccessAllowed() throws Exception {
        doReturn(java.util.Optional.of(sampleComment)).when(commentService).findById(1L);

        mockMvc.perform(get("/api/comments/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.text").value("Great book!"));
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void testCreateComment_Editor_AccessAllowed() throws Exception {
        SaveCommentRequest newComment = SaveCommentRequest.builder()
            .author("Jane Doe")
            .bookId(2L)
            .text("Exciting read!").build();

        doReturn(sampleComment).when(commentService).save(any(CommentDto.class));

        mockMvc.perform(post("/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newComment))
                .with(csrf()))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void testUpdateComment_Editor_AccessAllowed() throws Exception {
        SaveCommentRequest updateComment = SaveCommentRequest.builder()
            .text("Changed my mind.").build();

        doReturn(sampleComment).when(commentService).save(any(CommentDto.class));

        mockMvc.perform(put("/api/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateComment))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteComment_Admin_AccessAllowed() throws Exception {
        mockMvc.perform(delete("/api/comments/1")
                .with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void testDeleteComment_Editor_AccessDenied() throws Exception {
        mockMvc.perform(delete("/api/comments/1")
                .with(csrf()))
            .andExpect(status().isForbidden()); // Expect a Forbidden response
    }

    @Test
    @WithMockUser(roles = "READER")
    void testListCommentsForBook_Reader_AccessAllowed() throws Exception {
        doReturn(Collections.singletonList(sampleComment)).when(commentService).findAllByBookId(1L);

        mockMvc.perform(get("/api/comments?bookId=1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    void testGetCommentById_NoRole_AccessDenied() throws Exception {
        mockMvc.perform(get("/api/comments/1"))
            .andExpect(status().isForbidden()); // Expect a Forbidden response
    }

    @Test
    @WithMockUser
    void testCreateComment_NoRole_AccessDenied() throws Exception {
        SaveCommentRequest newComment = SaveCommentRequest.builder()
            .author("Jane Doe")
            .bookId(2L)
            .text("Exciting read!").build();

        mockMvc.perform(post("/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newComment))
                .with(csrf()))
            .andExpect(status().isForbidden()); // Expect a Forbidden response
    }

    @Test
    @WithMockUser
    void testUpdateComment_NoRole_AccessDenied() throws Exception {
        SaveCommentRequest updateComment = SaveCommentRequest.builder()
            .text("Changed my mind.").build();

        mockMvc.perform(put("/api/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateComment))
                .with(csrf()))
            .andExpect(status().isForbidden()); // Expect a Forbidden response
    }

    @Test
    @WithMockUser
    void testDeleteComment_NoRole_AccessDenied() throws Exception {
        mockMvc.perform(delete("/api/comments/1").with(csrf()))
            .andExpect(status().isForbidden()); // Expect a Forbidden response
    }

    @Test
    @WithMockUser
    void testListCommentsForBook_NoRole_AccessDenied() throws Exception {
        mockMvc.perform(get("/api/comments?bookId=1"))
            .andExpect(status().isForbidden()); // Expect a Forbidden response
    }
}
