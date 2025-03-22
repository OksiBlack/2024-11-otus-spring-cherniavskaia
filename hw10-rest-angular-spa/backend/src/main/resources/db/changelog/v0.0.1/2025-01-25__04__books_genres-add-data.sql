--liquibase formatted sql

--changeset chern:2025-01-25--books_genres-add-data
INSERT INTO books_genres (book_id, genre_id) VALUES (1, 1); -- The Hobbit is Fantasy
INSERT INTO books_genres (book_id, genre_id) VALUES (1, 2); -- The Hobbit is Fiction
INSERT INTO books_genres (book_id, genre_id) VALUES (1, 3); -- The Hobbit is Adventure
INSERT INTO books_genres (book_id, genre_id) VALUES (2, 1); -- Fellowship of the Ring is Fantasy
INSERT INTO books_genres (book_id, genre_id) VALUES (2, 2); -- Fellowship of the Ring is Fiction
INSERT INTO books_genres (book_id, genre_id) VALUES (2, 3); -- The Hobbit is Adventure
INSERT INTO books_genres (book_id, genre_id) VALUES (3, 1); -- The Two Towers is Fantasy
INSERT INTO books_genres (book_id, genre_id) VALUES (3, 2); -- The Two Towers is Fiction
INSERT INTO books_genres (book_id, genre_id) VALUES (3, 3); -- The Two Towers is Adventure
INSERT INTO books_genres (book_id, genre_id) VALUES (4, 1); -- The Return of the King is Fantasy
INSERT INTO books_genres (book_id, genre_id) VALUES (4, 2); -- The Return of the King is Fiction
INSERT INTO books_genres (book_id, genre_id) VALUES (4, 3); -- The Return of the King is Adventure


-- Alexander Pushkin's Genres
INSERT INTO books_genres (book_id, genre_id) VALUES (5, 5); -- Eugene Onegin is Classic Literature
INSERT INTO books_genres (book_id, genre_id) VALUES (5, 6); -- Eugene Onegin is Poetry

-- Mikhail Lermontov's Genres
INSERT INTO books_genres (book_id, genre_id) VALUES (6, 5); -- A Hero of Our Time is Classic Literature
INSERT INTO books_genres (book_id, genre_id) VALUES (6, 6); -- A Hero of Our Time is Classic Literature
INSERT INTO books_genres (book_id, genre_id) VALUES (7, 5); -- A Fatalist is Classic Literature

-- Fyodor Dostoevsky's Genres
INSERT INTO books_genres (book_id, genre_id) VALUES (8, 5); -- Crime and Punishment is Classic Literature
INSERT INTO books_genres (book_id, genre_id) VALUES (8, 7); -- Crime and Punishment is Psychological Fiction
INSERT INTO books_genres (book_id, genre_id) VALUES (9, 5); -- The Brothers Karamazov is Classic Literature
INSERT INTO books_genres (book_id, genre_id) VALUES (9, 7); -- The Brothers Karamazov is Psychological Fiction