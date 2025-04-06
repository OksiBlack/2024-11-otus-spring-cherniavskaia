package ru.otus.hw.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repository.BookRepository;

@RequiredArgsConstructor
@Service
public class CommentMapper implements ModelToDtoBidirectionalMapper<Comment, CommentDto> {

    private final BookRepository bookRepository;

    @Override
    public CommentDto mapToDto(Comment model) {
        return CommentDto.builder()
            .text(model.getText())
            .created(model.getCreated())
            .author(model.getAuthor())
            .bookId(model.getBook().getId())
            .id(model.getId())
            .build();
    }

    @Override
    public Comment mapToModel(CommentDto dto) {
        return Comment.builder()
            .id(dto.getId())
            .text(dto.getText())
            .created(dto.getCreated())
            .author(dto.getAuthor())
            .book(bookRepository.findById(dto.getBookId()).orElse(null))
            .build();
    }
}
