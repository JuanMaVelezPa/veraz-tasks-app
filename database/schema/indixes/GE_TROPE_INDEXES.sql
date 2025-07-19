--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación indexes tabla roles-permisos
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: INDEXES
-- OBJECT NAME: GE_TROPE_INDEXES
-- DESCRIPTION: Performance indexes for GE_TROPE table
--

-- Drop existing indexes if they exist
DROP INDEX IF EXISTS GE_IROPE_ROLE;
DROP INDEX IF EXISTS GE_IROPE_PERM;
DROP INDEX IF EXISTS GE_IROPE_ROLE_PERM;

-- Create indexes
CREATE INDEX GE_IROPE_ROLE ON GE_TROPE(rope_role);
CREATE INDEX GE_IROPE_PERM ON GE_TROPE(rope_perm);
CREATE INDEX GE_IROPE_ROLE_PERM ON GE_TROPE(rope_role, rope_perm); 