import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthResponse } from '@auth/interfaces/auth-response.interface';
import { environment } from '@env/environment';
import { SignInRequest } from '@auth/interfaces/sign-in.interface';
import { SignUpRequest } from '@auth/interfaces/sign-up.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthApiService {
  private http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/auth`;

  signIn(credentials: SignInRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/sign-in`, credentials)
      .pipe(
        catchError((error: HttpErrorResponse) => this.handleError(error, 'Error en sign-in'))
      );
  }

  signUp(userData: SignUpRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/sign-up`, userData)
      .pipe(
        catchError((error: HttpErrorResponse) => this.handleError(error, 'Error en sign-up'))
      );
  }

  checkStatus(): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(`${this.baseUrl}/check-status`)
      .pipe(
        catchError((error: HttpErrorResponse) => this.handleError(error, 'Error verificando estado'))
      );
  }

  refreshToken(): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/refresh`, {})
      .pipe(
        catchError((error: HttpErrorResponse) => this.handleError(error, 'Error refrescando token'))
      );
  }

  signOut(): Observable<any> {
    return this.http.post(`${this.baseUrl}/sign-out`, {})
      .pipe(
        catchError((error: HttpErrorResponse) => {
          return [];
        })
      );
  }

  private handleError(error: HttpErrorResponse, context: string): Observable<never> {

    let errorMessage = 'Unexpected error';

    switch (error.status) {
      case 400:
      case 401:
        errorMessage = error.error.message;
        break;
      case 403:
        errorMessage = 'Your account has been blocked. Contact the administrator.';
        break;
      case 409:
        errorMessage = 'User already exists. Try with a different username or email.';
        break;
      case 422:
        errorMessage = 'Registration data does not meet requirements.';
        break;
      case 429:
        errorMessage = 'Too many login attempts. Please wait a few minutes.';
        break;
      case 500:
        errorMessage = 'Internal server error';
        break;
      case 0:
        errorMessage = 'Connection error. Please check your internet connection.';
        break;
      default:
        errorMessage = `Error ${error.status}: ${error.message}`;
    }

    const customError = {
      message: errorMessage,
      status: error.status,
      originalError: error,
      context
    };

    return throwError(() => customError);
  }

}
