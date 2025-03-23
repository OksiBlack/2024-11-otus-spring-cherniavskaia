--liquibase formatted sql

--changeset chern:2025-03-22-add-read-edit-admin-authorities
INSERT INTO bookstore_management.authorities (name) VALUES ('READER');
INSERT INTO bookstore_management.authorities (name) VALUES ('EDITOR');
INSERT INTO bookstore_management.authorities (name) VALUES ('ADMIN');


--changeset chern:2025-03-22-re-add-frodo-user
INSERT INTO bookstore_management.users (login, first_name, last_name, password, email)
VALUES ('frodo', 'Frodo', 'Baggins', '$2a$10$js2r5j8pqmZ5wffFn4lCk.ImH9yms2y7.x9CtWOTxL4OBB0GC80EG', 'frodo@gmail.com');

--changeset chern:2025-03-22-re-add-bilbo-user
INSERT INTO bookstore_management.users (login, first_name, last_name, password, email)
VALUES ('bilbo', 'Bilbo', 'Baggins', '$2a$10$0P7IwHygwj7q1td5dnceL.OCSzfK4hm7Jr8tOiG8EBi9SLfALQP1m', 'bilbo@gmail.com');

--changeset chern:2025-03-22-add-more-users
INSERT INTO bookstore_management.users (login, first_name, last_name, password, email)
VALUES
    ('gandalf', 'Gandalf', 'The Grey', '$2a$10$0i/vBVOb8DR8RZfFUIiXfewNTwVpWOfTVzE/CMUY2lOC4xExFdB92', 'gandalf@gmail.com'),
    ('aragorn', 'Aragorn', 'Elessar', '$2a$10$DE4eC6kx3ooOm/T1K1waiONL6a84zIA4jhIGJVHcxePrdBrWHGuSW', 'aragorn@gmail.com'),
    ('admin', 'admin', 'admin', '$2a$10$0qdXar1AYIxqm/WuyYcZ..SReMXL2b/tcw0xNoLX9sVJR/0Ft/4oS', 'admin@gmail.com');

--changeset chern:2025-03-22-populate-authorities
INSERT INTO bookstore_management.users_authorities (user_id, authority_id)
SELECT (SELECT id FROM bookstore_management.users WHERE login = 'frodo'),
       (SELECT id FROM bookstore_management.authorities WHERE name = 'ADMIN');
INSERT INTO bookstore_management.users_authorities (user_id, authority_id)
SELECT (SELECT id FROM bookstore_management.users WHERE login = 'frodo'),
       (SELECT id FROM bookstore_management.authorities WHERE name = 'EDITOR');

--changeset chern:2025-03-22-re-assign-bilbo-authorities
INSERT INTO bookstore_management.users_authorities (user_id, authority_id)
SELECT (SELECT id FROM bookstore_management.users WHERE login = 'bilbo'),
       (SELECT id FROM bookstore_management.authorities WHERE name = 'READER');

--changeset chern:2025-03-22-assign-gandalf-authorities
INSERT INTO bookstore_management.users_authorities (user_id, authority_id)
SELECT (SELECT id FROM bookstore_management.users WHERE login = 'gandalf'),
       (SELECT id FROM bookstore_management.authorities WHERE name = 'EDITOR');

--changeset chern:2025-03-22-assign-aragorn-authorities
INSERT INTO bookstore_management.users_authorities (user_id, authority_id)
SELECT (SELECT id FROM bookstore_management.users WHERE login = 'aragorn'),
       (SELECT id FROM bookstore_management.authorities WHERE name = 'READER');

--changeset chern:2025-03-22-assign-aragorn-authorities-edit
INSERT INTO bookstore_management.users_authorities (user_id, authority_id)
SELECT (SELECT id FROM bookstore_management.users WHERE login = 'aragorn'),
       (SELECT id FROM bookstore_management.authorities WHERE name = 'EDITOR');

--changeset chern:2025-03-22-assign-admin-authorities
INSERT INTO bookstore_management.users_authorities (user_id, authority_id)
SELECT (SELECT id FROM bookstore_management.users WHERE login = 'admin'),
       (SELECT id FROM bookstore_management.authorities WHERE name = 'ADMIN');

--changeset chern:2025-03-22-assign-admin-authorities-edit
INSERT INTO bookstore_management.users_authorities (user_id, authority_id)
SELECT (SELECT id FROM bookstore_management.users WHERE login = 'admin'),
       (SELECT id FROM bookstore_management.authorities WHERE name = 'EDITOR');