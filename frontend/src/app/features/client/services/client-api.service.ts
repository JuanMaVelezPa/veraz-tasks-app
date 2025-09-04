import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { catchError, Observable, of, throwError } from 'rxjs';
import { environment } from '@env/environment';
import { Client, ClientCreateRequest, ClientUpdateRequest } from '@client/interfaces/client.interface';
import { ApiResponse } from '@shared/interfaces/api-response.interface';
import { PaginatedResponseDTO } from '@shared/interfaces/pagination.interface';
import { SearchOptions } from '@shared/interfaces/search.interface';

@Injectable({
  providedIn: 'root'
})
export class ClientApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/clients`;

  getClients(options: SearchOptions): Observable<ApiResponse<PaginatedResponseDTO<Client>>> {
    const params = this.buildSearchParams(options);
    return this.http.get<ApiResponse<PaginatedResponseDTO<Client>>>(this.baseUrl, { params });
  }

  getClientById(id: string): Observable<ApiResponse<Client>> {
    return this.http.get<ApiResponse<Client>>(`${this.baseUrl}/${id}`);
  }

  getClientByPersonId(personId: string): Observable<ApiResponse<Client | null>> {
    return this.http.get<ApiResponse<Client>>(`${this.baseUrl}/by-person/${personId}`)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          if (error.status === 404) {
            return of({
              data: null,
              status: error.status,
              message: error.message,
              success: false,
              errors: []
            });
          }
          return throwError(() => error);
        })
      );
  }

  createClient(clientData: ClientCreateRequest): Observable<ApiResponse<Client>> {
    return this.http.post<ApiResponse<Client>>(this.baseUrl, clientData);
  }

  updateClient(id: string, clientData: ClientUpdateRequest): Observable<ApiResponse<Client>> {
    return this.http.patch<ApiResponse<Client>>(`${this.baseUrl}/${id}`, clientData);
  }

  deleteClient(id: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }

  private buildSearchParams(options: SearchOptions): HttpParams {
    let params = new HttpParams();

    if (options.page) {
      params = params.set('page', options.page.toString());
    }

    if (options.size) {
      params = params.set('size', options.size.toString());
    }

    if (options.sort) {
      params = params.set('sort', options.sort);
    }

    if (options.search) {
      params = params.set('search', options.search);
    }

    return params;
  }
}
