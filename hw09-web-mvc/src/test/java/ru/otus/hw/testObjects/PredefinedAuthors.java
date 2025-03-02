package ru.otus.hw.testObjects;

import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.model.Author;

import java.util.HashMap;
import java.util.Map;

public class PredefinedAuthors {

    // String constants for author identifiers
    public static final String TOLKIEN_AUTHOR_ID = "TOLKIEN_AUTHOR";
    public static final String PUSHKIN_AUTHOR_ID = "PUSHKIN_AUTHOR";
    public static final String LERMONTOV_AUTHOR_ID = "LERMONTOV_AUTHOR";
    public static final String DOSTOEVSKY_AUTHOR_ID = "DOSTOEVSKY_AUTHOR";
    public static final Author TOLKIEN_AUTHOR = Author.builder()
        .id(1L) // using long number
        .firstName("John")
        .middleName("Ronald")
        .lastName("Tolkien")
        .description("English writer and academic, best known for The Hobbit and The Lord of the Rings.")
        .build();
    public static final Author PUSHKIN_AUTHOR = Author.builder()
        .id(2L) // using long number
        .firstName("Alexander")
        .middleName(null)
        .lastName("Pushkin")
        .description("Russian poet, playwright, and novelist, considered to be the founder of modern Russian literature.")
        .build();
    public static final Author LERMONTOV_AUTHOR = Author.builder()
        .id(3L) // using long number
        .firstName("Mikhail")
        .middleName(null)
        .lastName("Lermontov")
        .description("Russian Romantic writer and figurative artist, known for his poetry and the novel A Hero of Our Time.")
        .build();
    public static final Author DOSTOEVSKY_AUTHOR = Author.builder()
        .id(4L) // using long number
        .firstName("Fyodor")
        .middleName(null)
        .lastName("Dostoevsky")
        .description("Russian novelist famous for works like Crime and Punishment and The Brothers Karamazov.")
        .build();

    public static final class Dtos {

        public static final AuthorDto TOLKIEN_AUTHOR = AuthorDto.builder()
            .id(1L) // using long number
            .firstName("John")
            .middleName("Ronald")
            .lastName("Tolkien")
            .description("English writer and academic, best known for The Hobbit and The Lord of the Rings.")
            .build();

        public static final AuthorDto PUSHKIN_AUTHOR = AuthorDto.builder()
            .id(2L) // using long number
            .firstName("Alexander")
            .middleName(null)
            .lastName("Pushkin")
            .description("Russian poet, playwright, and novelist, considered to be the founder of modern Russian literature.")
            .build();

        public static final AuthorDto LERMONTOV_AUTHOR = AuthorDto.builder()
            .id(3L) // using long number
            .firstName("Mikhail")
            .middleName(null)
            .lastName("Lermontov")
            .description("Russian Romantic writer and figurative artist, known for his poetry and the novel A Hero of Our Time.")
            .build();

        public static final AuthorDto DOSTOEVSKY_AUTHOR = AuthorDto.builder()
            .id(4L) // using long number
            .firstName("Fyodor")
            .middleName(null)
            .lastName("Dostoevsky")
            .description("Russian novelist famous for works like Crime and Punishment and The Brothers Karamazov.")
            .build();

    }

    private static final Map<String, Author> AUTHOR_MODELS_MAP = new HashMap<>(); // Using Long as key

    private static final Map<String, AuthorDto> AUTHOR_DTO_MAP = new HashMap<>(); // Using Long as key

    public static Map<String, Author> getAuthorMap() {
        // Adding predefined authors to the map

        // Adding predefined authors to the map using constant strings as keys
        AUTHOR_MODELS_MAP.put(TOLKIEN_AUTHOR_ID, TOLKIEN_AUTHOR);
        AUTHOR_MODELS_MAP.put(PUSHKIN_AUTHOR_ID, PUSHKIN_AUTHOR);
        AUTHOR_MODELS_MAP.put(LERMONTOV_AUTHOR_ID, LERMONTOV_AUTHOR);
        AUTHOR_MODELS_MAP.put(DOSTOEVSKY_AUTHOR_ID, DOSTOEVSKY_AUTHOR);

        return AUTHOR_MODELS_MAP;
    }

    public static Map<String, AuthorDto> getAuthorDtosMap() {
        // Adding predefined authors to the map

        // Adding predefined authors to the map using constant strings as keys
        AUTHOR_DTO_MAP.put(TOLKIEN_AUTHOR_ID, Dtos.TOLKIEN_AUTHOR);
        AUTHOR_DTO_MAP.put(PUSHKIN_AUTHOR_ID, Dtos.PUSHKIN_AUTHOR);
        AUTHOR_DTO_MAP.put(LERMONTOV_AUTHOR_ID, Dtos.LERMONTOV_AUTHOR);
        AUTHOR_DTO_MAP.put(DOSTOEVSKY_AUTHOR_ID, Dtos.DOSTOEVSKY_AUTHOR);

        return AUTHOR_DTO_MAP;
    }
}
