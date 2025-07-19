--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación indexes tabla roles
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: INDEXES
-- OBJECT NAME: GE_TROLE_INDEXES
-- DESCRIPTION: Performance indexes for GE_TROLE table
--

-- Drop existing indexes if they exist
DROP INDEX IF EXISTS GE_IROLE_NAME;

-- Create indexes
CREATE INDEX GE_IROLE_NAME ON GE_TROLE(role_name);
 