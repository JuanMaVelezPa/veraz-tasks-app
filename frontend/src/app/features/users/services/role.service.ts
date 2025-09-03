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
        map((apiResponse) => this.extractRolesFromResponse(apiResponse)),
        catchError((error: HttpErrorResponse) => this.handleError(error, 'getting roles'))
      );
  }

  getActiveRoles(): Observable<RoleResponse[]> {
    if (this.activeRolesCache) {
      return of(this.activeRolesCache);
    }

    return this.http.get<ApiResponse<RoleResponse[]>>(`${environment.apiUrl}/roles/active`)
      .pipe(
        map((apiResponse) => this.handleActiveRolesSuccess(apiResponse)),
        catchError((error: HttpErrorResponse) => this.handleError(error, 'getting active roles'))
      );
  }

  clearCache(): void {
    this.activeRolesCache = null;
  }

  private extractRolesFromResponse(apiResponse: ApiResponse<any>): RoleResponse[] {
    if (!apiResponse.success || !apiResponse.data) {
      throw new Error(apiResponse.message || 'Error getting roles');
    }

    const rolesData = apiResponse.data;
    return rolesData.roles || rolesData;
  }

  private handleActiveRolesSuccess(apiResponse: ApiResponse<any>): RoleResponse[] {
    const roles = this.extractRolesFromResponse(apiResponse);
    this.activeRolesCache = roles;
    return roles;
  }

  private handleError(error: any, context: string): Observable<RoleResponse[]> {
    console.error(`Error ${context}:`, error);
    return of([]);
  }
}
