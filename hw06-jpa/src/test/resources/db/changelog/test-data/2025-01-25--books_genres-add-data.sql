-- liquibase formatted sql

-- changeset chernyavskaya:2025-01-25--books_genres-add-data
insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);