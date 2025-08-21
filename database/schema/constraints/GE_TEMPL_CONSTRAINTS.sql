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

-- Unique Constraints
ALTER TABLE GE_TEMPL ADD CONSTRAINT UQ_GE_TEMPL_CODE UNIQUE (empl_employee_code);

-- Check Constraints
ALTER TABLE GE_TEMPL ADD CONSTRAINT NN_GE_TEMPL_PERS CHECK (empl_pers IS NOT NULL);
ALTER TABLE GE_TEMPL ADD CONSTRAINT NN_GE_TEMPL_CODE CHECK (empl_employee_code IS NOT NULL AND LENGTH(TRIM(empl_employee_code)) > 0);
ALTER TABLE GE_TEMPL ADD CONSTRAINT NN_GE_TEMPL_POSITION CHECK (empl_position IS NOT NULL AND LENGTH(TRIM(empl_position)) > 0);
ALTER TABLE GE_TEMPL ADD CONSTRAINT NN_GE_TEMPL_HIRE_DATE CHECK (empl_hire_date IS NOT NULL);
ALTER TABLE GE_TEMPL ADD CONSTRAINT CK_GE_TEMPL_EMPLOYMENT_TYPE CHECK (empl_employment_type IN ('FULL_TIME', 'PART_TIME', 'CONTRACTOR', 'INTERN', 'FREELANCER'));
ALTER TABLE GE_TEMPL ADD CONSTRAINT CK_GE_TEMPL_STATUS CHECK (empl_status IN ('ACTIVE', 'INACTIVE', 'TERMINATED', 'ON_LEAVE'));
ALTER TABLE GE_TEMPL ADD CONSTRAINT CK_GE_TEMPL_SALARY CHECK (empl_salary IS NULL OR empl_salary >= 0);
ALTER TABLE GE_TEMPL ADD CONSTRAINT CK_GE_TEMPL_DATES CHECK (empl_termination_date IS NULL OR empl_termination_date >= empl_hire_date);
ALTER TABLE GE_TEMPL ADD CONSTRAINT CK_GE_TEMPL_PROBATION CHECK (empl_probation_end_date IS NULL OR empl_probation_end_date >= empl_hire_date);
ALTER TABLE GE_TEMPL ADD CONSTRAINT CK_GE_TEMPL_CURRENCY CHECK (empl_currency IS NULL OR LENGTH(empl_currency) = 3);
ALTER TABLE GE_TEMPL ADD CONSTRAINT CK_GE_TEMPL_SALARY_TYPE CHECK (empl_salary_type IS NULL OR empl_salary_type IN ('HOURLY', 'MONTHLY', 'ANNUAL'));
ALTER TABLE GE_TEMPL ADD CONSTRAINT CK_GE_TEMPL_JOB_LEVEL CHECK (empl_job_level IS NULL OR empl_job_level IN ('JUNIOR', 'MID', 'SENIOR', 'LEAD', 'MANAGER', 'DIRECTOR', 'EXECUTIVE'));
ALTER TABLE GE_TEMPL ADD CONSTRAINT CK_GE_TEMPL_WORK_SHIFT CHECK (empl_work_shift IS NULL OR empl_work_shift IN ('DAY', 'NIGHT', 'ROTATING', 'FLEXIBLE'));