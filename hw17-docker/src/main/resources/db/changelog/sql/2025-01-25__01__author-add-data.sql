--liquibase formatted sql
--changeset chern:2025-01-25--author-add-data
INSERT INTO authors (first_name, middle_name, last_name, description)
VALUES
('John', 'Ronald', 'Tolkien', 'English writer and academic, best known for The Hobbit and The Lord of the Rings.');

INSERT INTO authors (first_name, middle_name, last_name, description)
VALUES
('Alexander', NULL, 'Pushkin', 'Russian poet, playwright, and novelist, considered to be the founder of modern Russian literature.');

INSERT INTO authors (first_name, middle_name, last_name, description)
VALUES
('Mikhail', NULL, 'Lermontov', 'Russian Romantic writer and figurative artist, known for his poetry and the novel A Hero of Our Time.');

INSERT INTO authors (first_name, middle_name, last_name, description)
VALUES
('Fyodor', NULL, 'Dostoevsky', 'Russian novelist famous for works like Crime and Punishment and The Brothers Karamazov.');
