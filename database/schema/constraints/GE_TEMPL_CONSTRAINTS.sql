--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación constraints tabla empleados
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: CONSTRAINTS
-- OBJECT NAME: GE_TEMPL_CONSTRAINTS
-- DESCRIPTION: Primary key, foreign keys, unique and check constraints for GE_TEMPL table
--

-- Primary Key
ALTER TABLE GE_TEMPL ADD CONSTRAINT PKY_GE_TEMPL_EMPL PRIMARY KEY (empl_empl);

-- Foreign Keys
ALTER TABLE GE_TEMPL ADD CONSTRAINT FKY_GE_TEMPL_PERS FOREIGN KEY (empl_pers) REFERENCES GE_TPERS(pers_pers);
ALTER TABLE GE_TEMPL ADD CONSTRAINT FKY_GE_TEMPL_USER FOREIGN KEY (empl_user) REFERENCES GE_TUSER(user_user);
ALTER TABLE GE_TEMPL ADD CONSTRAINT FKY_GE_TEMPL_SUPERVISOR FOREIGN KEY (empl_supervisor) REFERENCES GE_TEMPL(empl_empl);

-- Unique Constraints
ALTER TABLE GE_TEMPL ADD CONSTRAINT UQ_GE_TEMPL_CODE UNIQUE (empl_employee_code);

-- Check Constraints
ALTER TABLE GE_TEMPL ADD CONSTRAINT NN_GE_TEMPL_PERS CHECK (empl_pers IS NOT NULL);
ALTER TABLE GE_TEMPL ADD CONSTRAINT NN_GE_TEMPL_CODE CHECK (empl_employee_code IS NOT NULL AND LENGTH(TRIM(empl_employee_code)) > 0);
ALTER TABLE GE_TEMPL ADD CONSTRAINT NN_GE_TEMPL_POSITION CHECK (empl_position IS NOT NULL AND LENGTH(TRIM(empl_position)) > 0);
ALTER TABLE GE_TEMPL ADD CONSTRAINT NN_GE_TEMPL_HIRE_DATE CHECK (empl_hire_date IS NOT NULL);
ALTER TABLE GE_TEMPL ADD CONSTRAINT CK_GE_TEMPL_EMPLOYMENT_TYPE CHECK (empl_employment_type IN ('FULL_TIME', 'PART_TIME', 'CONTRACT', 'INTERN'));
ALTER TABLE GE_TEMPL ADD CONSTRAINT CK_GE_TEMPL_STATUS CHECK (empl_status IN ('ACTIVE', 'INACTIVE', 'TERMINATED', 'SUSPENDED'));
ALTER TABLE GE_TEMPL ADD CONSTRAINT CK_GE_TEMPL_SALARY CHECK (empl_salary IS NULL OR empl_salary >= 0);
ALTER TABLE GE_TEMPL ADD CONSTRAINT CK_GE_TEMPL_DATES CHECK (empl_termination_date IS NULL OR empl_termination_date >= empl_hire_date); 