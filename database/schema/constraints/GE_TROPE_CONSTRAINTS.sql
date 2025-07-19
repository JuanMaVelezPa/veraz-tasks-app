--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación constraints tabla roles-permisos
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: CONSTRAINTS
-- OBJECT NAME: GE_TROPE_CONSTRAINTS
-- DESCRIPTION: Primary key, foreign keys and unique constraints for GE_TROPE table
--

-- Primary Key
ALTER TABLE GE_TROPE ADD CONSTRAINT PKY_GE_TROPE_ROPE PRIMARY KEY (rope_rope);

-- Foreign Keys
ALTER TABLE GE_TROPE ADD CONSTRAINT FKY_GE_TROPE_ROLE FOREIGN KEY (rope_role) REFERENCES GE_TROLE(role_role) ON DELETE CASCADE;
ALTER TABLE GE_TROPE ADD CONSTRAINT FKY_GE_TROPE_PERM FOREIGN KEY (rope_perm) REFERENCES GE_TPERM(perm_perm) ON DELETE CASCADE;

-- Unique Constraints
ALTER TABLE GE_TROPE ADD CONSTRAINT UQ_GE_TROPE_ROLE_PERM UNIQUE (rope_role, rope_perm); 