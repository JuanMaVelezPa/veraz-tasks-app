--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         02/10/2024       Creación índices tabla migration_history
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: INDEX
-- OBJECT NAME: migration_history_indexes
-- DESCRIPTION: Indexes for migration_history table
--


-- Index on migration number for ordering queries
CREATE INDEX idx_migration_history_number ON migration_history (migration_number);

-- Index on version for version-based queries
CREATE INDEX idx_migration_history_version ON migration_history (version);

-- Index on applied_at for chronological queries
CREATE INDEX idx_migration_history_applied_at ON migration_history (applied_at);

-- Index on is_active for filtering active migrations
CREATE INDEX idx_migration_history_is_active ON migration_history (is_active);

-- Composite index for version and number queries
CREATE INDEX idx_migration_history_version_number ON migration_history (version, migration_number);
