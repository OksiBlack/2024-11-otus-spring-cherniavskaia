package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final Converter<Comment, CommentDto> commentConverter;

    @Transactional(readOnly = true)
    @Override
    public Optional<CommentDto> findById(Long id) {
        return commentRepository.findById(id)
            .map(commentConverter::convert);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(Long bookId) {
        return commentRepository.findAllByBookId(bookId)
            .stream()
            .map(commentConverter::convert)
            .toList();
    }

    @Transactional
    @Override
    public CommentDto create(String commentText, Long bookId, String author) {

        var book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));

        Comment comment = Comment.builder()
            .created(LocalDate.now())
            .book(book)
            .author(author)
            .text(commentText)
            .build();

        commentRepository.save(comment);
        return commentConverter.convert(comment);
    }

    @Transactional
    @Override
    public CommentDto update(Long id, String commentText, String commentAuthorName) {

        var comment = commentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));

        comment.setText(commentText);
        comment.setAuthor(commentAuthorName);

        commentRepository.save(comment);
        return commentConverter.convert(comment);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return commentRepository.existsById(id);
    }
}
