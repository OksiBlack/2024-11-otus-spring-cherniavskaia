package ru.otus.hw.mapper;

import org.springframework.stereotype.Service;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Genre;

@Service
public class GenreMapper implements ModelToDtoBidirectionalMapper<Genre, GenreDto> {
    @Override
    public GenreDto mapToDto(Genre model) {
        return GenreDto.builder()
            .id(model.getId())
            .name(model.getName())
            .description(model.getDescription())

            .build();
    }

    @Override
    public Genre mapToModel(GenreDto dto) {
        return Genre.builder()
            .id(dto.getId())
            .name(dto.getName())
            .description(dto.getDescription())
            .build();
    }
}
