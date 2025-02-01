package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@ShellComponent
public class GenreCommands {

    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @ShellMethod(value = "Find all genres", key = "ag")
    public String findAllGenres() {
        return genreService.findAll().stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Insert genre", key = "gins")
    public String insertGenre(String name) {
        var genre = genreService.insert(name);
        return genreConverter.genreToString(genre);
    }

    @ShellMethod(value = "Find genre by id", key = "gbid")
    public String findGenreById(long id) {
        return genreService.findById(id)
            .map(genreConverter::genreToString)
            .orElse("Genre with id %d not found".formatted(id));
    }

    @ShellMethod(value = "Find all genres by ids", key = "agbids")
    public List<String> findAllGenresByIds(Set<Long> ids) {
        return genreService.findAllByIds(ids)
            .stream()
            .map(genreConverter::genreToString)
            .toList();
    }

    @ShellMethod(value = "Delete genre by id", key = "gdel")
    public void deleteGenre(long id) {
        genreService.deleteById(id);
    }


    @ShellMethod(value = "Update genre", key = "gupd")
    public String updateGenre(long id, String name) {
        GenreDto genre = genreService.updateGenre(id, name);
        return genreConverter.genreToString(genre);
    }

    @ShellMethod(value = "Upsert genre", key = "gups")
    public String upsertGenre(long id, String name) {
        GenreDto genre = genreService.upsertGenre(id, name);
        return genreConverter.genreToString(genre);
    }

    @ShellMethod(value = "Exists by id", key = "gexst")
    public boolean existsById(long id) {
        boolean existsById = genreService.existsById(id);

        log.info("exists: {} for genre with id {}", id, existsById);
        return existsById;
    }
}
