import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '@env/environment';
import { User } from '@users/interfaces/user.interface';
import { UserSearchOptions } from '@shared/interfaces/search.interface';
import { HttpErrorService } from '@shared/services/http-error.service';
import { ApiResponse } from '@shared/interfaces/api-response.interface';
import { PaginatedResponseDTO } from '@shared/interfaces/pagination.interface';

@Injectable({
  providedIn: 'root'
})
export class UserApiService {
  private http = inject(HttpClient);
  private httpErrorService = inject(HttpErrorService);
  private readonly baseUrl = `${environment.apiUrl}/users`;

  getUsers(options: UserSearchOptions): Observable<ApiResponse<PaginatedResponseDTO<any>>> {
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

    return this.http.get<ApiResponse<PaginatedResponseDTO<any>>>(`${this.baseUrl}`, { params })
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error getting users'))
      );
  }

  getUserById(id: string): Observable<ApiResponse<User>> {
    return this.http.get<ApiResponse<User>>(`${this.baseUrl}/${id}`)
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error getting user'))
      );
  }

  createUser(userData: Partial<User>): Observable<ApiResponse<User>> {
    const { perms, ...dataToSend } = userData;
    return this.http.post<ApiResponse<User>>(`${this.baseUrl}`, dataToSend)
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error creating user'))
      );
  }

  updateUser(id: string, updatedUser: Partial<User>): Observable<ApiResponse<User>> {
    return this.http.patch<ApiResponse<User>>(`${this.baseUrl}/${id}`, updatedUser)
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error updating user'))
      );
  }

  deleteUser(id: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`)
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error deleting user'))
      );
  }
}
