import { Injectable, inject } from '@angular/core';
import { Observable, map, of, tap, catchError, throwError } from 'rxjs';
import { EmployeeApiService } from './employee-api.service';
import { Employee, EmployeeCreateRequest, EmployeeUpdateRequest } from '@employee/interfaces/employee.interface';
import { SearchOptions } from '@shared/interfaces/search.interface';
import { PaginatedResponseDTO } from '@shared/interfaces/pagination.interface';
import { ApiResponse } from '@shared/interfaces/api-response.interface';
import { CacheService } from '@shared/services/cache.service';

const emptyEmployee: Employee = {
  id: 'new',
  personId: '',
  position: '',
  employmentType: '',
  status: 'ACTIVE',
  hireDate: new Date().toISOString().split('T')[0],
  isActive: true,
  createdAt: new Date().toISOString(),
  updatedAt: new Date().toISOString()
};

const emptyPagination: PaginatedResponseDTO<Employee> = {
  data: [],
  pagination: {
    currentPage: 0,
    totalPages: 0,
    totalElements: 0,
    pageSize: 10,
    hasNext: false,
    hasPrevious: false,
    isFirst: true,
    isLast: true
  }
};

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private employeeApiService = inject(EmployeeApiService);
  private cacheService = inject(CacheService);

  getEmployees(options: SearchOptions): Observable<PaginatedResponseDTO<Employee>> {
    const cacheKey = this.generateCacheKey(options);
    const cached = this.cacheService.get<PaginatedResponseDTO<Employee>>(cacheKey);

    if (cached) {
      return of(cached);
    }

    return this.employeeApiService.getEmployees(options)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'employees')),
        tap(response => this.cacheService.set(cacheKey, response)),
        catchError((error) => {
          console.error('EmployeeService - getEmployees - Error:', error);
          return of(emptyPagination);
        })
      );
  }

  getEmployeeById(id: string): Observable<Employee> {
    if (id === 'new') return of(emptyEmployee);

    const cacheKey = `employee:${id}`;
    const cached = this.cacheService.get<Employee>(cacheKey);

    if (cached) {
      return of(cached);
    }

    return this.employeeApiService.getEmployeeById(id)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'employee')),
        tap((employee) => this.cacheService.set(cacheKey, employee)),
        catchError((error) => {
          console.error('EmployeeService - getEmployeeById - Error:', error);
          return of(emptyEmployee);
        })
      );
  }

  createEmployee(employeeData: EmployeeCreateRequest): Observable<Employee> {
    return this.employeeApiService.createEmployee(employeeData)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'employee')),
        tap((employee: Employee) => this.cacheService.clearPattern('employees:')),
        catchError((error) => this.handleError(error, 'creating employee'))
      );
  }

  updateEmployee(id: string, employeeData: EmployeeUpdateRequest): Observable<Employee> {
    return this.employeeApiService.updateEmployee(id, employeeData)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'employee')),
        tap((employee: Employee) => {
          this.cacheService.set(`employee:${employee.id}`, employee);
          this.cacheService.clearPattern('employees:');
        }),
        catchError((error) => this.handleError(error, 'updating employee'))
      );
  }

  deleteEmployee(id: string): Observable<void> {
    return this.employeeApiService.deleteEmployee(id)
      .pipe(
        map(() => {}),
        tap(() => {
          this.cacheService.delete(`employee:${id}`);
          this.cacheService.clearPattern('employees:');
        }),
        catchError((error) => this.handleError(error, 'deleting employee'))
      );
  }

  getEmployeeByPersonId(personId: string): Observable<Employee | null> {
    // Validar que el personId sea válido
    if (!personId || personId === 'undefined' || personId === 'null' || personId.trim() === '') {
      console.error('EmployeeService - getEmployeeByPersonId - Invalid personId:', personId);
      return of(null);
    }

    const cacheKey = `employee:person:${personId}`;
    const cached = this.cacheService.get<Employee | null>(cacheKey);

    // Only use cache if we have a valid employee (not null)
    if (cached !== undefined && cached !== null) {
      return of(cached);
    }

    // Clear any null cache entry to force API call
    if (cached === null) {
      this.cacheService.delete(cacheKey);
    }

    return this.employeeApiService.getEmployeeByPersonId(personId)
      .pipe(
        map((apiResponse) => {
          // Si success es false, significa que no se encontró el empleado (caso válido)
          if (!apiResponse.success) {
            return null;
          }

          return apiResponse.data;
        }),
        tap((employee: Employee | null) => {
          // Cache the result (null or valid employee)
          this.cacheService.set(cacheKey, employee);
        }),
        catchError((error) => {
          console.error('EmployeeService - getEmployeeByPersonId - Error:', error);
          return this.handleError(error, 'loading employee by person ID');
        })
      );
  }

  private generateCacheKey(options: SearchOptions): string {
    const { page, size, search, sort, order } = options;
    return `employees:${page}:${size}:${search || ''}:${sort || ''}:${order || ''}`;
  }



  private handleSuccess(apiResponse: ApiResponse<any>, type: string): any {
    if (!apiResponse.success) {
      throw new Error(apiResponse.message || `Error ${type}`);
    }

    // For delete operations, data can be null/undefined, which is valid
    if (type === 'boolean' || type === 'void') {
      return true; // Return true for successful delete operations
    }

    // For other operations, data should exist
    if (!apiResponse.data) {
      throw new Error(apiResponse.message || `Error ${type}`);
    }

    return apiResponse.data;
  }

  private handleError(error: any, operation: string): Observable<never> {
    // Propagar el error original para que HttpErrorService pueda manejarlo correctamente
    return throwError(() => error);
  }
}
