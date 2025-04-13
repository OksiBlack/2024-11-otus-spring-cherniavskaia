--changeset chern:2025-04-06-acl_sid
CREATE TABLE acl_sid
(
  id        BIGSERIAL NOT NULL PRIMARY KEY,
  principal BOOLEAN NOT NULL,
  sid       VARCHAR(100) NOT NULL,
  CONSTRAINT unique_sid_principal UNIQUE(sid, principal)
);

--changeset chern:2025-04-06-acl_class
CREATE TABLE acl_class
(
  id    BIGSERIAL NOT NULL PRIMARY KEY,
  class VARCHAR(100) NOT NULL,
  class_id_type varchar(100),

  CONSTRAINT unique_class UNIQUE(class)
);

--changeset chern:2025-04-06-acl_object_identity
CREATE TABLE acl_object_identity
(
  id                 BIGSERIAL PRIMARY KEY,
  object_id_class    BIGINT NOT NULL,
  object_id_identity VARCHAR(36) NOT NULL,
  parent_object      BIGINT,
  owner_sid          BIGINT,
  entries_inheriting BOOLEAN NOT NULL,
  CONSTRAINT unique_object_identity UNIQUE(object_id_class, object_id_identity),
  CONSTRAINT fk_parent_object FOREIGN KEY(parent_object) REFERENCES acl_object_identity(id),
  CONSTRAINT fk_object_id_class FOREIGN KEY(object_id_class) REFERENCES acl_class(id),
  CONSTRAINT fk_owner_sid FOREIGN KEY(owner_sid) REFERENCES acl_sid(id)
);

--changeset chern:2025-04-06-acl_entry
CREATE TABLE acl_entry
(
  id                  BIGSERIAL PRIMARY KEY,
  acl_object_identity BIGINT NOT NULL,
  ace_order           INT NOT NULL,
  sid                 BIGINT NOT NULL,
  mask                INTEGER NOT NULL,
  granting            BOOLEAN NOT NULL,
  audit_success       BOOLEAN NOT NULL,
  audit_failure       BOOLEAN NOT NULL,
--   CONSTRAINT unique_acl_object_identity_ace_order UNIQUE(acl_object_identity, ace_order),
  CONSTRAINT fk_acl_object_identity FOREIGN KEY(acl_object_identity) REFERENCES acl_object_identity(id),
  CONSTRAINT fk_sid FOREIGN KEY(sid) REFERENCES acl_sid(id)
);
