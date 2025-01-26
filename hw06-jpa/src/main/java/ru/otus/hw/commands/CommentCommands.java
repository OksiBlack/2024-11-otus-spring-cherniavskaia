package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(Long id) {
        return commentService.findById(id)
            .map(commentConverter::commentToString)
            .orElse("Comment with id %d not found".formatted(id));
    }

    @ShellMethod(value = "Find all comments by book id", key = "cbbid")
    public String findAllCommentByBookId(Long bookId) {
        return commentService.findAllByBookId(bookId).stream()
            .map(commentConverter::commentToString)
            .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // cins newComment 2
    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertComment(String content, Long bookId, String author) {
        var savedComment = commentService.insert(content, bookId, author);
        return commentConverter.commentToString(savedComment);
    }

    // cupd 4 editedComment
    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateComment(Long id, String text, Long bookId, String author) {
        var savedComment = commentService.update(id, text, bookId, author);
        return commentConverter.commentToString(savedComment);
    }

    // cdel 6
    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public void deleteComment(Long id) {
        commentService.deleteById(id);
    }
}
