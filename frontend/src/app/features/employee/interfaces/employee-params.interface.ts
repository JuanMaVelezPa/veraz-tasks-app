// Base interface for all parameter types
export interface BaseParamOption {
  id: string;
  code: string;
  name: string;
  isActive: boolean;
}

// Specific interfaces extending the base
export interface EmploymentType extends BaseParamOption {}

export interface EmployeeStatus extends BaseParamOption {}

export interface JobLevel extends BaseParamOption {}

export interface SalaryType extends BaseParamOption {}

export interface WorkShift extends BaseParamOption {}

export interface Currency extends BaseParamOption {
  symbol: string;
}

export interface Department extends BaseParamOption {
  description?: string;
}

export interface EmployeeParams {
  employmentTypes: EmploymentType[];
  employeeStatuses: EmployeeStatus[];
  jobLevels: JobLevel[];
  salaryTypes: SalaryType[];
  currencies: Currency[];
  workShifts: WorkShift[];
  departments: Department[];
}
