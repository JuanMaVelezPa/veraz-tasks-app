import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '@env/environment';
import { User } from '@users/interfaces/user.interface';
import { UserResponse } from '@users/interfaces/user-response.interface';
import { UsersResponse } from '@users/interfaces/users-response.interface';
import { UserSearchOptions } from '@shared/interfaces/search.interface';
import { HttpErrorService } from '@shared/services/http-error.service';

@Injectable({
  providedIn: 'root'
})
export class UserApiService {
  private http = inject(HttpClient);
  private httpErrorService = inject(HttpErrorService);
  private readonly baseUrl = `${environment.apiUrl}/users`;

  getUsers(options: UserSearchOptions): Observable<UsersResponse> {
    const { page, size, sort, order, search } = options;

    const params: any = {
      page,
      size,
      sortBy: sort,
      sortDirection: order
    };

    if (search && search.trim()) {
      params.search = search.trim();
    }

    return this.http.get<UsersResponse>(`${this.baseUrl}`, { params })
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error getting users'))
      );
  }

  getUserById(id: string): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${id}`)
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error getting user'))
      );
  }

  createUser(userData: Partial<User>): Observable<UserResponse> {
    const { perms, ...dataToSend } = userData;
    return this.http.post<UserResponse>(`${this.baseUrl}`, dataToSend)
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error creating user'))
      );
  }

  updateUser(id: string, updatedUser: Partial<User>): Observable<UserResponse> {
    return this.http.patch<UserResponse>(`${this.baseUrl}/${id}`, updatedUser)
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error updating user'))
      );
  }

  deleteUser(id: string): Observable<UserResponse> {
    return this.http.delete<UserResponse>(`${this.baseUrl}/${id}`)
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error deleting user'))
      );
  }
}
