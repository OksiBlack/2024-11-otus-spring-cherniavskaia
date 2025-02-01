--liquibase formatted sql

--changeset chern:2025-01-25--author-add-data
insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');