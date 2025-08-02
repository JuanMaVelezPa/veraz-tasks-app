import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { Observable, of, map, catchError } from 'rxjs';
import { RoleResponse } from '../interfaces/role-response.interface';
import { HttpErrorService } from '@shared/services/http-error.service';
import { ApiResponse } from '@shared/interfaces/api-response.interface';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  private http = inject(HttpClient);
  private httpErrorService = inject(HttpErrorService);

  private activeRolesCache: RoleResponse[] | null = null;

  getAllRoles(): Observable<RoleResponse[]> {
    return this.http.get<ApiResponse<RoleResponse[]>>(`${environment.apiUrl}/roles`)
      .pipe(
        map((apiResponse) => this.handleGetRolesSuccess(apiResponse)),
        catchError((error: HttpErrorResponse) => this.handleGetRolesError(error))
      );
  }

  getActiveRoles(): Observable<RoleResponse[]> {
    if (this.activeRolesCache) {
      return of(this.activeRolesCache);
    }

    return this.http.get<ApiResponse<RoleResponse[]>>(`${environment.apiUrl}/roles/active`)
      .pipe(
        map((apiResponse) => this.handleGetActiveRolesSuccess(apiResponse)),
        catchError((error: HttpErrorResponse) => this.handleGetActiveRolesError(error))
      );
  }

  private handleGetRolesSuccess(apiResponse: ApiResponse<RoleResponse[]>): RoleResponse[] {
    if (!apiResponse.success || !apiResponse.data) {
      throw new Error(apiResponse.message || 'Error getting roles');
    }
    return apiResponse.data;
  }

  private handleGetRolesError(error: any): Observable<RoleResponse[]> {
    console.error('Error getting roles:', error);

    // Extraer mensaje de error de ErrorResponse si está disponible
    let errorMessage = 'Error getting roles';
    if (error?.errorResponse?.message) {
      errorMessage = error.errorResponse.message;
    } else if (error?.message) {
      errorMessage = error.message;
    }

    console.error(errorMessage);
    return of([]);
  }

  private handleGetActiveRolesSuccess(apiResponse: ApiResponse<RoleResponse[]>): RoleResponse[] {
    if (!apiResponse.success || !apiResponse.data) {
      throw new Error(apiResponse.message || 'Error getting active roles');
    }

    const roles = apiResponse.data;
    this.activeRolesCache = roles;
    return roles;
  }

  private handleGetActiveRolesError(error: any): Observable<RoleResponse[]> {
    console.error('Error getting active roles:', error);

    // Extraer mensaje de error de ErrorResponse si está disponible
    let errorMessage = 'Error getting active roles';
    if (error?.errorResponse?.message) {
      errorMessage = error.errorResponse.message;
    } else if (error?.message) {
      errorMessage = error.message;
    }

    console.error(errorMessage);
    return of([]);
  }

  clearCache(): void {
    this.activeRolesCache = null;
  }
}
