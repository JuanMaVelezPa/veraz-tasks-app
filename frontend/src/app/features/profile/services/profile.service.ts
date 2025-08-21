import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, tap, map, catchError, throwError } from 'rxjs';
import { environment } from '@env/environment';
import { User } from '@users/interfaces/user.interface';
import { Person, PersonCreateRequest, PersonUpdateRequest } from '@person/interfaces/person.interface';
import { ApiResponse } from '@shared/interfaces/api-response.interface';
import { CacheService } from '@shared/services/cache.service';
import { AuthService } from '@auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private http = inject(HttpClient);
  private cacheService = inject(CacheService);
  private authService = inject(AuthService);
  private apiUrl = environment.apiUrl;

  getMyProfile(): Observable<Person | null> {
    return this.http.get<ApiResponse<Person>>(`${this.apiUrl}/profile`)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'person')),
        catchError((error) => {
          // If profile not found (404), return null instead of throwing error
          // This is a valid state for users without associated person
          if (error.status === 404) {
            return of(null);
          }
          return this.handleError(error, 'getting profile');
        })
      );
  }

  checkProfileExists(): Observable<boolean> {
    return this.http.get<ApiResponse<boolean>>(`${this.apiUrl}/profile/exists`)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'boolean')),
        catchError(() => of(false))
      );
  }

  createMyProfile(personData: PersonCreateRequest): Observable<Person> {
    return this.http.post<ApiResponse<Person>>(`${this.apiUrl}/profile`, personData)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'person')),
        tap((person) => this.cacheService.clearPattern('persons:')),
        catchError((error) => this.handleError(error, 'creating profile'))
      );
  }

  updateMyProfile(personData: PersonUpdateRequest): Observable<Person> {
    return this.http.patch<ApiResponse<Person>>(`${this.apiUrl}/profile`, personData)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'person')),
        tap((person) => this.cacheService.clearPattern('persons:')),
        catchError((error) => this.handleError(error, 'updating profile'))
      );
  }

  deleteMyProfile(): Observable<boolean> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/profile`)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'void')),
        tap(() => this.cacheService.clearPattern('persons:')),
        catchError((error) => this.handleError(error, 'deleting profile'))
      );
  }

  getMyUserAccount(): Observable<User> {
    return this.http.get<ApiResponse<User>>(`${this.apiUrl}/profile/account`)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'user')),
        catchError((error) => this.handleError(error, 'getting user account'))
      );
  }

  updateMyUserAccount(userData: any): Observable<User> {
    return this.http.patch<ApiResponse<User>>(`${this.apiUrl}/profile/account`, userData)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'user')),
        tap((user) => {
          this.cacheService.delete(`user:${user.id}`);
          this.cacheService.clearPattern('users:');
          this.authService.updateCurrentUser(user);
        }),
        catchError((error) => this.handleError(error, 'updating user account'))
      );
  }

  private handleSuccess(apiResponse: ApiResponse<any>, type: string): any {
    if (!apiResponse.success) {
      throw new Error(apiResponse.message || `Error ${type}`);
    }

    // For delete operations, data can be null/undefined, which is valid
    if (type === 'boolean' || type === 'void') {
      return true; // Return true for successful operations
    }

    // For other operations, data should exist
    if (!apiResponse.data) {
      throw new Error(apiResponse.message || `Error ${type}`);
    }

    return apiResponse.data;
  }

  private handleError(error: any, operation: string): Observable<never> {
    // Propagar el error original para que HttpErrorService pueda manejarlo correctamente
    return throwError(() => error);
  }
}
