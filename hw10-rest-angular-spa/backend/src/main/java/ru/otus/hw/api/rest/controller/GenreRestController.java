package ru.otus.hw.api.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.request.SaveGenreRequest;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.service.GenreService;

import java.util.List;

@Tag(name = "Genre rest api", description = "Api for genre operations.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/genres")
public class GenreRestController {
    private final GenreService genreService;

    @Operation(description = "Get genre by id.", summary = "Get genre by id.")
    @GetMapping(value = "/{id}",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
    )
    public GenreDto getById(@PathVariable("id") Long genreId) {
        return genreService.findById(genreId).orElseThrow(
            () -> new EntityNotFoundException("Genre not found: [%s] ".formatted(genreId))
        );
    }

    @Operation(description = "List all genres.", summary = "List all genres.")
    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GenreDto> listGenres() {
        return genreService.findAll();
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(description = "Create new genre.", summary = "Create new genre.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public GenreDto create(@RequestBody SaveGenreRequest saveGenreRequest) {
        GenreDto theGenre = GenreDto.builder()
            .name(saveGenreRequest.getName())
            .description(saveGenreRequest.getDescription())
            .build();

        return genreService.save(theGenre);
    }

    @Operation(description = "Update genre.", summary = "Update genre.")
    @PutMapping(value = "/{genreId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public GenreDto update(@PathVariable("genreId") Long genreId) {
        GenreDto genreDto = genreService.findById(genreId)
            .orElseThrow(() -> new EntityNotFoundException("Genre not found: [%s]"
                .formatted(genreId)));

        return genreService.save(genreDto);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(description = "Delete genre.", summary = "Delete genre.")
    @DeleteMapping("/{genreId}")
    public void delete(@PathVariable("genreId") Long theId) {
        genreService.deleteById(theId);
    }
}