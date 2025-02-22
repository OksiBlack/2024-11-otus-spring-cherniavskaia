package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(Long id);

    List<CommentDto> findAllByBookId(Long bookId);

    CommentDto create(String commentText, Long bookId, String author);

    CommentDto update(Long id, String commentText, String author);

    void deleteById(Long id);

    boolean existsById(Long id);
}
