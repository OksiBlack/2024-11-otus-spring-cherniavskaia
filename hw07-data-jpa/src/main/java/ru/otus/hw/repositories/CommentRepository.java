package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentRepository extends ListCrudRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    List<Comment> findAllByBookId(Long bookId);
}
