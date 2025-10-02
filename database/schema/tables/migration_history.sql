--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         02/10/2024       Creación tabla migration_history
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: TABLE
-- OBJECT NAME: migration_history
-- DESCRIPTION: Tracks applied database migrations and versions
--


DROP TABLE IF EXISTS migration_history;
CREATE TABLE migration_history (
    migration_id SERIAL PRIMARY KEY,
    version VARCHAR(20) NOT NULL,
    migration_number INTEGER NOT NULL,
    migration_name VARCHAR(200) NOT NULL,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    applied_by VARCHAR(100) DEFAULT current_user,
    checksum VARCHAR(64),
    rollback_available BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
