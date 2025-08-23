import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Employee, EmployeeCreateRequest, EmployeeUpdateRequest } from '@employee/interfaces/employee.interface';
import { SearchOptions } from '@shared/interfaces/search.interface';
import { PaginatedResponseDTO } from '@shared/interfaces/pagination.interface';
import { ApiResponse } from '@shared/interfaces/api-response.interface';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root'
})
export class EmployeeApiService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/employees`;

  getEmployees(options: SearchOptions): Observable<ApiResponse<PaginatedResponseDTO<Employee>>> {
    let params = new HttpParams()
      .set('page', options.page?.toString() || '0')
      .set('size', options.size?.toString() || '10');

    if (options.sort) {
      params = params.set('sortBy', options.sort);
    }

    if (options.order) {
      params = params.set('sortDirection', options.order);
    }

    if (options.search) {
      params = params.set('search', options.search);
    }

    return this.http.get<ApiResponse<PaginatedResponseDTO<Employee>>>(this.baseUrl, { params });
  }

  getEmployeeById(id: string): Observable<ApiResponse<Employee>> {
    return this.http.get<ApiResponse<Employee>>(`${this.baseUrl}/${id}`);
  }

  createEmployee(employeeData: EmployeeCreateRequest): Observable<ApiResponse<Employee>> {
    return this.http.post<ApiResponse<Employee>>(this.baseUrl, employeeData);
  }

  updateEmployee(id: string, employeeData: EmployeeUpdateRequest): Observable<ApiResponse<Employee>> {
    return this.http.patch<ApiResponse<Employee>>(`${this.baseUrl}/${id}`, employeeData);
  }

  deleteEmployee(id: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }

  getEmployeeByPersonId(personId: string): Observable<ApiResponse<Employee | null>> {
    return this.http.get<ApiResponse<Employee>>(`${this.baseUrl}/by-person/${personId}`);
  }
}
