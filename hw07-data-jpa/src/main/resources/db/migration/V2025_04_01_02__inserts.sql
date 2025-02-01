insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

insert into comments(book_id, text, comment_date, author)
values (1,'Comment_1_1', '2024-01-01', 'Author_1'),
        (2,'Comment_2', '2024-01-02', 'Author_2'),
        (3,'Comment_3', '2024-01-03', 'Author_3'),
        (1,'Comment_1_2', '2024-01-04', 'Author_1'),
        (1,'Comment_1_3', '2024-01-04', 'Author_2');