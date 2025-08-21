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
  employeeCode: '',
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
        catchError(() => of(emptyPagination))
      );
  }

  getEmployeeById(id: string): Observable<Employee> {
    if (id === 'new') return of(emptyEmployee);

    const cacheKey = this.generateEmployeeCacheKey(id);
    const cached = this.cacheService.get<Employee>(cacheKey);

    if (cached) {
      return of(cached);
    }

    return this.employeeApiService.getEmployeeById(id)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'employee')),
        tap((employee) => this.cacheService.set(cacheKey, employee)),
        catchError(() => of(emptyEmployee))
      );
  }

  createEmployee(employeeData: EmployeeCreateRequest): Observable<Employee> {
    // Clear any existing cache for this person before creating
    if (employeeData.personId) {
      this.forceRefreshPersonCache(employeeData.personId);
    }

    return this.employeeApiService.createEmployee(employeeData)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'employee')),
        tap((employee: Employee) => {
          // Cache the new employee
          this.cacheService.set(this.generateEmployeeCacheKey(employee.id), employee);

          // Cache by person ID if available
          if (employee.personId) {
            this.cacheService.set(this.generatePersonCacheKey(employee.personId), employee);
          }

          // Clear employee lists
          this.cacheService.clearPattern('employees:');
        }),
        catchError((error) => this.handleError(error, 'creating employee'))
      );
  }

  updateEmployee(id: string, employeeData: EmployeeUpdateRequest): Observable<Employee> {
    return this.employeeApiService.updateEmployee(id, employeeData)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'employee')),
        tap((employee: Employee) => {
          // Update employee cache
          this.cacheService.set(this.generateEmployeeCacheKey(employee.id), employee);

          // Update person-employee cache if available
          if (employee.personId) {
            this.cacheService.set(this.generatePersonCacheKey(employee.personId), employee);
          }

          // Clear employee lists to force refresh
          this.cacheService.clearPattern('employees:');
        }),
        catchError((error) => this.handleError(error, 'updating employee'))
      );
  }

  deleteEmployee(id: string): Observable<boolean> {
    // Get employee from cache to know personId before deletion
    const cachedEmployee = this.cacheService.get<Employee>(this.generateEmployeeCacheKey(id));

    return this.employeeApiService.deleteEmployee(id)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'void')),
        tap(() => {
          // Delete specific employee cache
          this.cacheService.delete(this.generateEmployeeCacheKey(id));

          // Delete person-employee cache if we know the personId
          if (cachedEmployee?.personId) {
            this.cacheService.delete(this.generatePersonCacheKey(cachedEmployee.personId));
          }

          // Clear employee lists to force refresh
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

    const cacheKey = this.generatePersonCacheKey(personId);
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

  private generatePersonCacheKey(personId: string): string {
    return `employee:person:${personId}`;
  }

  private generateEmployeeCacheKey(employeeId: string): string {
    return `employee:${employeeId}`;
  }

  /**
   * Clear all employee-related cache
   */
  clearAllCache(): void {
    this.cacheService.clearPattern('employee:');
    this.cacheService.clearPattern('employees:');
  }

  /**
   * Force refresh cache for a specific person
   */
  forceRefreshPersonCache(personId: string): void {
    this.cacheService.delete(this.generatePersonCacheKey(personId));
  }

  /**
   * Clear all employee cache and force refresh
   */
  forceRefreshAllEmployeeCache(): void {
    this.clearAllCache();
  }

  /**
   * Debug method to get cache statistics
   */
  getCacheStats(): any {
    return this.cacheService.getDetailedStats();
  }

  /**
   * Clear cache for specific employee
   */
  clearEmployeeCache(employeeId: string): void {
    this.cacheService.delete(this.generateEmployeeCacheKey(employeeId));
    this.cacheService.clearPattern('employees:');
  }

  /**
   * Clear cache for employee by person ID
   */
  clearEmployeeCacheByPersonId(personId: string): void {
    this.cacheService.delete(this.generatePersonCacheKey(personId));
    this.cacheService.clearPattern('employees:');
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
