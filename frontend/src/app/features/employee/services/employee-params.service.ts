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
    { id: '1', code: 'ADMIN', name: 'Administration', description: 'Administrative management and HR', isActive: true },
    { id: '2', code: 'MANAGEMENT', name: 'Management', description: 'Strategic direction and management', isActive: true },
    { id: '3', code: 'FINANCE', name: 'Finance', description: 'Financial and accounting management', isActive: true },
    { id: '4', code: 'CONSTRUCTION', name: 'Construction', description: 'General construction and civil works', isActive: true },
    { id: '5', code: 'PLUMBING', name: 'Plumbing', description: 'Plumbing and hydraulic installations', isActive: true },
    { id: '6', code: 'ELECTRICAL', name: 'Electrical', description: 'Electrical installations and systems', isActive: true },
    { id: '7', code: 'MAINTENANCE', name: 'Maintenance', description: 'General maintenance and repairs', isActive: true },
    { id: '8', code: 'DEMOLITION', name: 'Demolition', description: 'Demolition and excavation works', isActive: true },
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
    return this.findById(this.employmentTypes(), id);
  }

  getEmployeeStatusById(id: string): EmployeeStatus | undefined {
    return this.findById(this.employeeStatuses(), id);
  }

  getJobLevelById(id: string): JobLevel | undefined {
    return this.findById(this.jobLevels(), id);
  }

  getSalaryTypeById(id: string): SalaryType | undefined {
    return this.findById(this.salaryTypes(), id);
  }

  getCurrencyById(id: string): Currency | undefined {
    return this.findById(this.currencies(), id);
  }

  getWorkShiftById(id: string): WorkShift | undefined {
    return this.findById(this.workShifts(), id);
  }

  getDepartmentById(id: string): Department | undefined {
    return this.findById(this.departments(), id);
  }

  getEmploymentTypeByCode(code: string): EmploymentType | undefined {
    return this.findByCode(this.employmentTypes(), code);
  }

  getEmployeeStatusByCode(code: string): EmployeeStatus | undefined {
    return this.findByCode(this.employeeStatuses(), code);
  }

  getJobLevelByCode(code: string): JobLevel | undefined {
    return this.findByCode(this.jobLevels(), code);
  }

  getSalaryTypeByCode(code: string): SalaryType | undefined {
    return this.findByCode(this.salaryTypes(), code);
  }

  getCurrencyByCode(code: string): Currency | undefined {
    return this.findByCode(this.currencies(), code);
  }

  getWorkShiftByCode(code: string): WorkShift | undefined {
    return this.findByCode(this.workShifts(), code);
  }

  getDepartmentByCode(code: string): Department | undefined {
    return this.findByCode(this.departments(), code);
  }

  getDepartmentByName(name: string): Department | undefined {
    return this.departments().find(dept => dept.name === name);
  }

  private findById<T extends BaseParamOption>(items: T[], id: string): T | undefined {
    return items.find(item => item.id === id);
  }

  private findByCode<T extends BaseParamOption>(items: T[], code: string): T | undefined {
    return items.find(item => item.code === code);
  }
}
