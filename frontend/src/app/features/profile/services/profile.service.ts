import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';
import { User } from '@users/interfaces/user.interface';
import { Person, PersonCreateRequest, PersonUpdateRequest } from '@person/interfaces/person.interface';
import { ApiResponse } from '@shared/interfaces/api-response.interface';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  getMyProfile(): Observable<ApiResponse<Person>> {
    return this.http.get<ApiResponse<Person>>(`${this.apiUrl}/profile`);
  }

  checkProfileExists(): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(`${this.apiUrl}/profile/exists`);
  }

  createMyProfile(personData: PersonCreateRequest): Observable<ApiResponse<Person>> {
    return this.http.post<ApiResponse<Person>>(`${this.apiUrl}/profile`, personData);
  }

  updateMyProfile(personData: PersonUpdateRequest): Observable<ApiResponse<Person>> {
    return this.http.patch<ApiResponse<Person>>(`${this.apiUrl}/profile`, personData);
  }

  deleteMyProfile(): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/profile`);
  }

  getMyUserAccount(): Observable<ApiResponse<User>> {
    return this.http.get<ApiResponse<User>>(`${this.apiUrl}/profile/account`);
  }

  updateMyUserAccount(userData: any): Observable<ApiResponse<User>> {
    return this.http.patch<ApiResponse<User>>(`${this.apiUrl}/profile/account`, userData);
  }
}
