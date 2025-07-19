--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación indexes tabla permisos
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: INDEXES
-- OBJECT NAME: GE_TPERM_INDEXES
-- DESCRIPTION: Performance indexes for GE_TPERM table
--

-- Drop existing indexes if they exist
DROP INDEX IF EXISTS GE_IPERM_NAME;

-- Create indexes
CREATE INDEX GE_IPERM_NAME ON GE_TPERM(perm_name);