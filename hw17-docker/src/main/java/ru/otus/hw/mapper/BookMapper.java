package ru.otus.hw.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookMapper implements ModelToDtoBidirectionalMapper<Book, BookDto> {

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    @Transactional(readOnly = true)
    @Override
    public BookDto mapToDto(Book model) {

        Set<GenreDto> genres = model.getGenres().stream().map(genreMapper::mapToDto).collect(Collectors.toSet());

        return BookDto.builder()
            .id(model.getId())
            .title(model.getTitle())
            .isbn(model.getIsbn())
            .description(model.getDescription())
            .serialNumber(model.getSerialNumber())
            .author(authorMapper.mapToDto(model.getAuthor()))
            .genres(genres)
            .build();
    }

    @Override
    public Book mapToModel(BookDto dto) {
        Set<Genre> genres = dto.getGenres().stream().map(genreMapper::mapToModel).collect(Collectors.toSet());

        return Book.builder()
            .id(dto.getId())
            .title(dto.getTitle())
            .isbn(dto.getIsbn())
            .description(dto.getDescription())
            .serialNumber(dto.getSerialNumber())
            .author(authorMapper.mapToModel(dto.getAuthor()))
            .genres(genres)
            .build();
    }
}
