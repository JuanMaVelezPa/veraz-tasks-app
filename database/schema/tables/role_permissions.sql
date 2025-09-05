--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación tabla roles-permisos
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: TABLE
-- OBJECT NAME: role_permissions
-- DESCRIPTION: Stores role-permission relationships
--

DROP TABLE IF EXISTS role_permissions;
CREATE TABLE role_permissions (
    role_permissions_id UUID DEFAULT GEN_RANDOM_UUID(),
    roles_id UUID NOT NULL,
    permissions_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);