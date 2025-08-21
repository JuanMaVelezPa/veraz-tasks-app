import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';
import { User } from '@users/interfaces/user.interface';
import { SearchOptions } from '@shared/interfaces/search.interface';
import { ApiResponse } from '@shared/interfaces/api-response.interface';
import { PaginatedResponseDTO } from '@shared/interfaces/pagination.interface';

/**
 * Service for User API endpoints.
 */
@Injectable({
  providedIn: 'root'
})
export class UserApiService {
  private http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/users`;

  getUsers(options: SearchOptions): Observable<ApiResponse<PaginatedResponseDTO<any>>> {
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

    return this.http.get<ApiResponse<PaginatedResponseDTO<any>>>(`${this.baseUrl}`, { params });
  }

  getUserById(id: string): Observable<ApiResponse<User>> {
    return this.http.get<ApiResponse<User>>(`${this.baseUrl}/${id}`);
  }

  createUser(userData: Partial<User>): Observable<ApiResponse<User>> {
    const { perms, ...dataToSend } = userData;
    return this.http.post<ApiResponse<User>>(`${this.baseUrl}`, dataToSend);
  }

  updateUser(id: string, updatedUser: Partial<User>): Observable<ApiResponse<User>> {
    return this.http.patch<ApiResponse<User>>(`${this.baseUrl}/${id}`, updatedUser);
  }

  deleteUser(id: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }
}
