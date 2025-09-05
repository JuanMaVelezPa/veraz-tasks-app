--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación indexes tabla clientes
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: INDEXES
-- OBJECT NAME: clients_indexes
-- DESCRIPTION: Performance indexes for clients table
--

CREATE INDEX idx_clients_persons ON clients(persons_id);
CREATE INDEX idx_clients_type ON clients(type);
CREATE INDEX idx_clients_category ON clients(category);
CREATE INDEX idx_clients_status ON clients(status);
CREATE INDEX idx_clients_active ON clients(is_active);
CREATE INDEX idx_clients_company_name ON clients(company_name);
CREATE INDEX idx_clients_tax_id ON clients(tax_id);
CREATE INDEX idx_clients_rating ON clients(rating);
CREATE INDEX idx_clients_source ON clients(source);