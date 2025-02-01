-- liquibase formatted sql

-- changeset chernyavskaya:2025-01-25--books-add-data
insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);