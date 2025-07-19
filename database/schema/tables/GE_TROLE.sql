--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación tabla roles
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: TABLE
-- OBJECT NAME: GE_TROLE
-- DESCRIPTION: Stores system roles information
--
DROP TABLE IF EXISTS GE_TROLE;
CREATE TABLE GE_TROLE (
    role_role UUID DEFAULT GEN_RANDOM_UUID(),
    role_name VARCHAR(50) NOT NULL,
    role_desc TEXT,
    role_is_active BOOLEAN DEFAULT TRUE,
    role_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);