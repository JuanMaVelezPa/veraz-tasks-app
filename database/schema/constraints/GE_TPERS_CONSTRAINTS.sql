--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación constraints tabla personas
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: CONSTRAINTS
-- OBJECT NAME: GE_TPERS_CONSTRAINTS
-- DESCRIPTION: Primary key, unique and check constraints for GE_TPERS table
--

-- Primary Key
ALTER TABLE GE_TPERS ADD CONSTRAINT PKY_GE_TPERS_PERS PRIMARY KEY (pers_pers);

-- Foreign Key
ALTER TABLE GE_TPERS ADD CONSTRAINT FK_GE_TPERS_USER FOREIGN KEY (pers_user) REFERENCES GE_TUSER(user_user);

-- Unique Constraints
ALTER TABLE GE_TPERS ADD CONSTRAINT UQ_GE_TPERS_IDENT UNIQUE (pers_ident_type, pers_ident_number);

-- Check Constraints
ALTER TABLE GE_TPERS ADD CONSTRAINT NN_GE_TPERS_FIRST_NAME CHECK (pers_first_name IS NOT NULL AND LENGTH(TRIM(pers_first_name)) > 0);
ALTER TABLE GE_TPERS ADD CONSTRAINT NN_GE_TPERS_LAST_NAME CHECK (pers_last_name IS NOT NULL AND LENGTH(TRIM(pers_last_name)) > 0);
ALTER TABLE GE_TPERS ADD CONSTRAINT CK_GE_TPERS_GENDER CHECK (pers_gender IN ('M', 'F', 'O')); 
ALTER TABLE GE_TPERS ADD CONSTRAINT NN_GE_TPERS_GENDER CHECK (pers_gender IS NOT NULL);
ALTER TABLE GE_TPERS ADD CONSTRAINT NN_GE_TPERS_IDENT_TYPE CHECK (pers_ident_type IS NOT NULL);
ALTER TABLE GE_TPERS ADD CONSTRAINT NN_GE_TPERS_IDENT_NUMBER CHECK (pers_ident_number IS NOT NULL);