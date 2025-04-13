--liquibase formatted sql

--changeset chern:2025-03-22--books-add-data-with-subselects
INSERT INTO books (title, description, serial_number, isbn, author_id)
SELECT 'The Hobbit', 'A fantasy novel about the adventures of a hobbit named Bilbo Baggins, who is drawn into an epic quest by a group of dwarves and the wizard Gandalf.', 'SN001', '978-0-618-26030-0', (SELECT id FROM authors WHERE last_name = 'Tolkien');

INSERT INTO books (title, description, serial_number, isbn, author_id)
SELECT 'The Lord of the Rings: The Fellowship of the Ring', 'The first part of the epic trilogy of Middle-earth, where a group of heroes embark on a quest to destroy the One Ring.', 'SN002', '978-0-618-32970-8', (SELECT id FROM authors WHERE last_name = 'Tolkien');

INSERT INTO books (title, description, serial_number, isbn, author_id)
SELECT 'The Lord of the Rings: The Two Towers', 'The continuation of the epic journey of the fellowship as they face new challenges and enemies.', 'SN003', '978-0-618-32972-2', (SELECT id FROM authors WHERE last_name = 'Tolkien');

INSERT INTO books (title, description, serial_number, isbn, author_id)
SELECT 'The Lord of the Rings: The Return of the King', 'The final book of the trilogy where the battle for Middle-earth culminates.', 'SN004', '978-0-618-32973-9', (SELECT id FROM authors WHERE last_name = 'Tolkien');

INSERT INTO books (title, description, serial_number, isbn, author_id)
SELECT 'Eugene Onegin', 'A novel in verse, telling the story of a young nobleman and his interaction with the world around him, including love and loss.', 'SN005', '978-0-14-044499-5', (SELECT id FROM authors WHERE last_name = 'Pushkin');

INSERT INTO books (title, description, serial_number, isbn, author_id)
SELECT 'A Hero of Our Time', 'A novel that explores the life of a disillusioned Russian officer and reflects on the nature of the Russian soul.', 'SN007', '978-0-14-044240-3', (SELECT id FROM authors WHERE last_name = 'Lermontov');

INSERT INTO books (title, description, serial_number, isbn, author_id)
SELECT 'Crime and Punishment', 'A novel that tells the story of Raskolnikov, a former student who commits a murder and struggles with guilt and redemption.', 'SN009', '978-0-14-305814-9', (SELECT id FROM authors WHERE last_name = 'Dostoevsky');

INSERT INTO books (title, description, serial_number, isbn, author_id)
SELECT 'The Brothers Karamazov', 'A philosophical novel that addresses deep moral questions through the lives and conflicts of the Karamazov family.', 'SN010', '978-0-14-303911-6', (SELECT id FROM authors WHERE last_name = 'Dostoevsky');
