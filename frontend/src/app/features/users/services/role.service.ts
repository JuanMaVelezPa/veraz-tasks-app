import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { Observable, of, map, catchError } from 'rxjs';
import { RoleResponse } from '../interfaces/role-response.interface';
import { HttpErrorService } from '@shared/services/http-error.service';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  private http = inject(HttpClient);
  private httpErrorService = inject(HttpErrorService);

  private activeRolesCache: RoleResponse[] | null = null;

  getAllRoles(): Observable<RoleResponse[]> {
    return this.http.get<RoleResponse[]>(`${environment.apiUrl}/roles`)
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error getting all roles'))
      );
  }

  getActiveRoles(): Observable<RoleResponse[]> {
    if (this.activeRolesCache) {
      return of(this.activeRolesCache);
    }

    return this.http.get<any>(`${environment.apiUrl}/roles/active`).pipe(
      map(response => {
        let roles: RoleResponse[];

        if (response && response.roles && Array.isArray(response.roles)) {
          roles = response.roles;
        } else if (Array.isArray(response)) {
          roles = response;
        } else {
          roles = [];
        }

        this.activeRolesCache = roles;
        return roles;
      }),
      catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error getting active roles'))
    );
  }

  clearCache(): void {
    this.activeRolesCache = null;
  }
}
