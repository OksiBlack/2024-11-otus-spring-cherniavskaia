package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repository.CommentRepository;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    @Override
    public CommentDto findById(Long id) {
        Comment byId = commentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comment with id [%s] not found.".formatted(id)));

        return commentMapper.mapToDto(byId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(Long bookId) {
        return commentRepository.findAllByBookId(bookId)
            .stream()
            .map(commentMapper::mapToDto)
            .toList();
    }

    @Transactional
    @Override
    public CommentDto save(CommentDto commentDto) {

        Comment comment = commentMapper.mapToModel(commentDto);
        commentRepository.save(comment);
        return commentMapper.mapToDto(comment);
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
