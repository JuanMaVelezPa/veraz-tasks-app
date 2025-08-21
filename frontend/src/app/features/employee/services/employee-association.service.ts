import { Injectable, inject, signal } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Employee } from '@employee/interfaces/employee.interface';
import { EmployeeService } from './employee.service';
import { ApiResponse } from '@shared/interfaces/api-response.interface';

@Injectable({ providedIn: 'root' })
export class EmployeeAssociationService {
  private employeeService = inject(EmployeeService);

  // Signals for state management
  private currentEmployeeSignal = signal<Employee | null>(null);
  private isLoadingEmployeeSignal = signal(false);

  // Expose signals as readonly
  currentEmployee = this.currentEmployeeSignal.asReadonly();
  isLoadingEmployee = this.isLoadingEmployeeSignal.asReadonly();

  /**
   * Get employee information for a specific person
   * @param personId The person ID to get employee information for
   * @returns Observable of employee data or null if not found
   */
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

  /**
   * Check if a person has employee information
   * @param personId The person ID to check
   * @returns Observable of boolean indicating if person is an employee
   */
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

  /**
   * Clear current employee data
   */
  clearCurrentEmployee(): void {
    this.currentEmployeeSignal.set(null);
  }

  /**
   * Set loading state
   */
  setLoading(loading: boolean): void {
    this.isLoadingEmployeeSignal.set(loading);
  }

  /**
   * Get employee status for display
   * @param employee The employee object
   * @returns Status string for display
   */
  getEmployeeStatus(employee: Employee): string {
    if (!employee.isActive) return 'Inactive';
    return employee.status || 'Active';
  }

  /**
   * Get employment type for display
   * @param employee The employee object
   * @returns Employment type string for display
   */
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
