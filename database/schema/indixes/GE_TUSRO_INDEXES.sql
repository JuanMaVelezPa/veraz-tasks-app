--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación indexes tabla usuarios-roles
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: INDEXES
-- OBJECT NAME: GE_TUSRO_INDEXES
-- DESCRIPTION: Performance indexes for GE_TUSRO table
--

-- Drop existing indexes if they exist
DROP INDEX IF EXISTS GE_IUSRO_USER;
DROP INDEX IF EXISTS GE_IUSRO_ROLE;
DROP INDEX IF EXISTS GE_IUSRO_USER_ROLE;

-- Create indexes
CREATE INDEX GE_IUSRO_USER ON GE_TUSRO(usro_user);
CREATE INDEX GE_IUSRO_ROLE ON GE_TUSRO(usro_role);
CREATE INDEX GE_IUSRO_USER_ROLE ON GE_TUSRO(usro_user, usro_role); 