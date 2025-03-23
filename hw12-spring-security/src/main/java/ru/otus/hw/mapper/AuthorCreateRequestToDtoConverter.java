package ru.otus.hw.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.request.SaveAuthorRequest;
import ru.otus.hw.dto.AuthorDto;

@Service
public class AuthorCreateRequestToDtoConverter implements Converter<SaveAuthorRequest, AuthorDto> {

    @Override
    public AuthorDto convert(SaveAuthorRequest source) {

        return AuthorDto.builder()
            .description(source.getDescription())
            .middleName(source.getMiddleName())
            .lastName(source.getLastName())
            .firstName(source.getFirstName())
            .build();
    }
}
