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
    const cacheKey = this.buildCacheKey(options);
    const cached = this.cacheService.get<PaginatedResponseDTO<Employee>>(cacheKey);

    if (cached) {
      return of(cached);
    }

    return this.employeeApiService.getEmployees(options)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'employees')),
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
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'employee')),
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
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'employee')),
        tap(() => {
          this.clearEmployeesCache();
          this.clearPersonsCache();
        }),
        catchError((error) => this.propagateError(error, 'creating employee'))
      );
  }

  updateEmployee(id: string, employeeData: EmployeeUpdateRequest): Observable<Employee> {
    return this.employeeApiService.updateEmployee(id, employeeData)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'employee')),
        tap((employee: Employee) => {
          this.updateEmployeeCache(employee);
          this.clearPersonsCache();
        }),
        catchError((error) => this.propagateError(error, 'updating employee'))
      );
  }

  deleteEmployee(id: string): Observable<void> {
    return this.employeeApiService.deleteEmployee(id)
      .pipe(
        map(() => { }),
        tap(() => {
          this.clearEmployeeCache(id);
          this.clearPersonsCache();
        }),
        catchError((error) => this.propagateError(error, 'deleting employee'))
      );
  }

  getEmployeeByPersonId(personId: string): Observable<Employee | null> {
    if (!this.isValidPersonId(personId)) {
      console.error('EmployeeService - getEmployeeByPersonId - Invalid personId:', personId);
      return of(null);
    }

    const cacheKey = `employee:person:${personId}`;
    const cached = this.cacheService.get<Employee | null>(cacheKey);

    if (cached !== undefined && cached !== null) {
      return of(cached);
    }

    if (cached === null) {
      this.cacheService.delete(cacheKey);
    }

    return this.employeeApiService.getEmployeeByPersonId(personId)
      .pipe(
        map((apiResponse) => this.extractEmployeeFromResponse(apiResponse)),
        tap((employee: Employee | null) => this.cacheService.set(cacheKey, employee)),
        catchError((error) => {
          return this.propagateError(error, 'loading employee by person ID');
        })
      );
  }

  private buildCacheKey(options: SearchOptions): string {
    const { page, size, search, sort, order } = options;
    return `employees:${page}:${size}:${search || ''}:${sort || ''}:${order || ''}`;
  }

  private extractDataFromResponse(apiResponse: ApiResponse<any>, type: string): any {
    if (!apiResponse.success) {
      throw new Error(apiResponse.message || `Error ${type}`);
    }

    if (type === 'boolean' || type === 'void') {
      return true;
    }

    if (!apiResponse.data) {
      throw new Error(apiResponse.message || `Error ${type}`);
    }

    return apiResponse.data;
  }

  private extractEmployeeFromResponse(apiResponse: ApiResponse<any>): Employee | null {
    if (!apiResponse.success) {
      return null;
    }
    return apiResponse.data;
  }

  private propagateError(error: any, operation: string): Observable<never> {
    return throwError(() => error);
  }

  private isValidPersonId(personId: string): boolean {
    return Boolean(personId && personId !== 'undefined' && personId !== 'null' && personId.trim() !== '');
  }

  private updateEmployeeCache(employee: Employee): void {
    this.cacheService.set(`employee:${employee.id}`, employee);
    this.clearEmployeesCache();
    this.clearPersonsCache();
  }

  private clearEmployeeCache(employeeId: string): void {
    this.cacheService.delete(`employee:${employeeId}`);
    this.clearEmployeesCache();
  }

  private clearEmployeesCache(): void {
    this.cacheService.clearPattern('employees:');
  }

  private clearPersonsCache(): void {
    this.cacheService.clearPattern('persons:');
  }
}
