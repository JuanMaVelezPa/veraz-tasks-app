import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { Observable, of, map, catchError } from 'rxjs';
import { RoleResponse } from '../interfaces/role.interface';
import { ApiResponse } from '@shared/interfaces/api-response.interface';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  private http = inject(HttpClient);

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

  private handleGetRolesSuccess(apiResponse: ApiResponse<any>): RoleResponse[] {
    if (!apiResponse.success || !apiResponse.data) {
      throw new Error(apiResponse.message || 'Error getting roles');
    }

    const rolesData = apiResponse.data;
    const roles = rolesData.roles || rolesData;
    return roles;
  }

  private handleGetRolesError(error: any): Observable<RoleResponse[]> {
    console.error('Error getting roles:', error);
    return of([]);
  }

  private handleGetActiveRolesSuccess(apiResponse: ApiResponse<any>): RoleResponse[] {
    if (!apiResponse.success || !apiResponse.data) {
      throw new Error(apiResponse.message || 'Error getting active roles');
    }

    const rolesData = apiResponse.data;
    const roles = rolesData.roles || rolesData;

    this.activeRolesCache = roles;
    return roles;
  }

  private handleGetActiveRolesError(error: any): Observable<RoleResponse[]> {
    console.error('Error getting active roles:', error);
    return of([]);
  }

  clearCache(): void {
    this.activeRolesCache = null;
  }
}
