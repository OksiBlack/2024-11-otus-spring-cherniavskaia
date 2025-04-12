--liquibase formatted sql
--changeset chern:2025-04-06--acl-object-identity-add-data-authors
INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, '1', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Tolkien
(2, 1, '2', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Pushkin
(3, 1, '3', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Lermontov
(4, 1, '4', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE);  -- Dostoevsky

--liquibase formatted sql
--changeset chern:2025-04-06--acl-object-identity-add-data-books
INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(5, 3, '1', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- The Hobbit
(6, 3, '2', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Fellowship
(7, 3, '3', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Two Towers
(8, 3, '4', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Return of the King
(9, 3, '5', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Eugene Onegin
(10, 3, '6', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- A Hero of Our Time
(11, 3, '8', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Crime and Punishment
(12, 3, '9', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE);  -- The Brothers Karamazov

--liquibase formatted sql
--changeset chern:2025-04-06--acl-object-identity-genres
INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(13, 2, '1', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Fantasy
(14, 2, '2', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Fiction
(15, 2, '3', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Adventure
(16, 2, '4', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Science Fiction
(17, 2, '5', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Classic Literature
(18, 2, '6', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Poetry
(19, 2, '9', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Mystery
(20, 2, '10', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Romance
(21, 2, '12', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE),  -- Thriller
(22, 2, '13', NULL, (SELECT id FROM acl_sid WHERE sid = 'admin'), TRUE);  -- Biography
