--liquibase formatted sql

--changeset chern:2025-01-25--books_genres-add-data
INSERT INTO books_genres (book_id, genre_id) VALUES (1, 1); -- The Hobbit is Fantasy
INSERT INTO books_genres (book_id, genre_id) VALUES (1, 2); -- The Hobbit is Adventure
INSERT INTO books_genres (book_id, genre_id) VALUES (2, 1); -- Fellowship of the Ring is Fantasy
INSERT INTO books_genres (book_id, genre_id) VALUES (2, 2); -- Fellowship of the Ring is Adventure
INSERT INTO books_genres (book_id, genre_id) VALUES (3, 1); -- The Two Towers is Fantasy
INSERT INTO books_genres (book_id, genre_id) VALUES (3, 2); -- The Two Towers is Adventure
INSERT INTO books_genres (book_id, genre_id) VALUES (4, 1); -- The Return of the King is Fantasy
INSERT INTO books_genres (book_id, genre_id) VALUES (4, 2); -- The Return of the King is Adventure


-- Alexander Pushkin's Genres
INSERT INTO books_genres (book_id, genre_id) VALUES (5, 3); -- Eugene Onegin is Classic Literature
INSERT INTO books_genres (book_id, genre_id) VALUES (5, 4); -- Eugene Onegin is Poetry
INSERT INTO books_genres (book_id, genre_id) VALUES (6, 3); -- The Captain's Daughter is Classic Literature

-- Mikhail Lermontov's Genres
INSERT INTO books_genres (book_id, genre_id) VALUES (7, 3); -- A Hero of Our Time is Classic Literature
INSERT INTO books_genres (book_id, genre_id) VALUES (7, 4); -- A Hero of Our Time is Poetry

-- Fyodor Dostoevsky's Genres
INSERT INTO books_genres (book_id, genre_id) VALUES (8, 3); -- Crime and Punishment is Classic Literature
INSERT INTO books_genres (book_id, genre_id) VALUES (8, 5); -- Crime and Punishment is Psychological Fiction
INSERT INTO books_genres (book_id, genre_id) VALUES (9, 3); -- The Brothers Karamazov is Classic Literature
INSERT INTO books_genres (book_id, genre_id) VALUES (9, 5); -- The Brothers Karamazov is Psychological Fiction