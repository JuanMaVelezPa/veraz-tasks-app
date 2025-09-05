--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación indexes tabla personas
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: INDEXES
-- OBJECT NAME: persons_indexes
-- DESCRIPTION: Performance indexes for persons table
--

CREATE INDEX idx_persons_ident ON persons(ident_type, ident_number);
CREATE INDEX idx_persons_full_name ON persons(first_name, last_name);
CREATE INDEX idx_persons_email ON persons(email);
CREATE INDEX idx_persons_mobile ON persons(mobile);
CREATE INDEX idx_persons_birth_date ON persons(birth_date);