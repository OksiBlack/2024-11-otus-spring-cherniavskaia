package ru.otus.hw.service;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(Long id);

    List<CommentDto> findAllByBookId(Long bookId);

    CommentDto save(CommentDto commentDto);

    void deleteById(Long id);

    boolean existsById(Long id);
}
