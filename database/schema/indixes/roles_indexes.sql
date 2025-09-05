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
-- OBJECT NAME: roles_indexes
-- DESCRIPTION: Performance indexes for roles table
--

CREATE INDEX idx_roles_name ON roles(name);