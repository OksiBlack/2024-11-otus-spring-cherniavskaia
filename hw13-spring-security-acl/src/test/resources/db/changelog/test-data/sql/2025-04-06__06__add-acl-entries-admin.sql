--liquibase formatted sql
--changeset chern:2025-04-06--acl-entry-add-author-admin-permissions

-- Grant 'admin' role access to all authors
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(1, 3,(SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 31, TRUE, TRUE, TRUE),  -- Access for Author 7
(2, 3,(SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 31, TRUE, TRUE, TRUE),  -- Access for Author 2
(3, 3,(SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 31, TRUE, TRUE, TRUE),  -- Access for Author 3
(4, 3,(SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 31, TRUE, TRUE, TRUE);  -- Access for Author 4


--changeset chern:2025-04-06--acl-entry-add-genres-admin-permissions
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(13, 3,(SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 31, TRUE, TRUE, TRUE),  -- Fantasy
(14, 3,(SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 31, TRUE, TRUE, TRUE),  -- Fiction
(15, 3,(SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 31, TRUE, TRUE, TRUE),  -- Adventure
(16, 3,(SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 31, TRUE, TRUE, TRUE),  -- Science Fiction
(17, 3,(SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 31, TRUE, TRUE, TRUE),  -- Classic Literature
(18, 3,(SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 31, TRUE, TRUE, TRUE),  -- Poetry
(19, 3,(SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 31, TRUE, TRUE, TRUE),  -- Mystery
(20, 3,(SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 31, TRUE, TRUE, TRUE),  -- Romance
(21, 3,(SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 31, TRUE, TRUE, TRUE),  -- Thriller
(22, 3,(SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 31, TRUE, TRUE, TRUE);  -- Biography

--liquibase formatted sql
--changeset chern:2025-04-06--acl-entry-add-data-books-role_admin
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT aoi.id, 3,(SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN'), 3,TRUE, TRUE, TRUE
FROM acl_object_identity aoi
WHERE aoi.object_id_identity IN (
    SELECT object_id_identity FROM acl_object_identity
    WHERE object_id_class = 3
)
AND NOT EXISTS (
    SELECT 1 FROM acl_entry ae
    WHERE ae.acl_object_identity = aoi.id
    AND ae.sid = (SELECT id FROM acl_sid WHERE sid = 'ROLE_ADMIN')
    AND ae.ace_order = 3
);