insert into authors(full_name)
values
  ('Author_1'),
  ('Author_2'),
  ('Author_3'),
  ('Author_4'),
  ('Author_5'),
  ('Author_6'),
  ('Author_7'),
  ('Author_8');

insert into genres(name)
values
  ('Genre_1'),
  ('Genre_2'),
  ('Genre_3'),
  ('Genre_4'),
  ('Genre_5'),
  ('Genre_6'),
  ('Genre_7'),
  ('Genre_8');

insert into books(title, description)
values
  ('BookTitle_1', 'description 1'),
  ('BookTitle_2', 'description 2'),
  ('BookTitle_3', 'description 3'),
  ('BookTitle_4', 'description 4'),
  ('BookTitle_5', 'description 5'),
  ('BookTitle_6', 'description 6'),
  ('BookTitle_7', 'description 7'),
  ('BookTitle_8', 'description 8');

insert into books_genres(book_id, genre_id)
values
  (1, 1),
  (1, 2),
  (2, 3),
  (2, 4),
  (3, 5),
  (3, 6),
  (4, 4),
  (5, 5),
  (6, 6),
  (7, 7),
  (8, 8);

insert into books_authors(book_id, author_id)
values
  (1, 1),
  (3, 3),
  (2, 2),
  (1, 2),
  (2, 3),
  (4, 4),
  (5, 5),
  (6, 6),
  (7, 7),
  (8, 8);
