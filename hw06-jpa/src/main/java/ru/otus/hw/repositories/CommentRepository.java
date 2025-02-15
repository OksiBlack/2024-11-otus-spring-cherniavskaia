package ru.otus.hw.repositories;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CommentRepository {

    List<Comment> findAll();

    Optional<Comment> findById(Long id);

    List<Comment> findAllByBookId(Long bookId);

    List<Comment> findAllByIds(Set<Long> ids);

    void deleteById(Long id);

    boolean existsById(Long id);

    Comment save(Comment comment);
}
