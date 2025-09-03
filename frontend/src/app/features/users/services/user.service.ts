import { inject, Injectable } from '@angular/core';
import { User } from '@users/interfaces/user.interface';
import { Observable, of, tap, map, catchError, throwError } from 'rxjs';
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
    const cacheKey = this.buildCacheKey(options);
    const cached = this.cacheService.get<PaginatedResponseDTO<User>>(cacheKey);

    if (cached) {
      return of(cached);
    }

    return this.userApi.getUsers(options)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'users')),
        tap((users) => this.cacheService.set(cacheKey, users)),
        catchError((error) => this.propagateError(error, 'getting users'))
      );
  }

  getAvailableUsers(options: SearchOptions): Observable<PaginatedResponseDTO<User>> {
    const cacheKey = `available_${this.buildCacheKey(options)}`;
    const cached = this.cacheService.get<PaginatedResponseDTO<User>>(cacheKey);

    if (cached) {
      return of(cached);
    }

    return this.userApi.getAvailableUsers(options)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'users')),
        tap((users) => this.cacheService.set(cacheKey, users)),
        catchError((error) => this.propagateError(error, 'getting available users'))
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
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'user')),
        tap((user) => this.cacheService.set(cacheKey, user)),
        catchError((error) => this.propagateError(error, 'getting user'))
      );
  }

  createUser(userData: Partial<User>): Observable<User> {
    return this.userApi.createUser(userData)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'user')),
        tap(() => this.clearUsersCache()),
        catchError((error) => this.propagateError(error, 'creating user'))
      );
  }

  updateUser(id: string, updatedUser: Partial<User>): Observable<User> {
    return this.userApi.updateUser(id, updatedUser)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'user')),
        tap((user: User) => this.updateUserCache(user)),
        catchError((error) => this.propagateError(error, 'updating user'))
      );
  }

  deleteUser(id: string): Observable<boolean> {
    return this.userApi.deleteUser(id)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'void')),
        tap(() => this.clearUserCache(id)),
        catchError((error) => this.propagateError(error, 'deleting user'))
      );
  }

  private buildCacheKey(options: SearchOptions): string {
    const { page, size, search, sort, order } = options;
    return `users:${page}:${size}:${search || ''}:${sort || ''}:${order || ''}`;
  }

  private extractDataFromResponse(apiResponse: ApiResponse<any>, type: string): any {
    if (!apiResponse.success) {
      throw new Error(apiResponse.message || `Error ${type}`);
    }

    if (type === 'boolean' || type === 'void') {
      return true;
    }

    if (!apiResponse.data) {
      throw new Error(apiResponse.message || `Error ${type}`);
    }

    return apiResponse.data;
  }

  private propagateError(error: any, operation: string): Observable<never> {
    return throwError(() => error);
  }

  private updateUserCache(user: User): void {
    this.cacheService.set(`user:${user.id}`, user);
    this.clearUsersCache();
  }

  private clearUserCache(userId: string): void {
    this.cacheService.delete(`user:${userId}`);
    this.clearUsersCache();
  }

  private clearUsersCache(): void {
    this.cacheService.clearPattern('users:');
  }

  clearAvailableUsersCache(): void {
    this.cacheService.clearPattern('available_');
  }

  clearAllUsersCache(): void {
    this.clearUsersCache();
    this.clearAvailableUsersCache();
  }

  // Método de utilidad para limpiar cache cuando cambia el estado de asociación de usuarios
  clearCacheOnUserAssociationChange(): void {
    this.clearAvailableUsersCache();
  }
}
