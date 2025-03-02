--liquibase formatted sql
--changeset chern:2025-03-01-schema
create schema if not exists bookstore;

--liquibase formatted sql
--changeset chern:2025-01-25-schema
create table authors (
    id bigserial,
    first_name varchar(155),
    last_name varchar(255),
    middle_name varchar(100),
    description TEXT,
    primary key (id)
);

--liquibase formatted sql
--changeset chern:2025-01-25-genres
create table genres (
    id bigserial,
    name varchar(255),
    primary key (id),
    description TEXT
);

--liquibase formatted sql
--changeset chern:2025-01-25-books
create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    description TEXT,
    serial_number VARCHAR(50),
    isbn VARCHAR(20),
    primary key (id)
);


--liquibase formatted sql
--changeset chern:2025-01-25-books_genres
create table books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);

--liquibase formatted sql
--changeset chern:2025-01-25-books_comments
create table comments (
    id bigserial,
    book_id bigint references books(id) on delete cascade,
    text varchar(255),
    created date,
    author varchar(255),
    primary key (id)
);