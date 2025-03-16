package ru.otus.hw.mapper;

import org.springframework.stereotype.Service;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.model.Author;

@Service
public class AuthorMapper implements ModelToDtoBidirectionalMapper<Author, AuthorDto> {
    @Override
    public AuthorDto mapToDto(Author model) {
        return AuthorDto.builder()
            .id(model.getId())
            .firstName(model.getFirstName())
            .lastName(model.getLastName())
            .middleName(model.getMiddleName())
            .description(model.getDescription())

            .build();
    }

    @Override
    public Author mapToModel(AuthorDto dto) {
        return Author.builder()
            .id(dto.getId())
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .middleName(dto.getMiddleName())
            .description(dto.getDescription())
            .build();
    }
}
