--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación indexes tabla usuarios
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: INDEXES
-- OBJECT NAME: GE_TUSER_INDEXES
-- DESCRIPTION: Performance indexes for GE_TUSER table
--

-- Drop existing indexes if they exist
DROP INDEX IF EXISTS GE_IUSER_USERNAME;
DROP INDEX IF EXISTS GE_IUSER_EMAIL;

-- Create indexes
CREATE INDEX GE_IUSER_USERNAME ON GE_TUSER(user_username);
CREATE INDEX GE_IUSER_EMAIL ON GE_TUSER(user_email);   