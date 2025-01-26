-- liquibase formatted sql

-- changeset chernyavskaya:2025-01-25--comments-add-data
insert into comments(book_id, text, comment_date, author)
values (1,'Comment_1_1', '2024-01-01', 'Comment author_1'),
        (2,'Comment_2_2', '2024-01-02', 'Comment author_2'),
        (1,'Comment_1_2', '2024-01-04', 'Comment author_2'),
        (1,'Comment_1_3', '2024-01-04', 'Comment author_3');