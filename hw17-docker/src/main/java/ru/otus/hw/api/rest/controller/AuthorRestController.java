package ru.otus.hw.api.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.request.SaveAuthorRequest;
import ru.otus.hw.service.AuthorService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

@Tag(name = "Author rest api", description = "Api for author operations.")
@RestController
@RequestMapping(value = "/api/authors",
    produces = {APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
)
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorService authorService;

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'EDITOR', 'READER') or hasAuthority('SCOPE_authors:read')")
    @Operation(summary = "List authors", description = "List authors.")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<AuthorDto> listAuthors() {
        return authorService.findAll();
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'EDITOR', 'READER') or hasAuthority('SCOPE_authors:read')")
    @Operation(summary = "Get author by id.", description = "Get author by id.")
    @GetMapping(value = "/{authorId}",
    produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE}
    )
    public AuthorDto getById(@PathVariable Long authorId) {
        return authorService.findById(authorId);
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'EDITOR') or hasAuthority('SCOPE_authors:write')")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Create author.", description = "Create author.")
    @PostMapping(
        produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE},
        consumes = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE}
    )
    public AuthorDto create(@Valid @RequestBody SaveAuthorRequest createAuthorRequest) {
        AuthorDto authorDto = convertToAuthorDto(null, createAuthorRequest);
        return authorService.save(authorDto);
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'EDITOR') or hasAuthority('SCOPE_authors:write')")
    @Operation(summary = "Update author by id.", description = "Update author by id.")
    @PutMapping(value = "/{authorId}",
        produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE},
        consumes = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE}
    )
    public AuthorDto updateAuthor(@PathVariable @NotNull Long authorId,
                                  @Valid @RequestBody SaveAuthorRequest createAuthorRequest) {
        AuthorDto authorDto = convertToAuthorDto(authorId, createAuthorRequest);

        return authorService.save(authorDto);
    }

    @PreAuthorize(value = "hasRole('ADMIN') or hasAuthority('SCOPE_authors:write')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete author by id.", description = "Delete author by id.")
    @DeleteMapping("/{authorId}")
    public void delete(@PathVariable("authorId") Long theId) {
        authorService.deleteById(theId);
    }

    private AuthorDto convertToAuthorDto(Long authorId, SaveAuthorRequest source) {

        return AuthorDto.builder()
            .description(source.getDescription())
            .middleName(source.getMiddleName())
            .lastName(source.getLastName())
            .firstName(source.getFirstName())
            .id(authorId)
            .build();
    }
}
