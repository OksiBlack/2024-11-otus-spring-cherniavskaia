--liquibase formatted sql

--changeset chern:2025-04-06-acl_sid-inserts
INSERT INTO acl_sid (id, principal, sid) VALUES
(1, false, 'ROLE_READER'),
(2, false, 'ROLE_EDITOR'),
(3, false, 'ROLE_ADMIN'),
(4, true, 'frodo'),
(5, true, 'bilbo'),
(6, true, 'aragorn'),
(7, true, 'admin');

--changeset chern:2025-04-06-acl_class-inserts
INSERT INTO acl_class (id, class) VALUES
(1, 'ru.otus.hw.model.Author'),
(2, 'ru.otus.hw.model.Genre'),
(3, 'ru.otus.hw.model.Book');