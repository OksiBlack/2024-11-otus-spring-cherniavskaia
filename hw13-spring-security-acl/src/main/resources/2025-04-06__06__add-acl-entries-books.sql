--liquibase formatted sql
--changeset chern:2025-04-06--acl-entry-add-permissions
INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
-- Permissions for authors
(1, 1, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_AUTHOR'), 1, TRUE, FALSE, FALSE),  -- Author can read own entries
(2, 2, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, FALSE, FALSE),  -- Reader can read authors
(3, 5, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, FALSE, FALSE),  -- Reader can read books
(4, 6, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 5, TRUE, FALSE, FALSE),  -- Editor can manage books
(5, 7, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 7, TRUE, FALSE, FALSE),  -- Admin manages everything
(6, 8, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, FALSE, FALSE),  -- Reader can comment on books
(7, 9, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, FALSE, FALSE);  -- Reader access to genres

--liquibase formatted sql
--changeset chern:2025-04-06--acl-entry-add-genre-permissions
INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
-- Permissions for genres
(8, 15, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, FALSE, FALSE),  -- Readers can read Fantasy
(9, 16, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, FALSE, FALSE),  -- Readers can read Classic
(10, 17, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, FALSE, FALSE),  -- Readers can read Mystery
(11, 18, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, FALSE, FALSE),  -- Readers can read Fiction
(12, 15, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 5, TRUE, FALSE, FALSE),  -- Editors can manage Fantasy
(13, 16, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 5, TRUE, FALSE, FALSE),  -- Editors can manage Classic
(14, 17, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 5, TRUE, FALSE, FALSE),  -- Editors can manage Mystery
(15, 18, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 5, TRUE, FALSE, FALSE),  -- Editors can manage Fiction
(16, 15, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 7, TRUE, FALSE, FALSE),  -- Admins manage Fantasy
(17, 16, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 7, TRUE, FALSE, FALSE),  -- Admins manage Classic
(18, 17, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 7, TRUE, FALSE, FALSE),  -- Admins manage Mystery
(19, 18, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 7, TRUE, FALSE, FALSE);  -- Admins manage Fiction

--liquibase formatted sql
--changeset chern:2025-04-06--acl-entry-add-author-permissions
INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
-- Permissions for authors
(20, 19, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, FALSE, FALSE),  -- Readers can read Tolkien
(21, 20, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, FALSE, FALSE),  -- Readers can read Pushkin
(22, 21, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, FALSE, FALSE),  -- Readers can read Lermontov
(23, 22, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, FALSE, FALSE),  -- Readers can read Dostoevsky
(24, 19, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 5, TRUE, FALSE, FALSE),  -- Editors can manage Tolkien
(25, 20, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 5, TRUE, FALSE, FALSE),  -- Editors can manage Pushkin
(26, 21, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 5, TRUE, FALSE, FALSE),  -- Editors can manage Lermontov
(27, 22, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 5, TRUE, FALSE, FALSE),  -- Editors can manage Dostoevsky
(28, 19, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 7, TRUE, FALSE, FALSE),  -- Admin can fully manage Tolkien
(29, 20, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 7, TRUE, FALSE, FALSE),  -- Admin can fully manage Pushkin
(30, 21, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 7, TRUE, FALSE, FALSE),  -- Admin can fully manage Lermontov
(31, 22, 1, (SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 7, TRUE, FALSE, FALSE);  -- Admin can fully manage Dostoevsky
