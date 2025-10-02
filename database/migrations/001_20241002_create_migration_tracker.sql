--
-- MIGRATION: 001_20241002_create_migration_tracker
-- VERSION: 1.0.0
-- DATE: 02/10/2024
-- DESCRIPTION: Create migration tracking system
--


\echo '====================================================='
\echo 'APPLYING MIGRATION: 001_20241002_create_migration_tracker'
\echo '====================================================='

-- Create migration_history table
\i ../schema/tables/migration_history.sql
\i ../schema/constraints/migration_history_constraints.sql
\i ../schema/indixes/migration_history_indexes.sql

-- Insert this migration record
INSERT INTO migration_history (version, migration_number, migration_name, checksum) 
VALUES ('1.0.0', 1, '001_20241002_create_migration_tracker', 'migration_tracker_v1');

\echo '====================================================='
\echo 'MIGRATION 001 APPLIED SUCCESSFULLY'
\echo 'Migration tracking system created'
\echo '====================================================='
