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
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userApi = inject(UserApiService);

  private usersCache = new Map<string, PaginatedResponseDTO<User>>();
  private userCache = new Map<string, User>();

  getUsers(options: UserSearchOptions): Observable<PaginatedResponseDTO<User>> {
    const { page, size, sort, order, search } = options;
    const cacheKey = `users-page-${page}-size-${size}-sort-${sort}-order-${order}-search-${search}`;

    if (this.usersCache.has(cacheKey)) {
      return of(this.usersCache.get(cacheKey)!);
    }

    return this.userApi.getUsers(options)
      .pipe(
        map((apiResponse) => this.handleGetUsersSuccess(apiResponse)),
        tap(response => {
          this.usersCache.set(cacheKey, response);
        }),
        catchError((error) => this.handleGetUsersError(error))
      );
  }

  getUserById(id: string): Observable<User> {
    if (id === 'new') {
      return of(emptyUser)
    }

    if (this.userCache.has(id)) {
      return of(this.userCache.get(id)!)
    }

    return this.userApi.getUserById(id)
      .pipe(
        map((apiResponse) => this.handleGetUserByIdSuccess(apiResponse)),
        tap((user) => {
          this.userCache.set(id, user);
        }),
        catchError((error) => this.handleGetUserByIdError(error))
      );
  }

  createUser(userData: Partial<User>): Observable<User> {
    return this.userApi.createUser(userData)
      .pipe(
        map((apiResponse) => this.handleCreateUserSuccess(apiResponse)),
        tap(() => {
          this.usersCache.clear();
        }),
        catchError((error) => this.handleCreateUserError(error))
      );
  }

  updateUser(id: string, updatedUser: Partial<User>): Observable<User> {
    return this.userApi.updateUser(id, updatedUser)
      .pipe(
        map((apiResponse) => this.handleUpdateUserSuccess(apiResponse)),
        tap((user: User) => {
          this.userCache.set(user.id, user);
          this.usersCache.clear();
        }),
        catchError((error) => this.handleUpdateUserError(error))
      );
  }

  deleteUser(id: string): Observable<boolean> {
    return this.userApi.deleteUser(id)
      .pipe(
        map((apiResponse) => this.handleDeleteUserSuccess(apiResponse)),
        tap(() => {
          this.userCache.delete(id);
          this.usersCache.clear();
        }),
        catchError((error) => this.handleDeleteUserError(error))
      );
  }

  private handleGetUsersSuccess(apiResponse: ApiResponse<PaginatedResponseDTO<any>>): PaginatedResponseDTO<User> {
    if (!apiResponse.success || !apiResponse.data) {
      throw new Error(apiResponse.message || 'Error getting users');
    }

    // Mapear cada usuario del backend al formato frontend
    const mappedUsers = apiResponse.data.data.map(user => this.mapBackendUserToFrontendUser(user));

    return {
      data: mappedUsers,
      pagination: apiResponse.data.pagination
    };
  }

  private handleGetUsersError(error: any): Observable<PaginatedResponseDTO<User>> {
    console.error('Error getting users:', error);

    // Extraer mensaje de error de ErrorResponse si está disponible
    let errorMessage = 'Error getting users';
    if (error?.errorResponse?.message) {
      errorMessage = error.errorResponse.message;
    } else if (error?.message) {
      errorMessage = error.message;
    }

    console.error(errorMessage);

    return of({
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
    });
  }

  private handleGetUserByIdSuccess(apiResponse: ApiResponse<User>): User {
    if (!apiResponse.success || !apiResponse.data) {
      throw new Error(apiResponse.message || 'Error getting user');
    }
    return this.mapBackendUserToFrontendUser(apiResponse.data);
  }

  private handleGetUserByIdError(error: any): Observable<User> {
    console.error('Error getting user by id:', error);

    // Extraer mensaje de error de ErrorResponse si está disponible
    let errorMessage = 'Error getting user';
    if (error?.errorResponse?.message) {
      errorMessage = error.errorResponse.message;
    } else if (error?.message) {
      errorMessage = error.message;
    }

    console.error(errorMessage);
    return of(emptyUser);
  }

  private handleCreateUserSuccess(apiResponse: ApiResponse<User>): User {
    if (!apiResponse.success || !apiResponse.data) {
      throw new Error(apiResponse.message || 'Error creating user');
    }
    return this.mapBackendUserToFrontendUser(apiResponse.data);
  }

  private handleCreateUserError(error: any): Observable<User> {
    console.error('Error creating user:', error);

    // Extraer mensaje de error de ErrorResponse si está disponible
    let errorMessage = 'Error creating user';
    if (error?.errorResponse?.message) {
      errorMessage = error.errorResponse.message;
    } else if (error?.message) {
      errorMessage = error.message;
    }

    console.error(errorMessage);
    throw new Error(errorMessage);
  }

  private handleUpdateUserSuccess(apiResponse: ApiResponse<User>): User {
    if (!apiResponse.success || !apiResponse.data) {
      throw new Error(apiResponse.message || 'Error updating user');
    }
    return this.mapBackendUserToFrontendUser(apiResponse.data);
  }

  private handleUpdateUserError(error: any): Observable<User> {
    console.error('Error updating user:', error);

    // Extraer mensaje de error de ErrorResponse si está disponible
    let errorMessage = 'Error updating user';
    if (error?.errorResponse?.message) {
      errorMessage = error.errorResponse.message;
    } else if (error?.message) {
      errorMessage = error.message;
    }

    console.error(errorMessage);
    throw new Error(errorMessage);
  }

  private handleDeleteUserSuccess(apiResponse: ApiResponse<void>): boolean {
    if (!apiResponse.success) {
      throw new Error(apiResponse.message || 'Error deleting user');
    }
    return true;
  }

  private handleDeleteUserError(error: any): Observable<boolean> {
    console.error('Error deleting user:', error);

    // Extraer mensaje de error de ErrorResponse si está disponible
    let errorMessage = 'Error deleting user';
    if (error?.errorResponse?.message) {
      errorMessage = error.errorResponse.message;
    } else if (error?.message) {
      errorMessage = error.message;
    }

    console.error(errorMessage);
    return of(false);
  }

  /**
   * Mapea un usuario del backend al formato esperado por el frontend
   * @param backendUser Usuario del backend (puede tener Set<String> para roles/perms)
   * @returns Usuario en formato frontend
   */
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
