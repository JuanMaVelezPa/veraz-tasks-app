--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación indexes tabla usuarios-roles
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: INDEXES
-- OBJECT NAME: user_roles_indexes
-- DESCRIPTION: Performance indexes for user_roles table
--

CREATE INDEX idx_user_roles_users ON user_roles(users_id);
CREATE INDEX idx_user_roles_roles ON user_roles(roles_id);
CREATE INDEX idx_user_roles_user_role ON user_roles(users_id, roles_id);