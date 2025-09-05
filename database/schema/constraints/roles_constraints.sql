--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación constraints tabla roles
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: CONSTRAINTS
-- OBJECT NAME: roles_constraints
-- DESCRIPTION: Primary key and unique constraints for roles table
--

ALTER TABLE roles ADD CONSTRAINT pk_roles_id PRIMARY KEY (roles_id);
ALTER TABLE roles ADD CONSTRAINT uq_roles_name UNIQUE (name);