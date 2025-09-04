--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación constraints tabla clientes
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: CONSTRAINTS
-- OBJECT NAME: GE_TCLIE_CONSTRAINTS
-- DESCRIPTION: Primary key, foreign keys, unique and check constraints for GE _TCLIE table
--
ALTER TABLE GE_TCLIE ADD CONSTRAINT PKY_GE_TCLIE_CLIE PRIMARY KEY (clie_clie);
ALTER TABLE GE_TCLIE ADD CONSTRAINT FKY_GE_TCLIE_PERS FOREIGN KEY (clie_pers) REFERENCES GE_TPERS(pers_pers);
ALTER TABLE GE_TCLIE ADD CONSTRAINT CK_GE_TCLIE_TYPE CHECK (LENGTH(TRIM(clie_type)) > 0);
ALTER TABLE GE_TCLIE ADD CONSTRAINT CK_GE_TCLIE_TYPE CHECK (clie_type IN ('INDIVIDUAL', 'CORPORATE', 'GOVERNMENT'));
ALTER TABLE GE_TCLIE ADD CONSTRAINT CK_GE_TCLIE_CATEGORY CHECK (clie_category IN ('STANDARD', 'PREMIUM'));
ALTER TABLE GE_TCLIE ADD CONSTRAINT CK_GE_TCLIE_STATUS CHECK (clie_status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'BLACKLISTED'));
ALTER TABLE GE_TCLIE ADD CONSTRAINT CK_GE_TCLIE_RATING CHECK ((clie_rating >= 1 AND clie_rating <= 5)); 