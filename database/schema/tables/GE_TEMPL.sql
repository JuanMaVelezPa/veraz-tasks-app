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
-- OBJECT NAME: GE_TEMPL
-- DESCRIPTION: Stores employee information linked to persons and users
--
DROP TABLE IF EXISTS GE_TEMPL;
CREATE TABLE GE_TEMPL (
    empl_empl UUID DEFAULT GEN_RANDOM_UUID(),
    empl_pers UUID NOT NULL,
    empl_employee_code VARCHAR(20) NOT NULL,
    empl_position VARCHAR(100) NOT NULL,
    empl_department VARCHAR(100),
    empl_supervisor UUID,
    empl_hire_date DATE NOT NULL,
    empl_termination_date DATE,
    empl_salary DECIMAL(12,2),
    empl_currency VARCHAR(3),
    empl_employment_type VARCHAR(20) NOT NULL,
    empl_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    empl_work_email VARCHAR(100),
    empl_work_mobile VARCHAR(20),
    empl_work_location VARCHAR(100),
    empl_work_schedule VARCHAR(100),
    empl_skills TEXT,
    empl_certifications TEXT,
    empl_education TEXT,
    empl_benefits TEXT,
    empl_notes TEXT,
    empl_is_active BOOLEAN DEFAULT TRUE,
    empl_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    empl_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 