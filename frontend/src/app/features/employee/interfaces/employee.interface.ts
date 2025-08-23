import { Timestamped } from '@shared/interfaces/timestamped.interface';

export interface EmployeeFields {
  personId: string;
  position: string;
  department?: string;
  employmentType: string;
  status: string;
  hireDate: string;
  terminationDate?: string;
  probationEndDate?: string;
  salary?: number;
  currency?: string;
  salaryType?: string;
  workEmail?: string;
  workPhone?: string;
  workLocation?: string;
  workSchedule?: string;
  jobLevel?: string;
  costCenter?: string;
  workShift?: string;
  skills?: string;
  certifications?: string;
  education?: string;
  benefits?: string;
  notes?: string;
  isActive: boolean;
}

export interface Employee extends EmployeeFields, Timestamped {
  id: string;
}

// Derived types using TypeScript utilities
export type EmployeeCreateRequest = EmployeeFields;
export type EmployeeUpdateRequest = Partial<EmployeeFields>;
export type EmployeeFormData = EmployeeFields;

// Employee association interface
export interface EmployeePersonAssociation {
  personId: string;
}

// Options for management service
export interface EmployeeManagementOptions {
  context: 'admin' | 'profile';
  personId?: string;
  onSuccess?: (employee: Employee) => void;
  onError?: (error: any) => void;
}

export interface EmployeeParams {
  page?: number;
  size?: number;
  search?: string;
  department?: string;
  status?: string;
  employmentType?: string;
}
