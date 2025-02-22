package ru.otus.hw.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

@Component
public class GenreConverter implements Converter<Genre, GenreDto> {
    public String genreToString(GenreDto genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }

    @Override
    public GenreDto convert(Genre source) {
        return GenreDto.builder()
            .name(source.getName())
            .id(source.getId())
            .build();
    }
}
