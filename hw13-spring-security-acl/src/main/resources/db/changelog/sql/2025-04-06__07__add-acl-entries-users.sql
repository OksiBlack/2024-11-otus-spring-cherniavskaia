--liquibase formatted sql
--changeset chern:2025-04-06--acl-entry-add-author-frodo-permissions
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(1, 1, (SELECT id FROM acl_sid WHERE sid = 'frodo'), 31, TRUE, TRUE, TRUE);  -- Access for Tolkien


--liquibase formatted sql
--changeset chern:2025-04-07--acl-entry-add-frodo-admin-permissions
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES
    (5, 1, (SELECT id FROM acl_sid WHERE sid = 'frodo'), 31, TRUE, TRUE, TRUE),  -- The Hobbit
    (6, 1, (SELECT id FROM acl_sid WHERE sid = 'frodo'), 31, TRUE, TRUE, TRUE),  -- Fellowship
    (7, 1, (SELECT id FROM acl_sid WHERE sid = 'frodo'), 31, TRUE, TRUE, TRUE),  -- Two Towers
    (8, 1, (SELECT id FROM acl_sid WHERE sid = 'frodo'), 31, TRUE, TRUE, TRUE);  -- Return of the King


--changeset chern:2025-04-06--acl-entry-add-genres-permissions-frodo
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(13, 1, (SELECT id FROM acl_sid WHERE sid = 'frodo'), 31, TRUE, TRUE, TRUE),  -- Fantasy
(14, 1, (SELECT id FROM acl_sid WHERE sid = 'frodo'), 31, TRUE, TRUE, TRUE),  -- Fiction
(15, 1, (SELECT id FROM acl_sid WHERE sid = 'frodo'), 31, TRUE, TRUE, TRUE);  -- Adventure;


--liquibase formatted sql
--changeset chern:2025-04-06--acl-entry-add-author-bilbo-permissions
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(1, 1, (SELECT id FROM acl_sid WHERE sid = 'bilbo'), 7, TRUE, TRUE, TRUE);  -- Access for Tolkien


--liquibase formatted sql
--changeset chern:2025-04-07--acl-entry-add-bilbo-admin-permissions
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES
    (5, 1, (SELECT id FROM acl_sid WHERE sid = 'bilbo'), 7, TRUE, TRUE, TRUE),  -- The Hobbit
    (6, 1, (SELECT id FROM acl_sid WHERE sid = 'bilbo'), 7, TRUE, TRUE, TRUE),  -- Fellowship
    (7, 1, (SELECT id FROM acl_sid WHERE sid = 'bilbo'), 7, TRUE, TRUE, TRUE),  -- Two Towers
    (8, 1, (SELECT id FROM acl_sid WHERE sid = 'bilbo'), 7, TRUE, TRUE, TRUE);  -- Return of the King


--changeset chern:2025-04-06--acl-entry-add-genres-permissions-bilbo
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(13, 1, (SELECT id FROM acl_sid WHERE sid = 'bilbo'), 7, TRUE, TRUE, TRUE),  -- Fantasy
(14, 1, (SELECT id FROM acl_sid WHERE sid = 'bilbo'), 7, TRUE, TRUE, TRUE),  -- Fiction
(15, 1, (SELECT id FROM acl_sid WHERE sid = 'bilbo'), 7, TRUE, TRUE, TRUE);  -- Adventure;



--liquibase formatted sql
--changeset chern:2025-04-06--acl-entry-add-author-aragorn-permissions
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(1, 1, (SELECT id FROM acl_sid WHERE sid = 'aragorn'), 7, TRUE, TRUE, TRUE);  -- Access for Tolkien


--liquibase formatted sql
--changeset chern:2025-04-07--acl-entry-add-aragorn-admin-permissions
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES
    (5, 1, (SELECT id FROM acl_sid WHERE sid = 'aragorn'), 7, TRUE, TRUE, TRUE),  -- The Hobbit
    (6, 1, (SELECT id FROM acl_sid WHERE sid = 'aragorn'), 7, TRUE, TRUE, TRUE),  -- Fellowship
    (7, 1, (SELECT id FROM acl_sid WHERE sid = 'aragorn'), 7, TRUE, TRUE, TRUE),  -- Two Towers
    (8, 1, (SELECT id FROM acl_sid WHERE sid = 'aragorn'), 7, TRUE, TRUE, TRUE);  -- Return of the King


--changeset chern:2025-04-06--acl-entry-add-genres-permissions-aragorn
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(13, 1, (SELECT id FROM acl_sid WHERE sid = 'aragorn'), 7, TRUE, TRUE, TRUE),  -- Fantasy
(14, 1, (SELECT id FROM acl_sid WHERE sid = 'aragorn'), 7, TRUE, TRUE, TRUE),  -- Fiction
(15, 1, (SELECT id FROM acl_sid WHERE sid = 'aragorn'), 7, TRUE, TRUE, TRUE);  -- Adventure;