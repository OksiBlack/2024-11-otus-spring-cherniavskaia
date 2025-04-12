--liquibase formatted sql

--changeset chern:2025-03-22--books_genres-add-hobbit-data
INSERT INTO books_genres (book_id, genre_id)
SELECT b.id, g.id
FROM books b, genres g
WHERE b.title = 'The Hobbit'
  AND (g.name = 'Fantasy' OR g.name = 'Fiction' OR g.name = 'Adventure');

--changeset chern:2025-03-22--books_genres-add-fellowship-data
INSERT INTO books_genres (book_id, genre_id)
SELECT b.id, g.id
FROM books b, genres g
WHERE b.title = 'The Lord of the Rings: The Fellowship of the Ring'
  AND (g.name = 'Fantasy' OR g.name = 'Fiction' OR g.name = 'Adventure');

--changeset chern:2025-03-22--books_genres-add-two-towers-data
INSERT INTO books_genres (book_id, genre_id)
SELECT b.id, g.id
FROM books b, genres g
WHERE b.title = 'The Lord of the Rings: The Two Towers'
  AND (g.name = 'Fantasy' OR g.name = 'Fiction' OR g.name = 'Adventure');

--changeset chern:2025-03-22--books_genres-add-return-of-king-data
INSERT INTO books_genres (book_id, genre_id)
SELECT b.id, g.id
FROM books b, genres g
WHERE b.title = 'The Lord of the Rings: The Return of the King'
  AND (g.name = 'Fantasy' OR g.name = 'Fiction' OR g.name = 'Adventure');

--changeset chern:2025-03-22--books_genres-add-eugene-onegin-data
INSERT INTO books_genres (book_id, genre_id)
SELECT b.id, g.id
FROM books b, genres g
WHERE b.title = 'Eugene Onegin'
  AND (g.name = 'Classic Literature' OR g.name = 'Poetry');

--changeset chern:2025-03-22--books_genres-add-hero-of-our-time-data
INSERT INTO books_genres (book_id, genre_id)
SELECT b.id, g.id
FROM books b, genres g
WHERE b.title = 'A Hero of Our Time'
  AND (g.name = 'Classic Literature' OR g.name = 'Poetry');

--changeset chern:2025-03-22--books_genres-add-crime-and-punishment-data
INSERT INTO books_genres (book_id, genre_id)
SELECT b.id, g.id
FROM books b, genres g
WHERE b.title = 'Crime and Punishment'
  AND (g.name = 'Classic Literature');

--changeset chern:2025-03-22--books_genres-add-brothers-karamazov-data
INSERT INTO books_genres (book_id, genre_id)
SELECT b.id, g.id
FROM books b, genres g
WHERE b.title = 'The Brothers Karamazov'
  AND (g.name = 'Classic Literature');
