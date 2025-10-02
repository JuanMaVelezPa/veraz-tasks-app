--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         02/10/2024       Creación constraints tabla migration_history
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: CONSTRAINT
-- OBJECT NAME: migration_history_constraints
-- DESCRIPTION: Constraints for migration_history table
--


-- Primary Key Constraint
ALTER TABLE migration_history ADD CONSTRAINT pk_migration_history_id PRIMARY KEY (migration_id);

-- Unique Constraints
ALTER TABLE migration_history ADD CONSTRAINT uq_migration_history_number 
    UNIQUE (migration_number);

ALTER TABLE migration_history ADD CONSTRAINT uq_migration_history_name 
    UNIQUE (migration_name);

-- Check Constraints
ALTER TABLE migration_history ADD CONSTRAINT ck_migration_history_number 
    CHECK (migration_number > 0);

-- Not Null Constraints
ALTER TABLE migration_history ALTER COLUMN version SET NOT NULL;
ALTER TABLE migration_history ALTER COLUMN migration_number SET NOT NULL;
ALTER TABLE migration_history ALTER COLUMN migration_name SET NOT NULL;
