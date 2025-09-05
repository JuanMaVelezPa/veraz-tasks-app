--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación tabla permisos
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: TABLE
-- OBJECT NAME: permissions
-- DESCRIPTION: Stores system permissions information
--

DROP TABLE IF EXISTS permissions;
CREATE TABLE permissions (
    permissions_id UUID DEFAULT GEN_RANDOM_UUID(),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);