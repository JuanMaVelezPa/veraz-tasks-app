--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Test data for employees and clients
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: SEED DATA
-- OBJECT NAME: 03_test_data_employees_clients
-- DESCRIPTION: Test data for persons, employees, clients and their users
--

-- =====================================================
-- STEP 1: INSERT PERSONS DATA
-- =====================================================

-- Insert persons for managers
INSERT INTO GE_TPERS (pers_ident_type, pers_ident_number, pers_first_name, pers_last_name, pers_email, pers_mobile, pers_address, pers_city, pers_country, pers_postal_code, pers_birth_date, pers_gender, pers_nationality) VALUES
('PASSPORT', 'CA123456789', 'Michael', 'Johnson', 'michael.johnson@construction.ca', '+1-416-555-0101', '123 Queen Street', 'Toronto', 'Canada', 'M5H 2M9', '1985-03-15', 'M', 'Canadian'),
('PASSPORT', 'CA987654321', 'Sarah', 'Williams', 'sarah.williams@construction.ca', '+1-416-555-0102', '456 King Street', 'Toronto', 'Canada', 'M5V 3A8', '1988-07-22', 'F', 'Canadian');

-- Insert persons for supervisors
INSERT INTO GE_TPERS (pers_ident_type, pers_ident_number, pers_first_name, pers_last_name, pers_email, pers_mobile, pers_address, pers_city, pers_country, pers_postal_code, pers_birth_date, pers_gender, pers_nationality) VALUES
('PASSPORT', 'CA111222333', 'David', 'Brown', 'david.brown@construction.ca', '+1-416-555-0103', '789 Bay Street', 'Toronto', 'Canada', 'M5G 2C3', '1990-11-08', 'M', 'Canadian'),
('PASSPORT', 'CA444555666', 'Jennifer', 'Davis', 'jennifer.davis@construction.ca', '+1-416-555-0104', '321 Yonge Street', 'Toronto', 'Canada', 'M5B 2L7', '1992-04-12', 'F', 'Canadian');

-- Insert persons for employees
INSERT INTO GE_TPERS (pers_ident_type, pers_ident_number, pers_first_name, pers_last_name, pers_email, pers_mobile, pers_address, pers_city, pers_country, pers_postal_code, pers_birth_date, pers_gender, pers_nationality) VALUES
('PASSPORT', 'CA777888999', 'Robert', 'Miller', 'robert.miller@construction.ca', '+1-416-555-0105', '654 Bloor Street', 'Toronto', 'Canada', 'M6G 1L3', '1995-01-20', 'M', 'Canadian'),
('PASSPORT', 'CA000111222', 'Lisa', 'Wilson', 'lisa.wilson@construction.ca', '+1-416-555-0106', '987 College Street', 'Toronto', 'Canada', 'M6G 1A5', '1993-08-14', 'F', 'Canadian'),
('PASSPORT', 'CA333444555', 'James', 'Taylor', 'james.taylor@construction.ca', '+1-416-555-0107', '147 Spadina Avenue', 'Toronto', 'Canada', 'M5T 2C2', '1994-12-03', 'M', 'Canadian'),
('PASSPORT', 'CA666777888', 'Amanda', 'Anderson', 'amanda.anderson@construction.ca', '+1-416-555-0108', '258 Dundas Street', 'Toronto', 'Canada', 'M5T 2Z5', '1996-06-18', 'F', 'Canadian'),
('PASSPORT', 'CA999000111', 'Christopher', 'Thomas', 'christopher.thomas@construction.ca', '+1-416-555-0109', '369 Bathurst Street', 'Toronto', 'Canada', 'M5T 2S3', '1991-09-25', 'M', 'Canadian'),
('PASSPORT', 'CA222333444', 'Michelle', 'Jackson', 'michelle.jackson@construction.ca', '+1-416-555-0110', '741 Ossington Avenue', 'Toronto', 'Canada', 'M6J 2Z7', '1997-02-11', 'F', 'Canadian'),
('PASSPORT', 'CA555666777', 'Daniel', 'White', 'daniel.white@construction.ca', '+1-416-555-0111', '852 Roncesvalles Avenue', 'Toronto', 'Canada', 'M6R 2L5', '1990-05-30', 'M', 'Canadian'),
('PASSPORT', 'CA888999000', 'Stephanie', 'Harris', 'stephanie.harris@construction.ca', '+1-416-555-0112', '963 Queen Street West', 'Toronto', 'Canada', 'M6J 1G7', '1994-10-07', 'F', 'Canadian'),
('PASSPORT', 'CA111222334', 'Matthew', 'Clark', 'matthew.clark@construction.ca', '+1-416-555-0113', '159 King Street West', 'Toronto', 'Canada', 'M5H 1A1', '1993-03-16', 'M', 'Canadian'),
('PASSPORT', 'CA444555667', 'Nicole', 'Lewis', 'nicole.lewis@construction.ca', '+1-416-555-0114', '357 Front Street', 'Toronto', 'Canada', 'M5V 2T6', '1996-11-29', 'F', 'Canadian');

-- Insert persons for clients
INSERT INTO GE_TPERS (pers_ident_type, pers_ident_number, pers_first_name, pers_last_name, pers_email, pers_mobile, pers_address, pers_city, pers_country, pers_postal_code, pers_birth_date, pers_gender, pers_nationality) VALUES
('PASSPORT', 'CA777888998', 'John', 'Smith', 'john.smith@email.com', '+1-416-555-0201', '100 Main Street', 'Toronto', 'Canada', 'M5H 1A1', '1975-04-10', 'M', 'Canadian'),
('PASSPORT', 'CA000111223', 'Mary', 'Johnson', 'mary.johnson@email.com', '+1-416-555-0202', '200 Oak Avenue', 'Toronto', 'Canada', 'M5V 2T6', '1980-12-15', 'F', 'Canadian'),
('PASSPORT', 'CA333444556', 'William', 'Brown', 'william.brown@email.com', '+1-416-555-0203', '300 Pine Street', 'Toronto', 'Canada', 'M6G 1L3', '1978-08-22', 'M', 'Canadian'),
('PASSPORT', 'CA666777889', 'Elizabeth', 'Davis', 'elizabeth.davis@email.com', '+1-416-555-0204', '400 Maple Drive', 'Toronto', 'Canada', 'M5B 2L7', '1982-06-05', 'F', 'Canadian'),
('PASSPORT', 'CA999000112', 'Richard', 'Wilson', 'richard.wilson@email.com', '+1-416-555-0205', '500 Cedar Lane', 'Toronto', 'Canada', 'M6J 2Z7', '1976-09-18', 'M', 'Canadian'),
('PASSPORT', 'CA222333445', 'Patricia', 'Moore', 'patricia.moore@email.com', '+1-416-555-0206', '600 Elm Street', 'Toronto', 'Canada', 'M5T 2C2', '1985-01-30', 'F', 'Canadian'),
('PASSPORT', 'CA555666778', 'Joseph', 'Taylor', 'joseph.taylor@email.com', '+1-416-555-0207', '700 Birch Road', 'Toronto', 'Canada', 'M6R 2L5', '1979-11-12', 'M', 'Canadian'),
('PASSPORT', 'CA888999001', 'Linda', 'Anderson', 'linda.anderson@email.com', '+1-416-555-0208', '800 Willow Way', 'Toronto', 'Canada', 'M5T 2Z5', '1983-07-25', 'F', 'Canadian'),
('PASSPORT', 'CA111222335', 'Thomas', 'Thomas', 'thomas.thomas@email.com', '+1-416-555-0209', '900 Spruce Street', 'Toronto', 'Canada', 'M6G 1A5', '1977-03-08', 'M', 'Canadian'),
('PASSPORT', 'CA444555668', 'Barbara', 'Jackson', 'barbara.jackson@email.com', '+1-416-555-0210', '1000 Ash Avenue', 'Toronto', 'Canada', 'M5H 2M9', '1981-05-14', 'F', 'Canadian');

-- =====================================================
-- STEP 2: INSERT USERS FOR ALL PERSONS
-- =====================================================

-- Insert users for managers
INSERT INTO GE_TUSER (user_username, user_email, user_pwd, user_is_active) VALUES
('manager01', 'michael.johnson@construction.ca', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('manager02', 'sarah.williams@construction.ca', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE);

-- Insert users for supervisors
INSERT INTO GE_TUSER (user_username, user_email, user_pwd, user_is_active) VALUES
('supervisor01', 'david.brown@construction.ca', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('supervisor02', 'jennifer.davis@construction.ca', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE);

-- Insert users for employees
INSERT INTO GE_TUSER (user_username, user_email, user_pwd, user_is_active) VALUES
('employee01', 'robert.miller@construction.ca', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('employee02', 'lisa.wilson@construction.ca', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('employee03', 'james.taylor@construction.ca', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('employee04', 'amanda.anderson@construction.ca', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('employee05', 'christopher.thomas@construction.ca', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('employee06', 'michelle.jackson@construction.ca', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('employee07', 'daniel.white@construction.ca', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('employee08', 'stephanie.harris@construction.ca', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('employee09', 'matthew.clark@construction.ca', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('employee10', 'nicole.lewis@construction.ca', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE);

-- Insert users for clients
INSERT INTO GE_TUSER (user_username, user_email, user_pwd, user_is_active) VALUES
('client01', 'john.smith@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('client02', 'mary.johnson@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('client03', 'william.brown@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('client04', 'elizabeth.davis@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('client05', 'richard.wilson@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('client06', 'patricia.moore@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('client07', 'joseph.taylor@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('client08', 'linda.anderson@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('client09', 'thomas.thomas@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE),
('client10', 'barbara.jackson@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE);

-- =====================================================
-- STEP 3: INSERT EMPLOYEES DATA
-- =====================================================

-- Insert managers
INSERT INTO GE_TEMPL ( empl_pers, empl_employee_code, empl_position, empl_department, empl_salary, empl_currency, empl_employment_type, empl_status, empl_work_email, empl_work_mobile, empl_work_location, empl_hire_date, empl_is_active) VALUES
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'michael.johnson@construction.ca'), 'EMP001', 'Project Manager', 'Construction', 85000.00, 'CAD', 'FULL_TIME', 'ACTIVE', 'michael.johnson@construction.ca', '+1-416-555-0101', 'Toronto Office', '2020-01-15', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'sarah.williams@construction.ca'), 'EMP002', 'Operations Manager', 'Construction', 82000.00, 'CAD', 'FULL_TIME', 'ACTIVE', 'sarah.williams@construction.ca', '+1-416-555-0102', 'Toronto Office', '2020-03-20', TRUE);

-- Insert supervisors
INSERT INTO GE_TEMPL (empl_pers, empl_employee_code, empl_position, empl_department, empl_salary, empl_currency, empl_employment_type, empl_status, empl_work_email, empl_work_mobile, empl_work_location, empl_hire_date, empl_is_active) VALUES
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'david.brown@construction.ca'), 'EMP003', 'Construction Supervisor', 'Construction', 65000.00, 'CAD', 'FULL_TIME', 'ACTIVE', 'david.brown@construction.ca', '+1-416-555-0103', 'Toronto Site', '2021-02-10', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'jennifer.davis@construction.ca'), 'EMP004', 'Site Supervisor', 'Construction', 62000.00, 'CAD', 'FULL_TIME', 'ACTIVE', 'jennifer.davis@construction.ca', '+1-416-555-0104', 'Toronto Site', '2021-05-15', TRUE);

-- Insert employees
INSERT INTO GE_TEMPL (empl_pers, empl_employee_code, empl_position, empl_department, empl_salary, empl_currency, empl_employment_type, empl_status, empl_work_email, empl_work_mobile, empl_work_location, empl_hire_date, empl_is_active) VALUES
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'robert.miller@construction.ca'), 'EMP005', 'Construction Worker', 'Construction', 45000.00, 'CAD', 'FULL_TIME', 'ACTIVE', 'robert.miller@construction.ca', '+1-416-555-0105', 'Toronto Site', '2022-01-10', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'lisa.wilson@construction.ca'), 'EMP006', 'Demolition Specialist', 'Demolition', 48000.00, 'CAD', 'FULL_TIME', 'ACTIVE', 'lisa.wilson@construction.ca', '+1-416-555-0106', 'Toronto Site', '2022-02-15', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'james.taylor@construction.ca'), 'EMP007', 'Plumber', 'Plumbing', 52000.00, 'CAD', 'FULL_TIME', 'ACTIVE', 'james.taylor@construction.ca', '+1-416-555-0107', 'Toronto Site', '2022-03-20', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'amanda.anderson@construction.ca'), 'EMP008', 'Electrician', 'Electrical', 55000.00, 'CAD', 'FULL_TIME', 'ACTIVE', 'amanda.anderson@construction.ca', '+1-416-555-0108', 'Toronto Site', '2022-04-25', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'christopher.thomas@construction.ca'), 'EMP009', 'Carpenter', 'Construction', 47000.00, 'CAD', 'FULL_TIME', 'ACTIVE', 'christopher.thomas@construction.ca', '+1-416-555-0109', 'Toronto Site', '2022-05-30', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'michelle.jackson@construction.ca'), 'EMP010', 'Mason', 'Construction', 46000.00, 'CAD', 'FULL_TIME', 'ACTIVE', 'michelle.jackson@construction.ca', '+1-416-555-0110', 'Toronto Site', '2022-06-05', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'daniel.white@construction.ca'), 'EMP011', 'HVAC Technician', 'HVAC', 54000.00, 'CAD', 'FULL_TIME', 'ACTIVE', 'daniel.white@construction.ca', '+1-416-555-0111', 'Toronto Site', '2022-07-10', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'stephanie.harris@construction.ca'), 'EMP012', 'Welder', 'Metal Work', 50000.00, 'CAD', 'FULL_TIME', 'ACTIVE', 'stephanie.harris@construction.ca', '+1-416-555-0112', 'Toronto Site', '2022-08-15', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'matthew.clark@construction.ca'), 'EMP013', 'Heavy Equipment Operator', 'Construction', 53000.00, 'CAD', 'FULL_TIME', 'ACTIVE', 'matthew.clark@construction.ca', '+1-416-555-0113', 'Toronto Site', '2022-09-20', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'nicole.lewis@construction.ca'), 'EMP014', 'Roofing Specialist', 'Construction', 49000.00, 'CAD', 'FULL_TIME', 'ACTIVE', 'nicole.lewis@construction.ca', '+1-416-555-0114', 'Toronto Site', '2022-10-25', TRUE);

-- =====================================================
-- STEP 4: INSERT CLIENTS DATA
-- =====================================================

INSERT INTO CL_TCLIE (clie_pers, clie_client_code, clie_type, clie_company_name, clie_company_industry, clie_contact_person, clie_contact_position, clie_address, clie_city, clie_country, clie_postal_code, clie_currency, clie_status, clie_is_active) VALUES
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'john.smith@email.com'), 'CLI001', 'CORPORATE', 'Smith Construction Ltd', 'Construction', 'John Smith', 'CEO', '100 Main Street', 'Toronto', 'Canada', 'M5H 1A1', 'CAD', 'ACTIVE', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'mary.johnson@email.com'), 'CLI002', 'CORPORATE', 'Johnson Properties Inc', 'Real Estate', 'Mary Johnson', 'President', '200 Oak Avenue', 'Toronto', 'Canada', 'M5V 2T6', 'CAD', 'ACTIVE', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'william.brown@email.com'), 'CLI003', 'CORPORATE', 'Brown Development Corp', 'Development', 'William Brown', 'Managing Director', '300 Pine Street', 'Toronto', 'Canada', 'M6G 1L3', 'CAD', 'ACTIVE', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'elizabeth.davis@email.com'), 'CLI004', 'CORPORATE', 'Davis Renovations', 'Renovation', 'Elizabeth Davis', 'Owner', '400 Maple Drive', 'Toronto', 'Canada', 'M5B 2L7', 'CAD', 'ACTIVE', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'richard.wilson@email.com'), 'CLI005', 'CORPORATE', 'Wilson Contracting', 'Contracting', 'Richard Wilson', 'General Manager', '500 Cedar Lane', 'Toronto', 'Canada', 'M6J 2Z7', 'CAD', 'ACTIVE', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'patricia.moore@email.com'), 'CLI006', 'CORPORATE', 'Moore Builders', 'Construction', 'Patricia Moore', 'Operations Director', '600 Elm Street', 'Toronto', 'Canada', 'M5T 2C2', 'CAD', 'ACTIVE', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'joseph.taylor@email.com'), 'CLI007', 'CORPORATE', 'Taylor Industrial', 'Industrial', 'Joseph Taylor', 'Plant Manager', '700 Birch Road', 'Toronto', 'Canada', 'M6R 2L5', 'CAD', 'ACTIVE', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'linda.anderson@email.com'), 'CLI008', 'CORPORATE', 'Anderson Projects', 'Project Management', 'Linda Anderson', 'Project Director', '800 Willow Way', 'Toronto', 'Canada', 'M5T 2Z5', 'CAD', 'ACTIVE', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'thomas.thomas@email.com'), 'CLI009', 'CORPORATE', 'Thomas Construction', 'Construction', 'Thomas Thomas', 'Construction Manager', '900 Spruce Street', 'Toronto', 'Canada', 'M6G 1A5', 'CAD', 'ACTIVE', TRUE),
((SELECT pers_pers FROM GE_TPERS WHERE pers_email = 'barbara.jackson@email.com'), 'CLI010', 'CORPORATE', 'Jackson Enterprises', 'Commercial', 'Barbara Jackson', 'Business Owner', '1000 Ash Avenue', 'Toronto', 'Canada', 'M5H 2M9', 'CAD', 'ACTIVE', TRUE);

-- =====================================================
-- STEP 5: ASSIGN ROLES TO USERS
-- =====================================================

-- Assign MANAGER role to managers
INSERT INTO GE_TUSRO (usro_user, usro_role) VALUES
((SELECT user_user FROM GE_TUSER WHERE user_username = 'manager01'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'MANAGER')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'manager02'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'MANAGER'));

-- Assign SUPERVISOR role to supervisors
INSERT INTO GE_TUSRO (usro_user, usro_role) VALUES
((SELECT user_user FROM GE_TUSER WHERE user_username = 'supervisor01'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'SUPERVISOR')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'supervisor02'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'SUPERVISOR'));

-- Assign USER role to employees
INSERT INTO GE_TUSRO (usro_user, usro_role) VALUES
((SELECT user_user FROM GE_TUSER WHERE user_username = 'employee01'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'USER')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'employee02'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'USER')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'employee03'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'USER')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'employee04'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'USER')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'employee05'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'USER')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'employee06'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'USER')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'employee07'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'USER')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'employee08'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'USER')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'employee09'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'USER')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'employee10'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'USER'));

-- Assign CLIENT role to clients
INSERT INTO GE_TUSRO (usro_user, usro_role) VALUES
((SELECT user_user FROM GE_TUSER WHERE user_username = 'client01'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'CLIENT')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'client02'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'CLIENT')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'client03'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'CLIENT')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'client04'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'CLIENT')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'client05'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'CLIENT')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'client06'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'CLIENT')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'client07'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'CLIENT')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'client08'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'CLIENT')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'client09'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'CLIENT')),
((SELECT user_user FROM GE_TUSER WHERE user_username = 'client10'), (SELECT role_role FROM GE_TROLE WHERE role_name = 'CLIENT'));

-- =====================================================
-- COMPLETION MESSAGE
-- =====================================================

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'TEST DATA INSERTED SUCCESSFULLY';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Created persons:';
    RAISE NOTICE '- 2 managers';
    RAISE NOTICE '- 2 supervisors';
    RAISE NOTICE '- 10 employees';
    RAISE NOTICE '- 10 clients';
    RAISE NOTICE '';
    RAISE NOTICE 'Created users:';
    RAISE NOTICE '- manager01, manager02 (MANAGER role)';
    RAISE NOTICE '- supervisor01, supervisor02 (SUPERVISOR role)';
    RAISE NOTICE '- employee01-10 (USER role)';
    RAISE NOTICE '- client01-10 (CLIENT role)';
    RAISE NOTICE '';
    RAISE NOTICE 'All users have password: Abc123456*';
    RAISE NOTICE 'All employees have salary in CAD currency';
    RAISE NOTICE 'All data is for Canada (CAD currency)';
    RAISE NOTICE '=====================================================';
END $$; 