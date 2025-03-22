package ru.otus.hw.testObjects;

import ru.otus.hw.model.Comment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.otus.hw.testObjects.PredefinedBooks.BROTHERS_KARAMAZOV;
import static ru.otus.hw.testObjects.PredefinedBooks.CRIME_AND_PUNISHMENT;
import static ru.otus.hw.testObjects.PredefinedBooks.EUGENE_ONEGIN_ID;
import static ru.otus.hw.testObjects.PredefinedBooks.FATALIST;
import static ru.otus.hw.testObjects.PredefinedBooks.HERO_OF_OUR_TIME;
import static ru.otus.hw.testObjects.PredefinedBooks.HOBBIT;

public class PredefinedComments {

    public static Map<Long, List<Comment>> getBookCommentMap() {
        return getBookComments().stream().collect(Collectors.groupingBy(comment -> comment.getBook().getId()));
    }

    public static List<Comment> getBookComments() {

        List<Comment> comments = new ArrayList<>();

        comments.add(Comment.builder()
            .id(1L)
            .book(HOBBIT)
            .text("Comment_1_1")
            .created(LocalDate.parse("2024-01-01"))
            .author("Author_1")
            .build());

        comments.add(Comment.builder()
            .id(2L)
            .book(PredefinedBooks.LORD_FELLOWSHIP)
            .text("Comment_2")
            .created(LocalDate.parse("2024-01-02"))
            .author("Author_2")
            .build());

        comments.add(Comment.builder()
            .id(3L)
            .book(PredefinedBooks.LORD_TWO_TOWERS)
            .text("Comment_3")
            .created(LocalDate.parse("2024-01-03"))
            .author("Author_3")
            .build());

        comments.add(Comment.builder()
            .id(4L)
            .book(HOBBIT)
            .text("Comment_1_2")
            .created(LocalDate.parse("2024-01-04"))
            .author("Author_1")
            .build());

        comments.add(Comment.builder()
            .id(5L)
            .book(HOBBIT)
            .text("Comment_1_3")
            .created(LocalDate.parse("2024-01-04"))
            .author("Author_2")
            .build());

        comments.add(Comment.builder()
            .id(6L)
            .book(HOBBIT)
            .text("An absolutely fascinating journey through Middle-earth!")
            .created(LocalDate.parse("2023-01-15"))
            .author("John Doe")
            .build());

        comments.add(Comment.builder()
            .id(7L)
            .book(HOBBIT)
            .text("This book inspired me to explore the world of fantasy literature.")
            .created(LocalDate.parse("2023-01-16"))
            .author("Alice Smith")
            .build());

        comments.add(Comment.builder()
            .id(8L)
            .book(PredefinedBooks.LORD_FELLOWSHIP)
            .text("The storytelling is immersive; J.R.R. Tolkien is a genius.")
            .created(LocalDate.parse("2023-02-10"))
            .author("Jane Doe")
            .build());

        comments.add(Comment.builder()
            .id(9L)
            .book(PredefinedBooks.LORD_FELLOWSHIP)
            .text("I loved the character development in this book.")
            .created(LocalDate.parse("2023-02-11"))
            .author("Bob Johnson")
            .build());

        comments.add(Comment.builder()
            .id(10L)
            .book(PredefinedBooks.LORD_TWO_TOWERS)
            .text("The plot twists kept me on the edge of my seat.")
            .created(LocalDate.parse("2023-03-05"))
            .author("Emily Davis")
            .build());

        comments.add(Comment.builder()
            .id(11L)
            .book(PredefinedBooks.LORD_RETURN_OF_THE_KING)
            .text("A powerful conclusion to an epic series!")
            .created(LocalDate.parse("2023-03-06"))
            .author("Mark Wilson")
            .build());

        comments.add(Comment.builder()
            .id(12L)
            .book(PredefinedBooks.EUGENE_ONEGIN)
            .text("Pushkin's writing style is poetic and captivating.")
            .created(LocalDate.parse("2023-02-20"))
            .author("Lucy Brown")
            .build());

        comments.add(Comment.builder()
            .id(13L)
            .book(PredefinedBooks.HERO_OF_OUR_TIME)
            .text("A deep exploration of the human psyche and Russian society.")
            .created(LocalDate.parse("2023-03-10"))
            .author("James Lee")
            .build());

        comments.add(Comment.builder()
            .id(14L)
            .book(PredefinedBooks.HERO_OF_OUR_TIME)
            .text("Thought-provoking and beautifully written.")
            .created(LocalDate.parse("2023-04-01"))
            .author("Sarah Miller")
            .build());

        comments.add(Comment.builder()
            .id(15L)
            .book(PredefinedBooks.CRIME_AND_PUNISHMENT)
            .text("A gripping tale of guilt and redemption.")
            .created(LocalDate.parse("2023-04-02"))
            .author("Paul Martinez")
            .build());

        comments.add(Comment.builder()
            .id(16L)
            .book(PredefinedBooks.BROTHERS_KARAMAZOV)
            .text("Dostoevsky tackles profound themes with such depth.")
            .created(LocalDate.parse("2023-04-03"))
            .author("Laura Thompson")
            .build());
        return comments;
    }

    public static Map<String, List<Comment>> getSampleComments() {
        Map<String, List<Comment>> commentsMap = new HashMap<>();

        // Initialize lists for comments for each book
        List<Comment> hobbitComments = new ArrayList<>();
        hobbitComments.add(Comment.builder()
            .id(1L) // Using Long type
            .book(PredefinedBooks.HOBBIT) // The Hobbit
            .text("An absolutely fascinating journey through Middle-earth!")
            .created(LocalDate.parse("2023-01-15"))
            .author("John Doe")
            .build());

        hobbitComments.add(Comment.builder()
            .id(2L) // Using Long type
            .book(PredefinedBooks.HOBBIT) // The Hobbit
            .text("This book inspired me to explore the world of fantasy literature.")
            .created(LocalDate.parse("2023-01-16"))
            .author("Alice Smith")
            .build());

        // Add Hobbit comments to the map
        commentsMap.put(PredefinedBooks.HOBBIT_ID, hobbitComments);

        // Initialize lists for comments for The Fellowship of the Ring
        List<Comment> fellowshipComments = new ArrayList<>();
        fellowshipComments.add(Comment.builder()
            .id(3L) // Using Long type
            .book(PredefinedBooks.LORD_FELLOWSHIP) // The Lord of the Rings: The Fellowship of the Ring
            .text("The storytelling is immersive; J.R.R. Tolkien is a genius.")
            .created(LocalDate.parse("2023-02-10"))
            .author("Jane Doe")
            .build());

        fellowshipComments.add(Comment.builder()
            .id(4L) // Using Long type
            .book(PredefinedBooks.LORD_FELLOWSHIP) // The Lord of the Rings: The Fellowship of the Ring
            .text("I loved the character development in this book.")
            .created(LocalDate.parse("2023-02-11"))
            .author("Bob Johnson")
            .build());

        // Add Fellowship comments to the map
        commentsMap.put(PredefinedBooks.LORD_FELLOWSHIP_ID, fellowshipComments);

        // Initialize lists for comments for The Two Towers
        List<Comment> twoTowersComments = new ArrayList<>();
        twoTowersComments.add(Comment.builder()
            .id(5L) // Using Long type
            .book(PredefinedBooks.LORD_TWO_TOWERS) // The Lord of the Rings: The Two Towers
            .text("The plot twists kept me on the edge of my seat.")
            .created(LocalDate.parse("2023-03-05"))
            .author("Emily Davis")
            .build());

        // Add Two Towers comments to the map
        commentsMap.put(PredefinedBooks.LORD_TWO_TOWERS_ID, twoTowersComments);

        // Initialize lists for comments for The Return of the King
        List<Comment> returnOfTheKingComments = new ArrayList<>();
        returnOfTheKingComments.add(Comment.builder()
            .id(6L) // Using Long type
            .book(PredefinedBooks.LORD_RETURN_OF_THE_KING) // The Lord of the Rings: The Return of the King
            .text("A powerful conclusion to an epic series!")
            .created(LocalDate.parse("2023-03-06"))
            .author("Mark Wilson")
            .build());

        // Add Return of the King comments to the map
        commentsMap.put(PredefinedBooks.LORD_RETURN_OF_THE_KING_ID, returnOfTheKingComments);

        // Initialize lists for comments for Eugene Onegin
        List<Comment> eugeneOneginComments = new ArrayList<>();
        eugeneOneginComments.add(Comment.builder()
            .id(7L) // Eugene Onegin
            .book(PredefinedBooks.EUGENE_ONEGIN)
            .text("Pushkin's writing style is poetic and captivating.")
            .created(LocalDate.parse("2023-02-20"))
            .author("Lucy Brown")
            .build());

        // Add Eugene Onegin comments to the map
        commentsMap.put(EUGENE_ONEGIN_ID, eugeneOneginComments);

        // Initialize lists for A Hero of Our Time
        List<Comment> heroOfOurTimeComments = new ArrayList<>();
        heroOfOurTimeComments.add(Comment.builder()
            .id(8L) // Using Long type
            .book(HERO_OF_OUR_TIME) // A Hero of Our Time
            .text("A deep exploration of the human psyche and Russian society.")
            .created(LocalDate.parse("2023-03-10"))
            .author("James Lee")
            .build());

        // Add A Hero of Our Time comments to the map
        commentsMap.put(PredefinedBooks.HERO_OF_OUR_TIME_ID, heroOfOurTimeComments);

        // Initialize lists for comments for Fatalist
        List<Comment> fatalistComments = new ArrayList<>();
        fatalistComments.add(Comment.builder()
            .id(9L) // Using Long type
            .book(FATALIST) // Fatalist
            .text("Thought-provoking and beautifully written.")
            .created(LocalDate.parse("2023-04-01"))
            .author("Sarah Miller")
            .build());

        // Add Fatalist comments to the map
        commentsMap.put(PredefinedBooks.FATALIST_ID, fatalistComments);

        // Initialize lists for comments for Crime and Punishment
        List<Comment> crimeAndPunishmentComments = new ArrayList<>();
        crimeAndPunishmentComments.add(Comment.builder()
            .id(10L) // Using Long type
            .book(CRIME_AND_PUNISHMENT) // Crime and Punishment
            .text("A gripping tale of guilt and redemption.")
            .created(LocalDate.parse("2023-04-02"))
            .author("Paul Martinez")
            .build());

        // Add Crime and Punishment comments to the map
        commentsMap.put(PredefinedBooks.CRIME_AND_PUNISHMENT_ID, crimeAndPunishmentComments);

        // Initialize lists for comments for The Brothers Karamazov
        List<Comment> brothersKaramazovComments = new ArrayList<>();
        brothersKaramazovComments.add(Comment.builder()
            .id(11L) // Using Long type
            .book(BROTHERS_KARAMAZOV) // The Brothers Karamazov
            .text("Dostoevsky tackles profound themes with such depth.")
            .created(LocalDate.parse("2023-04-03"))
            .author("Laura Thompson")
            .build());

        // Add The Brothers Karamazov comments to the map
        commentsMap.put(PredefinedBooks.BROTHERS_KARAMAZOV_ID, brothersKaramazovComments);

        return commentsMap;
    }
}
