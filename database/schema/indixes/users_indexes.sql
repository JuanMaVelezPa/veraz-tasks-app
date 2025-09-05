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
-- OBJECT NAME: users_indexes
-- DESCRIPTION: Performance indexes for users table
--

CREATE INDEX idx_users_username ON users (username);
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_is_active ON users (is_active);