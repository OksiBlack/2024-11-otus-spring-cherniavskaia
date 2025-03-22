package ru.otus.hw.api.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSearchFilter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.request.CreateCommentRequest;
import ru.otus.hw.dto.request.SaveBookRequest;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.CommentService;

import java.util.List;

@Tag(name = "Book rest api", description = "Api for book operations.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/books",
    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
)
public class BookRestController {

    private final BookService bookService;

    private final CommentService commentService;

    @Operation(summary = "List books.", description = "List books.")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
    )
    public List<BookDto> listBooks() {
        return bookService.findAll();
    }

    @Operation(summary = "Get book by id.", description = "Get book by id.")
    @GetMapping("/{id}")
    public BookDto getById(@PathVariable("id") Long bookId) {
        return bookService.findById(bookId).orElseThrow(
            () -> new EntityNotFoundException("Book not found: [%s] ".formatted(bookId))
        );
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Create book.", description = "Create book.")
    @PostMapping
    public BookDto create(@RequestBody @Valid SaveBookRequest saveBookRequest) {
        return bookService.save(saveBookRequest);
    }

    @Operation(summary = "Update book by id.", description = "Update book by id.")
    @PutMapping("/{bookId}")
    public BookDto update(@PathVariable("bookId") Long bookId, @RequestBody @Valid SaveBookRequest saveBookRequest) {
        saveBookRequest.setId(bookId);
        return bookService.save(saveBookRequest);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete book by id.", description = "Delete book by id.")
    @DeleteMapping("/{bookId}")
    public void delete(@PathVariable("bookId") Long theId) {
        bookService.deleteById(theId);
    }

    @Operation(summary = "Search book with filter.", description = "Search book with filter.")
    @PostMapping(value = "/search",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
    )
    public List<BookDto> findBookByFilter(@RequestBody BookSearchFilter bookSearchFilter) {
        return bookService.findAll(bookSearchFilter);
    }

    @Operation(summary = "List all comments for book id.", description = "List all comments for book id.")
    @GetMapping(value = "{bookId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CommentDto> listCommentsForBook(@PathVariable Long bookId) {
        return commentService.findAllByBookId(bookId);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Create comment for the book.", description = "Create comment for the book.")
    @PostMapping(value = "{bookId}/comments",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
    )
    public CommentDto createCommentForBook(@PathVariable Long bookId,
                                           @RequestBody CreateCommentRequest createCommentRequest) {
        CommentDto commentDto = CommentDto.builder()
            .bookId(bookId)
            .created(createCommentRequest.getCreated())
            .text(createCommentRequest.getText())
            .author(createCommentRequest.getAuthor())
            .build();
        return commentService.save(commentDto);
    }

}
