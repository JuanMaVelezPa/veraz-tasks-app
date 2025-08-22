import { Injectable, inject, signal } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Employee } from '@employee/interfaces/employee.interface';
import { EmployeeService } from './employee.service';
import { ApiResponse } from '@shared/interfaces/api-response.interface';

@Injectable({ providedIn: 'root' })
export class EmployeeAssociationService {
  private employeeService = inject(EmployeeService);

  private currentEmployeeSignal = signal<Employee | null>(null);
  private isLoadingEmployeeSignal = signal(false);

  currentEmployee = this.currentEmployeeSignal.asReadonly();
  isLoadingEmployee = this.isLoadingEmployeeSignal.asReadonly();

  getEmployeeByPersonId(personId: string): Observable<Employee | null> {
    this.isLoadingEmployeeSignal.set(true);

    return this.employeeService.getEmployeeByPersonId(personId).pipe(
      map((employee: Employee | null) => {
        this.currentEmployeeSignal.set(employee);
        this.isLoadingEmployeeSignal.set(false);
        return employee;
      })
    );
  }

  isPersonEmployee(personId: string): Observable<boolean> {
    return this.employeeService.getEmployeeByPersonId(personId).pipe(
      map((employee: Employee | null) => {
        const hasEmployee = !!employee;
        if (hasEmployee) {
          this.currentEmployeeSignal.set(employee);
        }
        return hasEmployee;
      })
    );
  }

  clearCurrentEmployee(): void {
    this.currentEmployeeSignal.set(null);
  }

  setLoading(loading: boolean): void {
    this.isLoadingEmployeeSignal.set(loading);
  }

  getEmployeeStatus(employee: Employee): string {
    if (!employee.isActive) return 'Inactive';
    return employee.status || 'Active';
  }

  getEmploymentType(employee: Employee): string {
    switch (employee.employmentType?.toUpperCase()) {
      case 'FULL_TIME':
        return 'Full Time';
      case 'PART_TIME':
        return 'Part Time';
      case 'CONTRACTOR':
        return 'Contractor';
      case 'INTERN':
        return 'Intern';
      case 'FREELANCER':
        return 'Freelancer';
      default:
        return employee.employmentType || 'N/A';
    }
  }
}
