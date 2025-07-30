import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { Observable, of, map } from 'rxjs';
import { RoleResponse } from '../interfaces/role-response.interface';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  private http = inject(HttpClient);

  private activeRolesCache: RoleResponse[] | null = null;

  getAllRoles(): Observable<RoleResponse[]> {
    return this.http.get<RoleResponse[]>(`${environment.apiUrl}/roles`);
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
      })
    );
  }

  clearCache(): void {
    this.activeRolesCache = null;
  }
}
