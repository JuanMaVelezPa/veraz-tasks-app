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
-- OBJECT NAME: GE_TROLE_CONSTRAINTS
-- DESCRIPTION: Primary key and unique constraints for GE_TROLE table
--

-- Primary Key
ALTER TABLE GE_TROLE ADD CONSTRAINT PKY_GE_TROLE_ROLE PRIMARY KEY (role_role);

-- Unique Constraints
ALTER TABLE GE_TROLE ADD CONSTRAINT UQ_GE_TROLE_NAME UNIQUE (role_name); 