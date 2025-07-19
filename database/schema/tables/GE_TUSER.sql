--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación tabla usuarios
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: TABLE
-- OBJECT NAME: GE_TUSER
-- DESCRIPTION: Stores system user information
--
DROP TABLE IF EXISTS GE_TUSER;
CREATE TABLE GE_TUSER (
    user_user UUID DEFAULT GEN_RANDOM_UUID(),
    user_username VARCHAR(50) NOT NULL,
    user_email VARCHAR(100) NOT NULL,
    user_pwd VARCHAR(255) NOT NULL,
    user_is_active BOOLEAN DEFAULT TRUE,
    user_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);