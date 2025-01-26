-- liquibase formatted sql

-- changeset chernyavskaya:2025-01-25--genres-add-data
insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');