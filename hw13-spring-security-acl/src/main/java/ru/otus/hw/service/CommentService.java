package ru.otus.hw.service;

import ru.otus.hw.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto findById(Long id);

    List<CommentDto> findAllByBookId(Long bookId);

    CommentDto save(CommentDto commentDto);

    void deleteById(Long id);

    boolean existsById(Long id);
}
