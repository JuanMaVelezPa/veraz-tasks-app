--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación constraints tabla permisos
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: CONSTRAINTS
-- OBJECT NAME: permissions_constraints
-- DESCRIPTION: Primary key and unique constraints for permissions table
--

ALTER TABLE permissions ADD CONSTRAINT pk_permissions_id PRIMARY KEY (permissions_id);
ALTER TABLE permissions ADD CONSTRAINT uq_permissions_name UNIQUE (name);