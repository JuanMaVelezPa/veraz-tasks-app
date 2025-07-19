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
-- OBJECT NAME: CU_TCLIE_CONSTRAINTS
-- DESCRIPTION: Primary key, foreign keys, unique and check constraints for CU_TCLIE table
--

-- Primary Key
ALTER TABLE CU_TCLIE ADD CONSTRAINT PKY_CU_TCLIE_CLIE PRIMARY KEY (clie_clie);

-- Foreign Keys
ALTER TABLE CU_TCLIE ADD CONSTRAINT FKY_CU_TCLIE_PERS FOREIGN KEY (clie_pers) REFERENCES GE_TPERS(pers_pers);
ALTER TABLE CU_TCLIE ADD CONSTRAINT FKY_CU_TCLIE_USER FOREIGN KEY (clie_user) REFERENCES GE_TUSER(user_user);

-- Unique Constraints
ALTER TABLE CU_TCLIE ADD CONSTRAINT UQ_CU_TCLIE_CODE UNIQUE (clie_client_code);

-- Check Constraints
ALTER TABLE CU_TCLIE ADD CONSTRAINT NN_CU_TCLIE_PERS CHECK (clie_pers IS NOT NULL);
ALTER TABLE CU_TCLIE ADD CONSTRAINT NN_CU_TCLIE_CODE CHECK (clie_client_code IS NOT NULL AND LENGTH(TRIM(clie_client_code)) > 0);
ALTER TABLE CU_TCLIE ADD CONSTRAINT NN_CU_TCLIE_TYPE CHECK (clie_type IS NOT NULL AND LENGTH(TRIM(clie_type)) > 0);
ALTER TABLE CU_TCLIE ADD CONSTRAINT CK_CU_TCLIE_TYPE CHECK (clie_type IN ('INDIVIDUAL', 'CORPORATE', 'GOVERNMENT'));
ALTER TABLE CU_TCLIE ADD CONSTRAINT CK_CU_TCLIE_CATEGORY CHECK (clie_category IN ('PREMIUM', 'STANDARD', 'BASIC', 'VIP'));
ALTER TABLE CU_TCLIE ADD CONSTRAINT CK_CU_TCLIE_STATUS CHECK (clie_status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'BLACKLISTED'));
ALTER TABLE CU_TCLIE ADD CONSTRAINT CK_CU_TCLIE_RATING CHECK (clie_rating IS NULL OR (clie_rating >= 1 AND clie_rating <= 5)); 