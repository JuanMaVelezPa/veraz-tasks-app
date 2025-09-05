--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación tabla empleados
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: TABLE
-- OBJECT NAME: employees
-- DESCRIPTION: Stores employment information linked to persons
--

DROP TABLE IF EXISTS employees;
CREATE TABLE employees (
    employees_id UUID DEFAULT GEN_RANDOM_UUID(),
    persons_id UUID NOT NULL,
    position VARCHAR(100) NOT NULL,
    department VARCHAR(100),
    employment_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    hire_date DATE NOT NULL,
    termination_date DATE,
    probation_end_date DATE,
    salary DECIMAL(12,2),
    currency VARCHAR(3) DEFAULT 'USD',
    salary_type VARCHAR(20),
    work_email VARCHAR(100),
    work_phone VARCHAR(20),
    work_location VARCHAR(100),
    work_schedule VARCHAR(100),
    job_level VARCHAR(20),
    cost_center VARCHAR(50),
    work_shift VARCHAR(20),
    skills TEXT,
    certifications TEXT,
    education TEXT,
    benefits TEXT,
    notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID
);