import { inject, Injectable } from '@angular/core';
import { User } from '@users/interfaces/user.interface';
import { Observable, of, tap, map, catchError } from 'rxjs';
import { SearchOptions } from '@shared/interfaces/search.interface';
import { UserApiService } from './user-api.service';
import { ApiResponse } from '@shared/interfaces/api-response.interface';
import { PaginatedResponseDTO } from '@shared/interfaces/pagination.interface';
import { CacheService } from '@shared/services/cache.service';

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
  private cacheService = inject(CacheService);

  getUsers(options: SearchOptions): Observable<PaginatedResponseDTO<User>> {
    const cacheKey = this.generateCacheKey(options);
    const cached = this.cacheService.get<PaginatedResponseDTO<User>>(cacheKey);

    if (cached) {
      return of(cached);
    }

    return this.userApi.getUsers(options)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'users')),
        tap(response => this.cacheService.set(cacheKey, response)),
        catchError(() => of(emptyPagination))
      );
  }

  getUserById(id: string): Observable<User> {
    if (id === 'new') return of(emptyUser);

    const cacheKey = `user:${id}`;
    const cached = this.cacheService.get<User>(cacheKey);

    if (cached) {
      return of(cached);
    }

    return this.userApi.getUserById(id)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'user')),
        tap((user) => this.cacheService.set(cacheKey, user)),
        catchError(() => of(emptyUser))
      );
  }

  createUser(userData: Partial<User>): Observable<User> {
    return this.userApi.createUser(userData)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'user')),
        tap((user: User) => this.cacheService.clearPattern('users:')),
        catchError((error) => this.handleError(error, 'creating user'))
      );
  }

  updateUser(id: string, updatedUser: Partial<User>): Observable<User> {
    return this.userApi.updateUser(id, updatedUser)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'user')),
        tap((user: User) => {
          this.cacheService.set(`user:${user.id}`, user);
          this.cacheService.clearPattern('users:');
        }),
        catchError((error) => this.handleError(error, 'updating user'))
      );
  }

  deleteUser(id: string): Observable<boolean> {
    return this.userApi.deleteUser(id)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'boolean')),
        tap(() => {
          this.cacheService.delete(`user:${id}`);
          this.cacheService.clearPattern('users:');
        }),
        catchError((error) => this.handleError(error, 'deleting user'))
      );
  }

  private generateCacheKey(options: SearchOptions): string {
    const { page, size, search, sort, order } = options;
    return `users:${page}:${size}:${search || ''}:${sort || ''}:${order || ''}`;
  }

  private handleSuccess(apiResponse: ApiResponse<any>, type: string): any {
    if (!apiResponse.success || !apiResponse.data) {
      throw new Error(apiResponse.message || `Error ${type}`);
    }
    return apiResponse.data;
  }

  private handleError(error: any, operation: string): Observable<never> {
    const errorMessage = error?.errorResponse?.message || error?.message || `Error ${operation}`;
    throw new Error(errorMessage);
  }
}
