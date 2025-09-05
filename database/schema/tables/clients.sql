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
-- OBJECT NAME: clients
-- DESCRIPTION: Stores client information linked to persons and users
--

DROP TABLE IF EXISTS clients;
CREATE TABLE clients (
    clients_id UUID DEFAULT GEN_RANDOM_UUID(),
    persons_id UUID NOT NULL,
    type VARCHAR(20) NOT NULL,
    category VARCHAR(50),
    source VARCHAR(50),
    company_name VARCHAR(200),
    company_website VARCHAR(255),
    company_industry VARCHAR(100),
    contact_person VARCHAR(200),
    contact_position VARCHAR(100),
    address VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(20),
    tax_id VARCHAR(50),
    credit_limit DECIMAL(20,2),
    currency VARCHAR(3),
    payment_terms VARCHAR(50),
    payment_method VARCHAR(50),
    notes TEXT,
    preferences TEXT,
    tags TEXT,
    rating INTEGER,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);