import { inject, Injectable } from '@angular/core';
import { User } from '@users/interfaces/user.interface';
import { Observable, of, tap, map, catchError } from 'rxjs';
import { UserSearchOptions } from '../../../shared/interfaces/search.interface';
import { UserApiService } from './user-api.service';
import { ApiResponse } from '@shared/interfaces/api-response.interface';
import { PaginatedResponseDTO } from '@shared/interfaces/pagination.interface';

const emptyUser: User = {
  id: 'new',
  username: '',
  email: '',
  isActive: true,
  createdAt: new Date().toISOString(),
  updatedAt: new Date().toISOString(),
  roles: ['USER'],
  perms: []
};

const emptyPagination: PaginatedResponseDTO<User> = {
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
export class UserService {

  private userApi = inject(UserApiService);

  private usersCache = new Map<string, PaginatedResponseDTO<User>>();
  private userCache = new Map<string, User>();

  getUsers(options: UserSearchOptions): Observable<PaginatedResponseDTO<User>> {
    const cacheKey = this.generateCacheKey(options);

    if (this.usersCache.has(cacheKey)) {
      return of(this.usersCache.get(cacheKey)!);
    }

    return this.userApi.getUsers(options)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'users')),
        tap(response => this.usersCache.set(cacheKey, response)),
        catchError(() => of(emptyPagination))
      );
  }

  getUserById(id: string): Observable<User> {
    if (id === 'new') return of(emptyUser);
    if (this.userCache.has(id)) return of(this.userCache.get(id)!);

    return this.userApi.getUserById(id)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'user')),
        tap((user) => this.userCache.set(id, user)),
        catchError(() => of(emptyUser))
      );
  }

  createUser(userData: Partial<User>): Observable<User> {
    return this.userApi.createUser(userData)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'user')),
        tap(() => this.clearUsersCache()),
        catchError((error) => this.handleError(error, 'creating user'))
      );
  }

  updateUser(id: string, updatedUser: Partial<User>): Observable<User> {
    return this.userApi.updateUser(id, updatedUser)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'user')),
        tap((user: User) => {
          this.userCache.set(user.id, user);
          this.clearUsersCache();
        }),
        catchError((error) => this.handleError(error, 'updating user'))
      );
  }

  deleteUser(id: string): Observable<boolean> {
    return this.userApi.deleteUser(id)
      .pipe(
        map(() => true),
        tap(() => {
          this.userCache.delete(id);
          this.clearUsersCache();
        }),
        catchError(() => of(false))
      );
  }

  private generateCacheKey(options: UserSearchOptions): string {
    const { page, size, sort, order, search } = options;
    return `users-page-${page}-size-${size}-sort-${sort}-order-${order}-search-${search}`;
  }

  private clearUsersCache(): void {
    this.usersCache.clear();
  }

  private handleSuccess(apiResponse: ApiResponse<any>, type: string): any {
    if (!apiResponse.success || !apiResponse.data) {
      throw new Error(apiResponse.message || `Error ${type}`);
    }

    if (type === 'users') {
      return this.mapPaginatedUsers(apiResponse.data);
    } else if (type === 'user') {
      return this.mapBackendUserToFrontendUser(apiResponse.data);
    }

    return apiResponse.data;
  }

  private handleError(error: any, operation: string): Observable<never> {
    const errorMessage = error?.errorResponse?.message || error?.message || `Error ${operation}`;
    throw new Error(errorMessage);
  }

  private mapPaginatedUsers(backendData: any): PaginatedResponseDTO<User> {
    const mappedUsers = backendData.data.map((user: any) =>
      this.mapBackendUserToFrontendUser(user)
    );

    return {
      data: mappedUsers,
      pagination: backendData.pagination
    };
  }

  private mapBackendUserToFrontendUser(backendUser: any): User {
    return {
      id: String(backendUser.id),
      username: backendUser.username,
      email: backendUser.email,
      isActive: backendUser.isActive,
      createdAt: backendUser.createdAt,
      updatedAt: backendUser.updatedAt,
      roles: Array.isArray(backendUser.roles) ? backendUser.roles : Array.from(backendUser.roles || []),
      perms: Array.isArray(backendUser.perms) ? backendUser.perms : Array.from(backendUser.perms || [])
    };
  }
}
