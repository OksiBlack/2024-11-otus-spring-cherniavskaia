package ru.otus.hw.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;

@Component
public class AuthorConverter implements Converter<Author, AuthorDto> {
    public String authorToString(AuthorDto author) {
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }

    @Override
    public AuthorDto convert(Author source) {
        return AuthorDto.builder().id(source.getId()).fullName(source.getFullName()).build();
    }
}
