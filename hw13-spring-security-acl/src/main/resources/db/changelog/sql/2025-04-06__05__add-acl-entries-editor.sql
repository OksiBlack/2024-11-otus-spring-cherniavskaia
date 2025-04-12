--liquibase formatted sql
--changeset chern:2025-04-06--acl-entry-add-author-editor-permissions

-- Grant 'editor' role access to all authors
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(1, 4, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 7, TRUE, TRUE, TRUE),  -- Access for Author 7
(2, 4, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 7, TRUE, TRUE, TRUE),  -- Access for Author 2
(3, 4, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 7, TRUE, TRUE, TRUE),  -- Access for Author 3
(4, 4, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 7, TRUE, TRUE, TRUE);  -- Access for Author 4

--changeset chern:2025-04-06--acl-entry-add-genres-editor-permissions
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(13, 4, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 7, TRUE, TRUE, TRUE),  -- Fantasy
(14, 4, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 7, TRUE, TRUE, TRUE),  -- Fiction
(15, 4, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 7, TRUE, TRUE, TRUE),  -- Adventure
(16, 4, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 7, TRUE, TRUE, TRUE),  -- Science Fiction
(17, 4, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 7, TRUE, TRUE, TRUE),  -- Classic Literature
(18, 4, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 7, TRUE, TRUE, TRUE),  -- Poetry
(19, 4, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 7, TRUE, TRUE, TRUE),  -- Mystery
(20, 4, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 7, TRUE, TRUE, TRUE),  -- Romance
(21, 4, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 7, TRUE, TRUE, TRUE),  -- Thriller
(22, 4, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 7, TRUE, TRUE, TRUE);  -- Biography


--liquibase formatted sql
--changeset chern:2025-04-06--acl-entry-add-data-books-role_EDITOR
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT aoi.id, 4, (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR'), 1, TRUE, TRUE, TRUE
FROM acl_object_identity aoi
WHERE aoi.object_id_identity IN (
    SELECT object_id_identity FROM acl_object_identity
    WHERE object_id_class = 3  -- Adjust the class type if needed
)
AND NOT EXISTS (
    SELECT 1 FROM acl_entry ae
    WHERE ae.acl_object_identity = aoi.id
    AND ae.sid = (SELECT id FROM acl_sid WHERE sid = 'ROLE_EDITOR')
    AND ae.ace_order = 4
);