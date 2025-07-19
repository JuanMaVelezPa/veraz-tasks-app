--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación indexes tabla personas
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: INDEXES
-- OBJECT NAME: GE_TPERS_INDEXES
-- DESCRIPTION: Performance indexes for GE_TPERS table
--

-- Drop existing indexes if they exist
DROP INDEX IF EXISTS GE_IPERS_IDENT;
DROP INDEX IF EXISTS GE_IPERS_FULL_NAME;
DROP INDEX IF EXISTS GE_IPERS_EMAIL;
DROP INDEX IF EXISTS GE_IPERS_MOBILE;
DROP INDEX IF EXISTS GE_IPERS_BIRTH_DATE;

-- Create indexes
CREATE INDEX GE_IPERS_IDENT ON GE_TPERS(pers_ident_type, pers_ident_number);
CREATE INDEX GE_IPERS_FULL_NAME ON GE_TPERS(pers_first_name, pers_last_name);
CREATE INDEX GE_IPERS_EMAIL ON GE_TPERS(pers_email);
CREATE INDEX GE_IPERS_MOBILE ON GE_TPERS(pers_mobile);
CREATE INDEX GE_IPERS_BIRTH_DATE ON GE_TPERS(pers_birth_date); 