import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';
import { Person, PersonCreateRequest, PersonUpdateRequest, PersonUserAssociation } from '@person/interfaces/person.interface';
import { ApiResponse } from '@shared/interfaces/api-response.interface';
import { PaginatedResponseDTO } from '@shared/interfaces/pagination.interface';
import { SearchOptions } from '@shared/interfaces/search.interface';

@Injectable({
  providedIn: 'root'
})
export class PersonApiService {

  private http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/persons`;

  getPersons(options: SearchOptions): Observable<ApiResponse<PaginatedResponseDTO<any>>> {
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

  getPersonById(id: string): Observable<ApiResponse<Person>> {
    return this.http.get<ApiResponse<Person>>(`${this.baseUrl}/${id}`);
  }

  createPerson(personData: PersonCreateRequest): Observable<ApiResponse<Person>> {
    return this.http.post<ApiResponse<Person>>(`${this.baseUrl}`, personData);
  }

  updatePerson(id: string, personData: PersonUpdateRequest): Observable<ApiResponse<Person>> {
    return this.http.patch<ApiResponse<Person>>(`${this.baseUrl}/${id}`, personData);
  }

  deletePerson(id: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }

  removeUserAssociation(id: string): Observable<ApiResponse<Person>> {
    return this.http.patch<ApiResponse<Person>>(`${this.baseUrl}/remove-user/${id}`, {});
  }

  associateUser(id: string, userId: string): Observable<ApiResponse<Person>> {
    const associationData: PersonUserAssociation = { userId };
    return this.http.patch<ApiResponse<Person>>(`${this.baseUrl}/associate-user/${id}`, associationData);
  }

  getPersonByUserId(userId: string): Observable<ApiResponse<Person>> {
    return this.http.get<ApiResponse<Person>>(`${this.baseUrl}/by-user/${userId}`);
  }
}
