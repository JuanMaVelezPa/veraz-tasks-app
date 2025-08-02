import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '@env/environment';
import { SignInRequest, SignInResponse } from '@auth/interfaces/sign-in.interface';
import { SignUpRequest } from '@auth/interfaces/sign-up.interface';
import { AuthResponse } from '@auth/interfaces/auth-response.interface';
import { HttpErrorService } from '@shared/services/http-error.service';
import { ApiResponse } from '@shared/interfaces/api-response.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthApiService {
  private http = inject(HttpClient);
  private httpErrorService = inject(HttpErrorService);
  private readonly baseUrl = `${environment.apiUrl}/auth`;

  signIn(request: SignInRequest): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(`${this.baseUrl}/sign-in`, request)
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error sign-in'))
      );
  }

  signUp(request: SignUpRequest): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(`${this.baseUrl}/sign-up`, request)
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error sign-up'))
      );
  }

  checkAuthStatus(): Observable<ApiResponse<AuthResponse>> {
    return this.http.get<ApiResponse<AuthResponse>>(`${this.baseUrl}/check-status`)
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error check-status'))
      );
  }

  refreshToken(): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(`${this.baseUrl}/refresh-token`, {})
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error refresh'))
      );
  }
}
