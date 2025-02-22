package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

@Component
@RequiredArgsConstructor
public class CommentConverter implements Converter<Comment, CommentDto> {

    public String commentToString(CommentDto comment) {
        return "Id: %d, date: %s, text: %s, author: %s"
            .formatted(comment.getId(), comment.getCreated().toString(), comment.getText(), comment.getAuthor());
    }

    @Override
    public CommentDto convert(Comment source) {
        return CommentDto.builder()
            .id(source.getId())
            .text(source.getText())
            .created(source.getCreated())
            .author(source.getAuthor())
            .build();
    }
}