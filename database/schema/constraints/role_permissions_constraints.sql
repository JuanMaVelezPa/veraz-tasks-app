--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación constraints tabla roles-permisos
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: CONSTRAINTS
-- OBJECT NAME: role_permissions_constraints
-- DESCRIPTION: Primary key, foreign keys and unique constraints for role_permissions table
--

ALTER TABLE role_permissions ADD CONSTRAINT pk_role_permissions_id PRIMARY KEY (role_permissions_id);
ALTER TABLE role_permissions ADD CONSTRAINT fk_role_permissions_roles FOREIGN KEY (roles_id) REFERENCES roles(roles_id) ON DELETE CASCADE;
ALTER TABLE role_permissions ADD CONSTRAINT fk_role_permissions_permissions FOREIGN KEY (permissions_id) REFERENCES permissions(permissions_id) ON DELETE CASCADE;
ALTER TABLE role_permissions ADD CONSTRAINT uq_role_permissions_role_perm UNIQUE (roles_id, permissions_id);