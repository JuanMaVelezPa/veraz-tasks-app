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
-- OBJECT NAME: employees_constraints
-- DESCRIPTION: Primary key, foreign keys, unique and check constraints for employees table
--

-- PRIMARY KEY
ALTER TABLE employees ADD CONSTRAINT pk_employees_id PRIMARY KEY (employees_id);

-- FOREIGN KEYS
ALTER TABLE employees ADD CONSTRAINT fk_employees_persons FOREIGN KEY (persons_id) REFERENCES persons(persons_id);

-- CHECK CONSTRAINTS PARA CAMPOS OBLIGATORIOS
ALTER TABLE employees ADD CONSTRAINT ck_employees_position CHECK (LENGTH(TRIM(position)) > 0);
ALTER TABLE employees ADD CONSTRAINT ck_employees_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'TERMINATED', 'ON_LEAVE'));
ALTER TABLE employees ADD CONSTRAINT ck_employees_salary CHECK (salary > 0);
ALTER TABLE employees ADD CONSTRAINT ck_employees_currency CHECK (LENGTH(currency) = 3);
ALTER TABLE employees ADD CONSTRAINT ck_employees_salary_type CHECK (salary_type IN ('HOURLY', 'MONTHLY', 'ANNUAL'));

-- CHECK CONSTRAINTS PARA CAMPOS OPCIONALES
ALTER TABLE employees ADD CONSTRAINT ck_employees_employment_type CHECK (employment_type IS NULL OR employment_type IN ('FULL_TIME', 'PART_TIME', 'CONTRACTOR', 'INTERN', 'FREELANCER'));
ALTER TABLE employees ADD CONSTRAINT ck_employees_dates CHECK (termination_date IS NULL OR termination_date >= hire_date);
ALTER TABLE employees ADD CONSTRAINT ck_employees_probation CHECK (probation_end_date IS NULL OR probation_end_date >= hire_date);
ALTER TABLE employees ADD CONSTRAINT ck_employees_job_level CHECK (job_level IS NULL OR job_level IN ('JUNIOR', 'MID', 'SENIOR', 'LEAD', 'MANAGER', 'DIRECTOR', 'EXECUTIVE'));
ALTER TABLE employees ADD CONSTRAINT ck_employees_work_shift CHECK (work_shift IS NULL OR work_shift IN ('DAY', 'NIGHT', 'ROTATING', 'FLEXIBLE'));