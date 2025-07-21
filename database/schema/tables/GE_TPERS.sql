--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación tabla personas
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: TABLE
-- OBJECT NAME: GE_TPERS
-- DESCRIPTION: Stores complete person information (natural persons)
--
DROP TABLE IF EXISTS GE_TPERS;
CREATE TABLE GE_TPERS (
    pers_pers UUID DEFAULT GEN_RANDOM_UUID(),
    pers_user UUID,
    pers_ident_type VARCHAR(20) NOT NULL,
    pers_ident_number VARCHAR(20) NOT NULL,
    pers_first_name VARCHAR(100) NOT NULL,
    pers_last_name VARCHAR(100) NOT NULL,
    pers_birth_date DATE,
    pers_gender VARCHAR(3),
    pers_nationality VARCHAR(50),
    pers_mobile VARCHAR(20),
    pers_email VARCHAR(100),
    pers_address VARCHAR(255),
    pers_city VARCHAR(100),
    pers_country VARCHAR(100),
    pers_postal_code VARCHAR(20),
    pers_notes TEXT,
    pers_is_active BOOLEAN DEFAULT TRUE,
    pers_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    pers_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 