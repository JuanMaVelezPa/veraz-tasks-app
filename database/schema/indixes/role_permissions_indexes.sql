--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación indexes tabla roles-permisos
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: INDEXES
-- OBJECT NAME: role_permissions_indexes
-- DESCRIPTION: Performance indexes for role_permissions table
--

CREATE INDEX idx_role_permissions_roles ON role_permissions(roles_id);
CREATE INDEX idx_role_permissions_permissions ON role_permissions(permissions_id);
CREATE INDEX idx_role_permissions_role_perm ON role_permissions(roles_id, permissions_id);