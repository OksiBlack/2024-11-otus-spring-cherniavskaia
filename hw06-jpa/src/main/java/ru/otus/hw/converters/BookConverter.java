package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter implements Converter<Book, BookDto> {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public String bookToString(BookDto book) {
        var genresString = book.getGenres().stream()
                .map(genreConverter::genreToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));

        return ("Id: %d,bbid 1" +
            "%ntitle: %s,%nauthors: [%s],%ngenres: [%s]").formatted(
                book.getId(),
                book.getTitle(),
            authorConverter.authorToString(book.getAuthor()),
                genresString);
    }

    @Override
    public BookDto convert(Book source) {
        return BookDto.builder()
            .id(source.getId())
            .title(source.getTitle())
            .genres(source.getGenres().stream().map(genreConverter::convert).toList())
            .author(authorConverter.convert(source.getAuthor()))
            .build();
    }
}
