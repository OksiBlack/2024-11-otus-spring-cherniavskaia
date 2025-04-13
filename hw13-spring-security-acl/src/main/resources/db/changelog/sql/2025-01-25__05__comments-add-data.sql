--liquibase formatted sql

--changeset chern:2025-01-25--comments-add-data
INSERT INTO comments (book_id, text, created, author)
VALUES
(1, 'An absolutely fascinating journey through Middle-earth.', '2023-01-15', 'John Doe'),
(1, 'This book inspired me to explore the world of fantasy literature.', '2023-01-16', 'Alice Smith'),
(2, 'The storytelling is immersive; J.R.R. Tolkien is a genius.', '2023-02-10', 'Jane Doe'),
(2, 'I loved the character development in this book.', '2023-02-11', 'Bob Johnson'),
(3, 'The plot twists kept me on the edge of my seat.', '2023-03-05', 'Emily Davis'),
(4, 'A powerful conclusion to an epic series.', '2023-03-06', 'Mark Wilson'),
(5, 'Pushkins writing style is poetic and captivating.', '2023-02-20', 'Lucy Brown'),
(6, 'A deep exploration of the human psyche and Russian society.', '2023-03-10', 'James Lee'),
(8, 'Thought-provoking and beautifully written.', '2023-04-01', 'Sarah Miller'),
(8, 'A gripping tale of guilt and redemption.', '2023-04-02', 'Paul Martinez'),
(8, 'Dostoevsky tackles profound themes with such depth.', '2023-04-03', 'Laura Thompson');