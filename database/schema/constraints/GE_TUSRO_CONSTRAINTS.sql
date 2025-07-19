--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación constraints tabla usuarios-roles
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: CONSTRAINTS
-- OBJECT NAME: GE_TUSRO_CONSTRAINTS
-- DESCRIPTION: Primary key, foreign keys and unique constraints for GE_TUSRO table
--

-- Primary Key
ALTER TABLE GE_TUSRO ADD CONSTRAINT PKY_GE_TUSRO_USRO PRIMARY KEY (usro_usro);

-- Foreign Keys
ALTER TABLE GE_TUSRO ADD CONSTRAINT FKY_GE_TUSRO_USER FOREIGN KEY (usro_user) REFERENCES GE_TUSER(user_user) ON DELETE CASCADE;
ALTER TABLE GE_TUSRO ADD CONSTRAINT FKY_GE_TUSRO_ROLE FOREIGN KEY (usro_role) REFERENCES GE_TROLE(role_role) ON DELETE CASCADE;

-- Unique Constraints
ALTER TABLE GE_TUSRO ADD CONSTRAINT UQ_GE_TUSRO_USER_ROLE UNIQUE (usro_user, usro_role); 