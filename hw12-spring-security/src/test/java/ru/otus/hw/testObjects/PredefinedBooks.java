package ru.otus.hw.testObjects;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.otus.hw.testObjects.PredefinedAuthors.DOSTOEVSKY_AUTHOR;
import static ru.otus.hw.testObjects.PredefinedAuthors.LERMONTOV_AUTHOR;
import static ru.otus.hw.testObjects.PredefinedAuthors.PUSHKIN_AUTHOR;
import static ru.otus.hw.testObjects.PredefinedAuthors.TOLKIEN_AUTHOR;
import static ru.otus.hw.testObjects.PredefinedGenres.ADVENTURE_GENRE;
import static ru.otus.hw.testObjects.PredefinedGenres.CLASSIC_LITERATURE_GENRE;
import static ru.otus.hw.testObjects.PredefinedGenres.FANTASY_GENRE;
import static ru.otus.hw.testObjects.PredefinedGenres.FICTION_GENRE;
import static ru.otus.hw.testObjects.PredefinedGenres.POETRY_GENRE;

public class PredefinedBooks {

    // String constants for book identifiers
    public static final String HOBBIT_ID = "HOBBIT";
    public static final String LORD_FELLOWSHIP_ID = "THE_LORD_OF_THE_RINGS_FELLOWSHIP";
    public static final String LORD_TWO_TOWERS_ID = "THE_LORD_OF_THE_RINGS_TWO_TOWERS";
    public static final String LORD_RETURN_OF_THE_KING_ID = "THE_LORD_OF_THE_RINGS_RETURN_OF_THE_KING";
    public static final String EUGENE_ONEGIN_ID = "EUGENE_ONEGIN";
    public static final String HERO_OF_OUR_TIME_ID = "HERO_OF_OUR_TIME";
    public static final String FATALIST_ID = "FATALIST";
    public static final String CRIME_AND_PUNISHMENT_ID = "CRIME_AND_PUNISHMENT";
    public static final String BROTHERS_KARAMAZOV_ID = "BROTHERS_KARAMAZOV";

    // Book objects
    public static final Book HOBBIT = Book.builder()
        .id(1L)
        .title("The Hobbit")
        .description("A fantasy novel about the adventures of a hobbit named Bilbo Baggins, who is drawn into an epic quest by a group of dwarves and the wizard Gandalf.")
        .serialNumber("SN001")
        .isbn("978-0-618-26030-0")
        .genres(Set.of(FANTASY_GENRE, FICTION_GENRE, ADVENTURE_GENRE))
        .author(TOLKIEN_AUTHOR) // Author ID for J.R.R. Tolkien
        .build();

    public static final Book LORD_FELLOWSHIP = Book.builder()
        .id(2L)
        .title("The Lord of the Rings: The Fellowship of the Ring")
        .description("The first part of the epic trilogy of Middle-earth, where a group of heroes embark on a quest to destroy the One Ring.")
        .serialNumber("SN002")
        .isbn("978-0-618-32970-8")
        .author(TOLKIEN_AUTHOR)
        .genres(Set.of(FANTASY_GENRE, FICTION_GENRE, ADVENTURE_GENRE))
        .build();

    public static final Book LORD_TWO_TOWERS = Book.builder()
        .id(3L)
        .title("The Lord of the Rings: The Two Towers")
        .description("The continuation of the epic journey of the fellowship as they face new challenges and enemies.")
        .serialNumber("SN003")
        .isbn("978-0-618-32972-2")
        .author(TOLKIEN_AUTHOR)
        .genres(Set.of(FANTASY_GENRE, FICTION_GENRE, ADVENTURE_GENRE))
        .build();

    public static final Book LORD_RETURN_OF_THE_KING = Book.builder()
        .id(4L)
        .title("The Lord of the Rings: The Return of the King")
        .description("The final book of the trilogy where the battle for Middle-earth culminates.")
        .serialNumber("SN004")
        .isbn("978-0-618-32973-9")
        .author(TOLKIEN_AUTHOR)
        .genres(Set.of(FANTASY_GENRE, FICTION_GENRE, ADVENTURE_GENRE))
        .build();

    public static final Book EUGENE_ONEGIN = Book.builder()
        .id(5L)
        .title("Eugene Onegin")
        .description("A novel in verse, telling the story of a young nobleman and his interaction with the world around him, including love and loss.")
        .serialNumber("SN005")
        .isbn("978-0-14-044499-5")
        .author(PUSHKIN_AUTHOR)
        .genres(Set.of(CLASSIC_LITERATURE_GENRE, POETRY_GENRE))

        .build();

    public static final Book HERO_OF_OUR_TIME = Book.builder()
        .id(6L)
        .title("A Hero of Our Time")
        .description("A novel that explores the life of a disillusioned Russian officer and reflects on the nature of the Russian soul.")
        .serialNumber("SN007")
        .isbn("978-0-14-044240-3")
        .author(LERMONTOV_AUTHOR)
        .genres(Set.of(CLASSIC_LITERATURE_GENRE, POETRY_GENRE))

        .build();

    public static final Book FATALIST = Book.builder()
        .id(7L)
        .title("Fatalist")
        .description("A story portraying the inner conflicts and explorations of characters facing unlikely situations, linked to fate and free will.")
        .serialNumber("SN008")
        .isbn("978-0-14-044533-6")
        .genres(Set.of(CLASSIC_LITERATURE_GENRE))
        .author(LERMONTOV_AUTHOR)
        .build();

    public static final Book CRIME_AND_PUNISHMENT = Book.builder()
        .id(8L)
        .title("Crime and Punishment")
        .description("A novel that tells the story of Raskolnikov, a former student who commits a murder and struggles with guilt and redemption.")
        .serialNumber("SN009")
        .isbn("978-0-14-305814-9")
        .author(DOSTOEVSKY_AUTHOR)
        .genres(Set.of(CLASSIC_LITERATURE_GENRE, PredefinedGenres.PSYCHOLOGICAL_FICTION_GENRE))

        .build();

    public static final Book BROTHERS_KARAMAZOV = Book.builder()
        .id(9L)
        .title("The Brothers Karamazov")
        .description("A philosophical novel that addresses deep moral questions through the lives and conflicts of the Karamazov family.")
        .serialNumber("SN010")
        .isbn("978-0-14-303911-6")
        .author(DOSTOEVSKY_AUTHOR)
        .genres(Set.of(CLASSIC_LITERATURE_GENRE, PredefinedGenres.PSYCHOLOGICAL_FICTION_GENRE))
        .build();

    private static final Map<String, Book> BOOKS_MAP = new HashMap<>(); // Using string constants as keys
    private static final Map<String, BookDto> BOOKS_DTO_MAP = new HashMap<>(); // Using string constants as keys

    public static Map<String, Book> getBooksMap() {
        // Adding predefined books to the map using constant strings
        BOOKS_MAP.put(PredefinedBooks.HOBBIT_ID, PredefinedBooks.HOBBIT);
        BOOKS_MAP.put(PredefinedBooks.LORD_FELLOWSHIP_ID, PredefinedBooks.LORD_FELLOWSHIP);
        BOOKS_MAP.put(PredefinedBooks.LORD_TWO_TOWERS_ID, PredefinedBooks.LORD_TWO_TOWERS);
        BOOKS_MAP.put(PredefinedBooks.LORD_RETURN_OF_THE_KING_ID, PredefinedBooks.LORD_RETURN_OF_THE_KING);
        BOOKS_MAP.put(PredefinedBooks.EUGENE_ONEGIN_ID, PredefinedBooks.EUGENE_ONEGIN);
        BOOKS_MAP.put(PredefinedBooks.HERO_OF_OUR_TIME_ID, PredefinedBooks.HERO_OF_OUR_TIME);
        BOOKS_MAP.put(PredefinedBooks.FATALIST_ID, PredefinedBooks.FATALIST);
        BOOKS_MAP.put(PredefinedBooks.CRIME_AND_PUNISHMENT_ID, PredefinedBooks.CRIME_AND_PUNISHMENT);
        BOOKS_MAP.put(PredefinedBooks.BROTHERS_KARAMAZOV_ID, PredefinedBooks.BROTHERS_KARAMAZOV);

        return BOOKS_MAP;
    }

    public static Map<Long, Book> getIdToBookMapping() {

        return PredefinedBooks.getBooksMap()
            .values().stream().collect(Collectors.toMap(Book::getId, it -> it));
    }

    public static Map<String, BookDto> getBookDtoMap() {
        // Adding predefined books to the map using constant strings
        BOOKS_DTO_MAP.put(PredefinedBooks.HOBBIT_ID, Dtos.HOBBIT);
        BOOKS_DTO_MAP.put(PredefinedBooks.LORD_FELLOWSHIP_ID, Dtos.LORD_FELLOWSHIP);
        BOOKS_DTO_MAP.put(PredefinedBooks.LORD_TWO_TOWERS_ID, Dtos.LORD_TWO_TOWERS);
        BOOKS_DTO_MAP.put(PredefinedBooks.LORD_RETURN_OF_THE_KING_ID, Dtos.LORD_RETURN_OF_THE_KING);
        BOOKS_DTO_MAP.put(PredefinedBooks.EUGENE_ONEGIN_ID, Dtos.EUGENE_ONEGIN);
        BOOKS_DTO_MAP.put(PredefinedBooks.HERO_OF_OUR_TIME_ID, Dtos.HERO_OF_OUR_TIME);
        BOOKS_DTO_MAP.put(PredefinedBooks.FATALIST_ID, Dtos.FATALIST);
        BOOKS_DTO_MAP.put(PredefinedBooks.CRIME_AND_PUNISHMENT_ID, Dtos.CRIME_AND_PUNISHMENT);
        BOOKS_DTO_MAP.put(PredefinedBooks.BROTHERS_KARAMAZOV_ID, Dtos.BROTHERS_KARAMAZOV);

        return BOOKS_DTO_MAP;
    }


    public static Map<Long, BookDto> getIdToBookDtoMapping() {

        return PredefinedBooks.getBookDtoMap()
            .values().stream().collect(Collectors.toMap(BookDto::getId, it -> it));
    }

    public static final class Dtos {
        public static final BookDto HOBBIT = BookDto.builder()
            .id(1L)
            .title("The Hobbit")
            .description("A fantasy novel about the adventures of a hobbit named Bilbo Baggins, who is drawn into an epic quest by a group of dwarves and the wizard Gandalf.")
            .serialNumber("SN001")
            .isbn("978-0-618-26030-0")
            .genres(Set.of(PredefinedGenres.Dtos.FANTASY_GENRE, PredefinedGenres.Dtos.FICTION_GENRE,
                PredefinedGenres.Dtos.ADVENTURE_GENRE))
            .author(PredefinedAuthors.Dtos.TOLKIEN_AUTHOR) // Author ID for J.R.R. Tolkien
            .build();

        public static final BookDto LORD_FELLOWSHIP = BookDto.builder()
            .id(2L)
            .title("The Lord of the Rings: The Fellowship of the Ring")
            .description("The first part of the epic trilogy of Middle-earth, where a group of heroes embark on a quest to destroy the One Ring.")
            .serialNumber("SN002")
            .isbn("978-0-618-32970-8")
            .author(PredefinedAuthors.Dtos.TOLKIEN_AUTHOR)
            .genres(Set.of(PredefinedGenres.Dtos.FANTASY_GENRE, PredefinedGenres.Dtos.FICTION_GENRE, PredefinedGenres.Dtos.ADVENTURE_GENRE))
            .build();

        public static final BookDto LORD_TWO_TOWERS = BookDto.builder()
            .id(3L)
            .title("The Lord of the Rings: The Two Towers")
            .description("The continuation of the epic journey of the fellowship as they face new challenges and enemies.")
            .serialNumber("SN003")
            .isbn("978-0-618-32972-2")
            .author(PredefinedAuthors.Dtos.TOLKIEN_AUTHOR)
            .genres(Set.of(PredefinedGenres.Dtos.FANTASY_GENRE, PredefinedGenres.Dtos.FICTION_GENRE, PredefinedGenres.Dtos.ADVENTURE_GENRE))
            .build();

        public static final BookDto LORD_RETURN_OF_THE_KING = BookDto.builder()
            .id(4L)
            .title("The Lord of the Rings: The Return of the King")
            .description("The final book of the trilogy where the battle for Middle-earth culminates.")
            .serialNumber("SN004")
            .isbn("978-0-618-32973-9")
            .author(PredefinedAuthors.Dtos.TOLKIEN_AUTHOR)
            .genres(Set.of(PredefinedGenres.Dtos.FANTASY_GENRE, PredefinedGenres.Dtos.FICTION_GENRE, PredefinedGenres.Dtos.ADVENTURE_GENRE))
            .build();

        public static final BookDto EUGENE_ONEGIN = BookDto.builder()
            .id(5L)
            .title("Eugene Onegin")
            .description("A novel in verse, telling the story of a young nobleman and his interaction with the world around him, including love and loss.")
            .serialNumber("SN005")
            .isbn("978-0-14-044499-5")
            .author(PredefinedAuthors.Dtos.PUSHKIN_AUTHOR)
            .genres(Set.of(PredefinedGenres.Dtos.CLASSIC_LITERATURE_GENRE, PredefinedGenres.Dtos.POETRY_GENRE))

            .build();

        public static final BookDto HERO_OF_OUR_TIME = BookDto.builder()
            .id(6L)
            .title("A Hero of Our Time")
            .description("A novel that explores the life of a disillusioned Russian officer and reflects on the nature of the Russian soul.")
            .serialNumber("SN007")
            .isbn("978-0-14-044240-3")
            .author(PredefinedAuthors.Dtos.LERMONTOV_AUTHOR)
            .genres(Set.of(PredefinedGenres.Dtos.CLASSIC_LITERATURE_GENRE, PredefinedGenres.Dtos.POETRY_GENRE))

            .build();

        public static final BookDto FATALIST = BookDto.builder()
            .id(7L)
            .title("Fatalist")
            .description("A story portraying the inner conflicts and explorations of characters facing unlikely situations, linked to fate and free will.")
            .serialNumber("SN008")
            .isbn("978-0-14-044533-6")
            .genres(Set.of(PredefinedGenres.Dtos.CLASSIC_LITERATURE_GENRE))
            .author(PredefinedAuthors.Dtos.LERMONTOV_AUTHOR)
            .build();

        public static final BookDto CRIME_AND_PUNISHMENT = BookDto.builder()
            .id(8L)
            .title("Crime and Punishment")
            .description("A novel that tells the story of Raskolnikov, a former student who commits a murder and struggles with guilt and redemption.")
            .serialNumber("SN009")
            .isbn("978-0-14-305814-9")
            .author(PredefinedAuthors.Dtos.DOSTOEVSKY_AUTHOR)
            .genres(Set.of(PredefinedGenres.Dtos.CLASSIC_LITERATURE_GENRE, PredefinedGenres.Dtos.PSYCHOLOGICAL_FICTION_GENRE))

            .build();

        public static final BookDto BROTHERS_KARAMAZOV = BookDto.builder()
            .id(9L)
            .title("The Brothers Karamazov")
            .description("A philosophical novel that addresses deep moral questions through the lives and conflicts of the Karamazov family.")
            .serialNumber("SN010")
            .isbn("978-0-14-303911-6")
            .author(PredefinedAuthors.Dtos.DOSTOEVSKY_AUTHOR)
            .genres(Set.of(PredefinedGenres.Dtos.CLASSIC_LITERATURE_GENRE, PredefinedGenres.Dtos.PSYCHOLOGICAL_FICTION_GENRE))
            .build();
    }
}
