--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación constraints tabla usuarios
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: CONSTRAINTS
-- OBJECT NAME: GE_TUSER_CONSTRAINTS
-- DESCRIPTION: Primary key, unique and check constraints for GE_TUSER table
--

-- Primary Key
ALTER TABLE GE_TUSER ADD CONSTRAINT PKY_GE_TUSER_USER PRIMARY KEY (user_user);

-- Unique Constraints
ALTER TABLE GE_TUSER ADD CONSTRAINT UQ_GE_TUSER_USERNAME UNIQUE (user_username);
ALTER TABLE GE_TUSER ADD CONSTRAINT UQ_GE_TUSER_EMAIL UNIQUE (user_email); 