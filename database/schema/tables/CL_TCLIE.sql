--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación tabla clientes
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: TABLE
-- OBJECT NAME: CL_TCLIE
-- DESCRIPTION: Stores client information linked to persons and users
--
DROP TABLE IF EXISTS CL_TCLIE;
CREATE TABLE CL_TCLIE (
    clie_clie UUID DEFAULT GEN_RANDOM_UUID(),
    clie_pers UUID NOT NULL,
    clie_client_code VARCHAR(20) NOT NULL,
    clie_type VARCHAR(20) NOT NULL,
    clie_category VARCHAR(50),
    clie_source VARCHAR(50),
    clie_company_name VARCHAR(200),
    clie_company_website VARCHAR(255),
    clie_company_industry VARCHAR(100),
    clie_contact_person VARCHAR(200),
    clie_contact_position VARCHAR(100),
    clie_address VARCHAR(255),
    clie_city VARCHAR(100),
    clie_country VARCHAR(100),
    clie_postal_code VARCHAR(20),
    clie_tax_id VARCHAR(50),
    clie_credit_limit DECIMAL(20,2),
    clie_currency VARCHAR(3),
    clie_payment_terms VARCHAR(50),
    clie_payment_method VARCHAR(50),
    clie_notes TEXT,
    clie_preferences TEXT,
    clie_tags TEXT,
    clie_rating INTEGER,
    clie_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    clie_is_active BOOLEAN DEFAULT TRUE,
    clie_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    clie_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 