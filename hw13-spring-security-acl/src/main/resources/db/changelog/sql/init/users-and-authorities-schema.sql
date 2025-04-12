--liquibase formatted sql

--changeset chern:2025-03-22-extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--changeset chern:2025-03-22-authorities
CREATE TABLE authorities (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(1024)
);

--liquibase formatted sql
--changeset chern:2025-03-22-users
CREATE TABLE users (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    login VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    middle_name VARCHAR(255),
    birthday DATE,
    email VARCHAR(255),
    password VARCHAR(255) NOT NULL
);

--liquibase formatted sql
--changeset chern:2025-03-22--users-authorities
CREATE TABLE users_authorities (
    user_id UUID NOT NULL,
    authority_id UUID NOT NULL,
    PRIMARY KEY (user_id, authority_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,   -- Added schema name
    FOREIGN KEY (authority_id) REFERENCES authorities(id) ON DELETE CASCADE -- Added schema name
);
