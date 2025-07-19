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
-- OBJECT NAME: GE_TPERM
-- DESCRIPTION: Stores system permissions information
--
DROP TABLE IF EXISTS GE_TPERM;
CREATE TABLE GE_TPERM (
    perm_perm UUID DEFAULT GEN_RANDOM_UUID(),
    perm_name VARCHAR(100) NOT NULL,
    perm_descr TEXT,
    perm_is_active BOOLEAN DEFAULT TRUE,
    perm_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    perm_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);