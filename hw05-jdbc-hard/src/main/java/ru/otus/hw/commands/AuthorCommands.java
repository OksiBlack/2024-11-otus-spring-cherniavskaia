package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@ShellComponent
public class AuthorCommands {

    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

    @ShellMethod(value = "Find all authors", key = "aa")
    public String findAllAuthors() {
        return authorService.findAll().stream()
            .map(authorConverter::authorToString)
            .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Insert author", key = "ains")
    public String insertAuthor(String fullName) {
        var author = authorService.insert(fullName);
        return authorConverter.authorToString(author);
    }

    @ShellMethod(value = "Update author", key = "aupd")
    public String updateAuthor(long id, String fullName) {
        Author author = authorService.updateAuthor(id, fullName);
        return authorConverter.authorToString(author);
    }

    @ShellMethod(value = "Upsert author", key = "aups")
    public String upsertAuthor(long id, String fullName) {
        Author author = authorService.upsert(id, fullName);
        return authorConverter.authorToString(author);
    }

    @ShellMethod(value = "Find author by id", key = "abid")
    public String findAuthorById(long id) {
        return authorService.findById(id)
            .map(authorConverter::authorToString)
            .orElse("Author with id %d not found".formatted(id));
    }

    @ShellMethod(value = "Find all authors by ids", key = "aabids")
    public List<String> findAllAuthorsByIds(Set<Long> ids) {
        return authorService.findAllByIds(ids)
            .stream()
            .map(authorConverter::authorToString)
            .toList();
    }

    @ShellMethod(value = "Exists by id", key = "aexst")
    public boolean existsById(long id) {
        boolean existsById = authorService.existsById(id);

        log.info("exists: {} for author with id {}", id, existsById);
        return existsById;
    }

    @ShellMethod(value = "Delete author by id", key = "adel")
    public void deleteAuthor(long id) {
        log.info("Deleting author with id {}", id);

        authorService.deleteById(id);
    }
}
