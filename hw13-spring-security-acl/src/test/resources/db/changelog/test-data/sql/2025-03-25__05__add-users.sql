--liquibase formatted sql

--changeset chern:2025-03-22-add-read-edit-admin-authorities
INSERT INTO authorities (name, description) VALUES ('READER', 'role for read');
INSERT INTO authorities (name, description) VALUES ('EDITOR', 'role for edit');
INSERT INTO authorities (name, description) VALUES ('ADMIN', 'role for administration');


--changeset chern:2025-03-22-add-frodo-user
INSERT INTO users (login, first_name, last_name, password, email)
VALUES ('frodo', 'Frodo', 'Baggins', '$2a$10$js2r5j8pqmZ5wffFn4lCk.ImH9yms2y7.x9CtWOTxL4OBB0GC80EG', 'frodo@gmail.com');

--changeset chern:2025-03-22-add-bilbo-user
INSERT INTO users (login, first_name, last_name, password, email)
VALUES ('bilbo', 'Bilbo', 'Baggins', '$2a$10$0P7IwHygwj7q1td5dnceL.OCSzfK4hm7Jr8tOiG8EBi9SLfALQP1m', 'bilbo@gmail.com');

--changeset chern:2025-03-22-add-more-users
INSERT INTO users (login, first_name, last_name, password, email)
VALUES
    ('aragorn', 'Aragorn', 'Elessar', '$2a$10$DE4eC6kx3ooOm/T1K1waiONL6a84zIA4jhIGJVHcxePrdBrWHGuSW', 'aragorn@gmail.com'),
    ('admin', 'admin', 'admin', '$2a$10$0qdXar1AYIxqm/WuyYcZ..SReMXL2b/tcw0xNoLX9sVJR/0Ft/4oS', 'admin@gmail.com');

--changeset chern:2025-03-22-populate-authorities
INSERT INTO users_authorities (user_id, authority_id)
SELECT (SELECT id FROM users WHERE login = 'frodo'),
       (SELECT id FROM authorities WHERE name = 'EDITOR');

--changeset chern:2025-03-22-re-assign-bilbo-authorities
INSERT INTO users_authorities (user_id, authority_id)
SELECT (SELECT id FROM users WHERE login = 'bilbo'),
       (SELECT id FROM authorities WHERE name = 'READER');

--changeset chern:2025-03-22-assign-admin-authorities
INSERT INTO users_authorities (user_id, authority_id)
SELECT (SELECT id FROM users WHERE login = 'admin'),
       (SELECT id FROM authorities WHERE name = 'ADMIN');