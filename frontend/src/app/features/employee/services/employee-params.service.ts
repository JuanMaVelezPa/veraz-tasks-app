import { Injectable, signal } from '@angular/core';
import {
  BaseParamOption,
  EmploymentType,
  EmployeeStatus,
  JobLevel,
  SalaryType,
  Currency,
  WorkShift,
  Department,
  EmployeeParams
} from '@employee/interfaces/employee-params.interface';

@Injectable({
  providedIn: 'root'
})
export class EmployeeParamsService {
  private employmentTypes = signal<EmploymentType[]>([
    { id: '1', code: 'FULL_TIME', name: 'Full Time', isActive: true },
    { id: '2', code: 'PART_TIME', name: 'Part Time', isActive: true },
    { id: '3', code: 'CONTRACTOR', name: 'Contractor', isActive: true },
    { id: '4', code: 'INTERN', name: 'Intern', isActive: true },
    { id: '5', code: 'FREELANCER', name: 'Freelancer', isActive: true }
  ]);

  private employeeStatuses = signal<EmployeeStatus[]>([
    { id: '1', code: 'ACTIVE', name: 'Active', isActive: true },
    { id: '2', code: 'INACTIVE', name: 'Inactive', isActive: true },
    { id: '3', code: 'TERMINATED', name: 'Terminated', isActive: true },
    { id: '4', code: 'ON_LEAVE', name: 'On Leave', isActive: true }
  ]);

  private jobLevels = signal<JobLevel[]>([
    { id: '1', code: 'JUNIOR', name: 'Junior', isActive: true },
    { id: '2', code: 'MID', name: 'Mid Level', isActive: true },
    { id: '3', code: 'SENIOR', name: 'Senior', isActive: true },
    { id: '4', code: 'LEAD', name: 'Lead', isActive: true },
    { id: '5', code: 'MANAGER', name: 'Manager', isActive: true },
    { id: '6', code: 'DIRECTOR', name: 'Director', isActive: true },
    { id: '7', code: 'EXECUTIVE', name: 'Executive', isActive: true }
  ]);

  private salaryTypes = signal<SalaryType[]>([
    { id: '1', code: 'HOURLY', name: 'Hourly', isActive: true },
    { id: '2', code: 'MONTHLY', name: 'Monthly', isActive: true },
    { id: '3', code: 'ANNUAL', name: 'Annual', isActive: true }
  ]);

  private currencies = signal<Currency[]>([
    { id: '1', code: 'CAD', name: 'Canadian Dollar', symbol: '$', isActive: true },
    { id: '2', code: 'USD', name: 'US Dollar', symbol: '$', isActive: true },
    { id: '3', code: 'EUR', name: 'Euro', symbol: '€', isActive: true },
    { id: '4', code: 'COP', name: 'Colombian Peso', symbol: '$', isActive: true },
  ]);

  private workShifts = signal<WorkShift[]>([
    { id: '1', code: 'DAY', name: 'Day', isActive: true },
    { id: '2', code: 'NIGHT', name: 'Night', isActive: true },
    { id: '3', code: 'ROTATING', name: 'Rotating', isActive: true },
    { id: '4', code: 'FLEXIBLE', name: 'Flexible', isActive: true }
  ]);

      private departments = signal<Department[]>([
    // Core Departments
    { id: '1', code: 'ADMIN', name: 'Administration', description: 'Administrative management and HR', isActive: true },
    { id: '2', code: 'MANAGEMENT', name: 'Management', description: 'Strategic direction and management', isActive: true },
    { id: '3', code: 'FINANCE', name: 'Finance', description: 'Financial and accounting management', isActive: true },

    // Main Construction Services
    { id: '4', code: 'CONSTRUCTION', name: 'Construction', description: 'General construction and civil works', isActive: true },
    { id: '5', code: 'PLUMBING', name: 'Plumbing', description: 'Plumbing and hydraulic installations', isActive: true },
    { id: '6', code: 'ELECTRICAL', name: 'Electrical', description: 'Electrical installations and systems', isActive: true },
    { id: '7', code: 'MAINTENANCE', name: 'Maintenance', description: 'General maintenance and repairs', isActive: true },
    { id: '8', code: 'DEMOLITION', name: 'Demolition', description: 'Demolition and excavation works', isActive: true },

    // Support Services
    { id: '9', code: 'SAFETY', name: 'Safety', description: 'Safety and quality control', isActive: true },
    { id: '10', code: 'LOGISTICS', name: 'Logistics', description: 'Materials and equipment management', isActive: true },
    { id: '11', code: 'SALES', name: 'Sales', description: 'Sales and customer service', isActive: true }
  ]);

  getEmploymentTypes() {
    return this.employmentTypes;
  }

  getEmployeeStatuses() {
    return this.employeeStatuses;
  }

  getJobLevels() {
    return this.jobLevels;
  }

  getSalaryTypes() {
    return this.salaryTypes;
  }

  getCurrencies() {
    return this.currencies;
  }

  getWorkShifts() {
    return this.workShifts;
  }

  getDepartments() {
    return this.departments;
  }

  getEmployeeParams(): EmployeeParams {
    return {
      employmentTypes: this.employmentTypes(),
      employeeStatuses: this.employeeStatuses(),
      jobLevels: this.jobLevels(),
      salaryTypes: this.salaryTypes(),
      currencies: this.currencies(),
      workShifts: this.workShifts(),
      departments: this.departments()
    };
  }

  getEmploymentTypeById(id: string): EmploymentType | undefined {
    return this.employmentTypes().find(type => type.id === id);
  }

  getEmployeeStatusById(id: string): EmployeeStatus | undefined {
    return this.employeeStatuses().find(status => status.id === id);
  }

  getJobLevelById(id: string): JobLevel | undefined {
    return this.jobLevels().find(level => level.id === id);
  }

  getSalaryTypeById(id: string): SalaryType | undefined {
    return this.salaryTypes().find(type => type.id === id);
  }

  getCurrencyById(id: string): Currency | undefined {
    return this.currencies().find(currency => currency.id === id);
  }

  getWorkShiftById(id: string): WorkShift | undefined {
    return this.workShifts().find(shift => shift.id === id);
  }

  getEmploymentTypeByCode(code: string): EmploymentType | undefined {
    return this.employmentTypes().find(type => type.code === code);
  }

  getEmployeeStatusByCode(code: string): EmployeeStatus | undefined {
    return this.employeeStatuses().find(status => status.code === code);
  }

  getJobLevelByCode(code: string): JobLevel | undefined {
    return this.jobLevels().find(level => level.code === code);
  }

  getSalaryTypeByCode(code: string): SalaryType | undefined {
    return this.salaryTypes().find(type => type.code === code);
  }

  getCurrencyByCode(code: string): Currency | undefined {
    return this.currencies().find(currency => currency.code === code);
  }

  getWorkShiftByCode(code: string): WorkShift | undefined {
    return this.workShifts().find(shift => shift.code === code);
  }

  getDepartmentById(id: string): Department | undefined {
    return this.departments().find(dept => dept.id === id);
  }

  getDepartmentByCode(code: string): Department | undefined {
    return this.departments().find(dept => dept.code === code);
  }

  getDepartmentByName(name: string): Department | undefined {
    return this.departments().find(dept => dept.name === name);
  }
}
