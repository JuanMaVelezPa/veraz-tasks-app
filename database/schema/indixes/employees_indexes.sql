--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación indexes tabla empleados
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: INDEXES
-- OBJECT NAME: employees_indexes
-- DESCRIPTION: Performance indexes for employees table
--

CREATE INDEX idx_employees_persons ON employees(persons_id);
CREATE INDEX idx_employees_position ON employees(position);
CREATE INDEX idx_employees_department ON employees(department);
CREATE INDEX idx_employees_hire_date ON employees(hire_date);
CREATE INDEX idx_employees_status ON employees(status);
CREATE INDEX idx_employees_work_email ON employees(work_email);
CREATE INDEX idx_employees_employment_type ON employees(employment_type);
CREATE INDEX idx_employees_job_level ON employees(job_level);
CREATE INDEX idx_employees_work_shift ON employees(work_shift);
CREATE INDEX idx_employees_active ON employees(is_active);