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
-- OBJECT NAME: GE_TROPE
-- DESCRIPTION: Stores role-permission relationships
--
DROP TABLE IF EXISTS GE_TROPE;
CREATE TABLE GE_TROPE (
    rope_rope UUID DEFAULT GEN_RANDOM_UUID(),
    rope_role UUID NOT NULL,
    rope_perm UUID NOT NULL,
    rope_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rope_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
