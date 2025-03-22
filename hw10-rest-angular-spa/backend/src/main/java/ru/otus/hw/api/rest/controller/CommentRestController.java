package ru.otus.hw.api.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.request.SaveCommentRequest;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.service.CommentService;

import java.util.List;

@Tag(name = "Comment rest api", description = "Api for book comment operations.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentRestController {
    private final CommentService commentService;

    @Operation(summary = "Get comment by id.", description = "Get comment by id.")
    @GetMapping(value = "/{id}",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    public CommentDto getById(@PathVariable("id") Long commentId) {
        return commentService.findById(commentId).orElseThrow(
            () -> new EntityNotFoundException("Comment not found: [%s] ".formatted(commentId))
        );
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Create comment.", description = "Create comment.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    public CommentDto create(@RequestBody SaveCommentRequest saveCommentRequest) {
        CommentDto commentDto = convertToCommentDto(null, saveCommentRequest);
        return commentService.save(commentDto);
    }

    @Operation(summary = "Update comment by id.", description = "Update comment by id.")
    @PutMapping(value = "/{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    public CommentDto update(@PathVariable("commentId") Long commentId,
                             @RequestBody SaveCommentRequest saveCommentRequest) {

        CommentDto commentDto = convertToCommentDto(commentId, saveCommentRequest);
        return commentService.save(commentDto);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete comment by id.", description = "Delete comment by id.")
    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable("commentId") @NotNull Long theId) {
        commentService.deleteById(theId);
    }

    @Operation(summary = "List all comments for book id.", description = "List all comments for book id.")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    public List<CommentDto> listCommentsForBook(@RequestParam Long bookId) {
        return commentService.findAllByBookId(bookId);
    }

    private CommentDto convertToCommentDto(Long commentId, SaveCommentRequest saveCommentRequest) {
        return CommentDto.builder()
            .id(commentId)
            .author(saveCommentRequest.getAuthor())
            .bookId(saveCommentRequest.getBookId())
            .created(saveCommentRequest.getCreated())
            .text(saveCommentRequest.getText())
            .build();
    }
}
