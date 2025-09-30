import { Timestamped } from '@shared/interfaces/timestamped.interface';

export interface EmployeeFields {
  personId: string;
  hireDate: string;
  position: string;
  status: string;
  salary: number;
  currency: string;
  salaryType: string;
  department?: string;
  employmentType?: string;
  terminationDate?: string;
  probationEndDate?: string;
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

export type EmployeeCreateRequest = EmployeeFields;
export type EmployeeUpdateRequest = Partial<EmployeeFields>;
export type EmployeeFormData = EmployeeFields;

export interface EmployeePersonAssociation {
  personId: string;
}

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
