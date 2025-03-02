package ru.otus.hw.controller.mapper;

import org.springframework.stereotype.Service;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.model.Comment;

@Service
public class CommentMapper implements ModelToDtoBidirectionalMapper<Comment, CommentDto> {

    @Override
    public CommentDto mapToDto(Comment model) {
        return CommentDto.builder()
            .text(model.getText())
            .created(model.getCreated())
            .author(model.getAuthor())
            .build();
    }

    @Override
    public Comment mapToModel(CommentDto dto) {
        return Comment.builder()
            .text(dto.getText())
            .created(dto.getCreated())
            .author(dto.getAuthor())
            .build();
    }
}
