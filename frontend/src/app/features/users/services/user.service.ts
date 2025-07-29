import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { User } from '@auth/interfaces/user';
import { environment } from '@env/environment';
import { Observable, of, tap } from 'rxjs';
import { UserSearchOptions } from '../../../shared/interfaces/search.interface';
import { UsersResponse } from '../interfaces/userResponse.interface';

const emptyUser: User = {
  id: 'new',
  username: '',
  email: '',
  isActive: true,
  createdAt: new Date().toISOString(),
  updatedAt: new Date().toISOString(),
  roles: ['USER'],
  perms: []
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private http = inject(HttpClient);

  private usersCache = new Map<string, UsersResponse>();
  private userCache = new Map<string, User>();

  getUsers(options: UserSearchOptions): Observable<UsersResponse> {
    const { page, size, sort, order, search } = options;
    const cacheKey = `users-page-${page}-size-${size}-sort-${sort}-order-${order}-search-${search}`;

    if (this.usersCache.has(cacheKey)) {
      return of(this.usersCache.get(cacheKey)!);
    }

    const params: any = {
      page,
      size,
      sortBy: sort,
      sortDirection: order
    };

    if (search && search.trim()) {
      params.search = search.trim();
    }

    return this.http
      .get<UsersResponse>(`${environment.apiUrl}/users`, { params })
      .pipe(
        tap(response => {
          this.usersCache.set(cacheKey, response);
        })
      );
  }

  getUserById(id: string): Observable<User> {

    if (id === 'new') { return of(emptyUser) }

    if (this.userCache.has(id)) { return of(this.userCache.get(id)!) }

    return this.http.get<User>(`${environment.apiUrl}/users/${id}`)
      .pipe(
        tap((user) => {
          this.userCache.set(id, user);
        })
      );
  }

  createUser(userData: Partial<User>): Observable<User> {
    // Remover permisos del envío ya que el backend los maneja automáticamente
    const { perms, ...dataToSend } = userData;
    return this.http.post<User>(`${environment.apiUrl}/users`, dataToSend)
      .pipe(
        tap(() => {
          // Limpiar cache de usuarios para refrescar la lista
          this.usersCache.clear();
        })
      );
  }

  updateUser(id: string, userData: Partial<User>): Observable<User> {
    // Remover permisos del envío ya que el backend los maneja automáticamente
    const { perms, ...dataToSend } = userData;
    return this.http.put<User>(`${environment.apiUrl}/users/${id}`, dataToSend)
      .pipe(
        tap((updatedUser) => {
          // Actualizar cache del usuario específico
          this.userCache.set(id, updatedUser);
          // Limpiar cache de usuarios para refrescar la lista
          this.usersCache.clear();
        })
      );
  }

}
