--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación tabla usuarios-roles
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: TABLE
-- OBJECT NAME: user_roles
-- DESCRIPTION: Stores user-role relationships
--

DROP TABLE IF EXISTS user_roles;
CREATE TABLE user_roles (
    user_roles_id UUID DEFAULT GEN_RANDOM_UUID(),
    users_id UUID NOT NULL,
    roles_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);