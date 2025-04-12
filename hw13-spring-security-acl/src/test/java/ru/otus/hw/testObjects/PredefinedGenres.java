package ru.otus.hw.testObjects;

import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Genre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredefinedGenres {

    // String constants for genre identifiers
    public static final String FANTASY_GENRE_ID = "FANTASY_GENRE";
    public static final String FICTION_GENRE_ID = "FICTION_GENRE";
    public static final String ADVENTURE_GENRE_ID = "ADVENTURE_GENRE";
    public static final String SCIENCE_FICTION_GENRE_ID = "SCIENCE_FICTION_GENRE";
    public static final String CLASSIC_LITERATURE_GENRE_ID = "CLASSIC_LITERATURE_GENRE";
    public static final String POETRY_GENRE_ID = "POETRY_GENRE";
    public static final String PSYCHOLOGICAL_FICTION_GENRE_ID = "PSYCHOLOGICAL_FICTION_GENRE";
    public static final String NON_FICTION_GENRE_ID = "NON_FICTION_GENRE";
    public static final String MYSTERY_GENRE_ID = "MYSTERY_GENRE";
    public static final String ROMANCE_GENRE_ID = "ROMANCE_GENRE";
    public static final String THRILLER_GENRE_ID = "THRILLER_GENRE";
    public static final String BIOGRAPHY_GENRE_ID = "BIOGRAPHY_GENRE";

    // Genre objects
    public static final Genre FANTASY_GENRE = Genre.builder()
        .id(1L)
        .name("Fantasy")
        .description("A genre that uses magic and other supernatural elements as a primary plot element, theme, or setting.")
        .build();

    public static final Genre FICTION_GENRE = Genre.builder()
        .id(2L)
        .name("Fiction")
        .description("A genre that includes imaginative narratives, novels, and stories.")
        .build();

    public static final Genre ADVENTURE_GENRE = Genre.builder()
        .id(3L)
        .name("Adventure")
        .description("A genre focusing on exciting journeys and exploits, often featuring quests and heroic struggles.")
        .build();

    public static final Genre SCIENCE_FICTION_GENRE = Genre.builder()
        .id(4L)
        .name("Science Fiction")
        .description("A genre that explores futuristic concepts, advanced science, and technological innovations.")
        .build();

    public static final Genre CLASSIC_LITERATURE_GENRE = Genre.builder()
        .id(5L)
        .name("Classic Literature")
        .description("Timeless literary works that have been acknowledged for their cultural, historical, and artistic significance.")
        .build();

    public static final Genre POETRY_GENRE = Genre.builder()
        .id(6L)
        .name("Poetry")
        .description("A literary genre that uses aesthetic and rhythmic qualities of language to evoke meanings in addition to, or in place of, the prosaic ostensible meaning.")
        .build();


    public static final Genre MYSTERY_GENRE = Genre.builder()
        .id(7L)
        .name("Mystery")
        .description("A genre that involves solving a crime or uncovering secrets.")
        .build();

    public static final Genre ROMANCE_GENRE = Genre.builder()
        .id(8L)
        .name("Romance")
        .description("A genre that focuses on romantic relationships and love stories.")
        .build();


    public static final Genre THRILLER_GENRE = Genre.builder()
        .id(9L)
        .name("Thriller")
        .description("A genre that features exciting plots with suspenseful or high-stakes situations.")
        .build();

    public static final Genre BIOGRAPHY_GENRE = Genre.builder()
        .id(10L)
        .name("Biography")
        .description("A non-fiction genre that tells the life story of an individual.")
        .build();

    private static final Map<String, Genre> GENRE_MAP = new HashMap<>(); // Using Long as key
    private static final Map<String, GenreDto> GENRE_DTO_MAP = new HashMap<>(); // Using Long as key


    public static Map<String, Genre> getGenreMap() {

        // Adding predefined genres to the map using constant strings
        GENRE_MAP.put(PredefinedGenres.FANTASY_GENRE_ID, PredefinedGenres.FANTASY_GENRE);
        GENRE_MAP.put(PredefinedGenres.FICTION_GENRE_ID, PredefinedGenres.FICTION_GENRE);
        GENRE_MAP.put(PredefinedGenres.ADVENTURE_GENRE_ID, PredefinedGenres.ADVENTURE_GENRE);
        GENRE_MAP.put(PredefinedGenres.SCIENCE_FICTION_GENRE_ID, PredefinedGenres.SCIENCE_FICTION_GENRE);
        GENRE_MAP.put(PredefinedGenres.CLASSIC_LITERATURE_GENRE_ID, PredefinedGenres.CLASSIC_LITERATURE_GENRE);
        GENRE_MAP.put(PredefinedGenres.POETRY_GENRE_ID, PredefinedGenres.POETRY_GENRE);
        GENRE_MAP.put(PredefinedGenres.MYSTERY_GENRE_ID, PredefinedGenres.MYSTERY_GENRE);
        GENRE_MAP.put(PredefinedGenres.ROMANCE_GENRE_ID, PredefinedGenres.ROMANCE_GENRE);
        GENRE_MAP.put(PredefinedGenres.THRILLER_GENRE_ID, PredefinedGenres.THRILLER_GENRE);
        GENRE_MAP.put(PredefinedGenres.BIOGRAPHY_GENRE_ID, PredefinedGenres.BIOGRAPHY_GENRE);

        return GENRE_MAP;
    }

    public static Map<String, GenreDto> getGenreDtoMap() {

        // Adding predefined genres to the map using constant strings
        GENRE_DTO_MAP.put(PredefinedGenres.FANTASY_GENRE_ID, Dtos.FANTASY_GENRE);
        GENRE_DTO_MAP.put(PredefinedGenres.FICTION_GENRE_ID, Dtos.FICTION_GENRE);
        GENRE_DTO_MAP.put(PredefinedGenres.ADVENTURE_GENRE_ID, Dtos.ADVENTURE_GENRE);
        GENRE_DTO_MAP.put(PredefinedGenres.SCIENCE_FICTION_GENRE_ID, Dtos.SCIENCE_FICTION_GENRE);
        GENRE_DTO_MAP.put(PredefinedGenres.CLASSIC_LITERATURE_GENRE_ID, Dtos.CLASSIC_LITERATURE_GENRE);
        GENRE_DTO_MAP.put(PredefinedGenres.POETRY_GENRE_ID, Dtos.POETRY_GENRE);
//        GENRE_DTO_MAP.put(PredefinedGenres.PSYCHOLOGICAL_FICTION_GENRE_ID, Dtos.PSYCHOLOGICAL_FICTION_GENRE);
//        GENRE_DTO_MAP.put(PredefinedGenres.NON_FICTION_GENRE_ID, Dtos.NON_FICTION_GENRE);
        GENRE_DTO_MAP.put(PredefinedGenres.MYSTERY_GENRE_ID, Dtos.MYSTERY_GENRE);
        GENRE_DTO_MAP.put(PredefinedGenres.ROMANCE_GENRE_ID, Dtos.ROMANCE_GENRE);
        GENRE_DTO_MAP.put(PredefinedGenres.THRILLER_GENRE_ID, Dtos.THRILLER_GENRE);
        GENRE_DTO_MAP.put(PredefinedGenres.BIOGRAPHY_GENRE_ID, Dtos.BIOGRAPHY_GENRE);

        return GENRE_DTO_MAP;
    }

    public static final class Dtos {

        // Genre objects
        public static final GenreDto FANTASY_GENRE = GenreDto.builder()
            .id(1L)
            .name("Fantasy")
            .description("A genre that uses magic and other supernatural elements as a primary plot element, theme, or setting.")
            .build();

        public static final GenreDto FICTION_GENRE = GenreDto.builder()
            .id(2L)
            .name("Fiction")
            .description("A genre that includes imaginative narratives, novels, and stories.")
            .build();

        public static final GenreDto ADVENTURE_GENRE = GenreDto.builder()
            .id(3L)
            .name("Adventure")
            .description("A genre focusing on exciting journeys and exploits, often featuring quests and heroic struggles.")
            .build();

        public static final GenreDto SCIENCE_FICTION_GENRE = GenreDto.builder()
            .id(4L)
            .name("Science Fiction")
            .description("A genre that explores futuristic concepts, advanced science, and technological innovations.")
            .build();

        public static final GenreDto CLASSIC_LITERATURE_GENRE = GenreDto.builder()
            .id(5L)
            .name("Classic Literature")
            .description("Timeless literary works that have been acknowledged for their cultural, historical, and artistic significance.")
            .build();

        public static final GenreDto POETRY_GENRE = GenreDto.builder()
            .id(6L)
            .name("Poetry")
            .description("A literary genre that uses aesthetic and rhythmic qualities of language to evoke meanings in addition to, or in place of, the prosaic ostensible meaning.")
            .build();

        public static final GenreDto PSYCHOLOGICAL_FICTION_GENRE = GenreDto.builder()
            .id(7L)
            .name("Psychological Fiction")
            .description("A genre that emphasizes the inner thoughts and emotional states of its characters.")
            .build();


        public static final GenreDto MYSTERY_GENRE = GenreDto.builder()
            .id(8L)
            .name("Mystery")
            .description("A genre that involves solving a crime or uncovering secrets.")
            .build();

        public static final GenreDto ROMANCE_GENRE = GenreDto.builder()
            .id(9L)
            .name("Romance")
            .description("A genre that focuses on romantic relationships and love stories.")
            .build();

        public static final GenreDto THRILLER_GENRE = GenreDto.builder()
            .id(10L)
            .name("Thriller")
            .description("A genre that features exciting plots with suspenseful or high-stakes situations.")
            .build();

        public static final GenreDto BIOGRAPHY_GENRE = GenreDto.builder()
            .id(11L)
            .name("Biography")
            .description("A non-fiction genre that tells the life story of an individual.")
            .build();


    }

}
