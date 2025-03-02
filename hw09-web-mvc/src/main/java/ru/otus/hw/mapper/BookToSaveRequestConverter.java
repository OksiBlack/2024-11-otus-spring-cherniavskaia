package ru.otus.hw.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.SaveBookRequest;

import java.util.stream.Collectors;

@Service
public class BookToSaveRequestConverter implements Converter<BookDto, SaveBookRequest> {
    @Override
    public SaveBookRequest convert(BookDto source) {
        return SaveBookRequest.builder()
            .id(source.getId())
            .title(source.getTitle())
            .isbn(source.getIsbn())
            .description(source.getDescription())
            .serialNumber(source.getSerialNumber())
            .authorId(source.getAuthor().getId())
            .genreIds(source.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet()))
            .build();
    }
}
