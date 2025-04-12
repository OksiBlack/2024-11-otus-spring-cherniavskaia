--liquibase formatted sql

--changeset chern:2025-04-06--acl-entry-add-author-reader-authors-permissions
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(1, 5, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, TRUE, TRUE),  -- Access for Author 1
(2, 5, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, TRUE, TRUE),  -- Access for Author 2
(5, 5, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, TRUE, TRUE),  -- Access for Author 5
(4, 5, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, TRUE, TRUE);  -- Access for Author 4

--changeset chern:2025-04-06--acl-entry-add-genres-reader-permissions-genres
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(13, 5, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, TRUE, TRUE),  -- Fantasy
(14, 5, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, TRUE, TRUE),  -- Fiction
(15, 5, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, TRUE, TRUE),  -- Adventure
(16, 5, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, TRUE, TRUE),  -- Science Fiction
(17, 5, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, TRUE, TRUE),  -- Classic Literature
(18, 5, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, TRUE, TRUE),  -- Poetry
(19, 5, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, TRUE, TRUE),  -- Mystery
(20, 5, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, TRUE, TRUE),  -- Romance
(21, 5, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, TRUE, TRUE),  -- Thriller
(22, 5, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, TRUE, TRUE);  -- Biography


--liquibase formatted sql
--changeset chern:2025-04-06--acl-entry-add-data-books-role_reader_books
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT aoi.id, 5, (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER'), 1, TRUE, TRUE, TRUE
FROM acl_object_identity aoi
WHERE aoi.object_id_identity IN (
    SELECT object_id_identity FROM acl_object_identity
    WHERE object_id_class = 3  -- Adjust the class type if needed
)
AND NOT EXISTS (
    SELECT 1 FROM acl_entry ae
    WHERE ae.acl_object_identity = aoi.id
    AND ae.sid = (SELECT id FROM acl_sid WHERE sid = 'ROLE_READER')
    AND ae.ace_order = 5
);


