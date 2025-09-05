--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación constraints tabla clientes
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: CONSTRAINTS
-- OBJECT NAME: clients_constraints
-- DESCRIPTION: Primary key, foreign keys, unique and check constraints for clients table
--

ALTER TABLE clients ADD CONSTRAINT pk_clients_id PRIMARY KEY (clients_id);
ALTER TABLE clients ADD CONSTRAINT fk_clients_persons FOREIGN KEY (persons_id) REFERENCES persons(persons_id);
ALTER TABLE clients ADD CONSTRAINT ck_clients_type CHECK (LENGTH(TRIM(type)) > 0);
ALTER TABLE clients ADD CONSTRAINT ck_clients_type_values CHECK (type IN ('INDIVIDUAL', 'CORPORATE', 'GOVERNMENT'));
ALTER TABLE clients ADD CONSTRAINT ck_clients_category CHECK (category IN ('STANDARD', 'PREMIUM'));
ALTER TABLE clients ADD CONSTRAINT ck_clients_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'BLACKLISTED'));
ALTER TABLE clients ADD CONSTRAINT ck_clients_rating CHECK ((rating >= 1 AND rating <= 5));