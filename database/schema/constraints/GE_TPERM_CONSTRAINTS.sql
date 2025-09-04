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
-- OBJECT NAME: GE_TPERM_CONSTRAINTS
-- DESCRIPTION: Primary key and unique constraints for GE_TPERM table
--
ALTER TABLE GE_TPERM ADD CONSTRAINT PKY_GE_TPERM_PERM PRIMARY KEY (perm_perm);
ALTER TABLE GE_TPERM ADD CONSTRAINT UQ_GE_TPERM_NAME UNIQUE (perm_name); 