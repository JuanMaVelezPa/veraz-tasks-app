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
-- OBJECT NAME: GE_TUSRO
-- DESCRIPTION: Stores user-role relationships
--
DROP TABLE IF EXISTS GE_TUSRO;
CREATE TABLE GE_TUSRO (
    usro_usro UUID DEFAULT GEN_RANDOM_UUID(),
    usro_user UUID NOT NULL,
    usro_role UUID NOT NULL,
    usro_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usro_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);