--liquibase formatted sql

--changeset chern:2025-01-25--comments-add-data
insert into comments(book_id, text, created, author)
values (1,'Comment_1_1', '2024-01-01', 'Author_1'),
        (2,'Comment_2', '2024-01-02', 'Author_2'),
        (3,'Comment_3', '2024-01-03', 'Author_3'),
        (1,'Comment_1_2', '2024-01-04', 'Author_1'),
        (1,'Comment_1_3', '2024-01-04', 'Author_2');