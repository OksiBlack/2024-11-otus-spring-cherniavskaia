package ru.otus.hw.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.request.SaveCommentRequest;
import ru.otus.hw.service.CommentService;
import ru.otus.hw.testObjects.TestData;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
@WebMvcTest(CommentRestController.class)
class CommentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    @MockitoBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @InjectMocks
    private CommentRestController commentRestController;

    @Autowired
    private ObjectMapper objectMapper; // Used for JSON serialization

    @Test
    void testGetCommentById() throws Exception {
        Long commentId = 1L;
        CommentDto commentDto = CommentDto.builder()
            .id(commentId)
            .author("Author Name")
            .bookId(1L)
            .created(LocalDate.now())
            .text("This is a comment")
            .build();

        when(commentService.findById(commentId)).thenReturn(Optional.of(commentDto));

        mockMvc.perform(get("/api/comments/{id}", commentId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(commentId))
            .andExpect(jsonPath("$.author").value("Author Name"))
            .andExpect(jsonPath("$.text").value("This is a comment"));
    }

    @WithMockUser(roles = {TestData.RoleNames.EDITOR})
    @Test
    void testCreateComment() throws Exception {
        SaveCommentRequest saveCommentRequest = SaveCommentRequest.builder()
            .author("Author Name")
            .bookId(1L)
            .created(LocalDate.now())
            .text("This is a new comment")
            .build();

        CommentDto createdCommentDto = CommentDto.builder()
            .id(2L)
            .author("Author Name")
            .bookId(1L)
            .created(saveCommentRequest.getCreated())
            .text("This is a new comment")
            .build();

        when(commentService.save(any(CommentDto.class))).thenReturn(createdCommentDto);

        mockMvc.perform(post("/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(saveCommentRequest)))

            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(2L))
            .andExpect(jsonPath("$.author").value("Author Name"))
            .andExpect(jsonPath("$.text").value("This is a new comment"));
    }

    @WithMockUser(roles = {TestData.RoleNames.EDITOR})
    @Test
    void testUpdateComment() throws Exception {
        Long commentId = 1L;
        SaveCommentRequest saveCommentRequest = SaveCommentRequest.builder()
            .author("Updated Author")
            .bookId(1L)
            .created(LocalDate.now())
            .text("Updated comment text")
            .build();

        CommentDto updatedCommentDto = CommentDto.builder()
            .id(commentId)
            .author("Updated Author")
            .bookId(1L)
            .created(saveCommentRequest.getCreated())
            .text("Updated comment text")
            .build();

        when(commentService.save(any(CommentDto.class))).thenReturn(updatedCommentDto);

        mockMvc.perform(put("/api/comments/{commentId}", commentId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveCommentRequest)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(commentId))
            .andExpect(jsonPath("$.author").value("Updated Author"))
            .andExpect(jsonPath("$.text").value("Updated comment text"));
    }

    @WithMockUser(roles = {TestData.RoleNames.ADMIN})
    @Test
    void testDeleteComment() throws Exception {
        Long commentId = 1L;

        mockMvc.perform(delete("/api/comments/{commentId}", commentId)
                .with(csrf())
            )
            .andExpect(status().isNoContent());

        verify(commentService, times(1)).deleteById(commentId);
    }

    @Test
    void testListCommentsForBook() throws Exception {
        Long bookId = 1L;
        CommentDto commentDto = CommentDto.builder()
            .id(1L)
            .author("Author Name")
            .bookId(bookId)
            .created(LocalDate.now())
            .text("This is a comment")
            .build();

        when(commentService.findAllByBookId(bookId)).thenReturn(Collections.singletonList(commentDto));

        mockMvc.perform(get("/api/comments?bookId={bookId}", bookId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].author").value("Author Name"))
            .andExpect(jsonPath("$[0].text").value("This is a comment"));
    }
}
