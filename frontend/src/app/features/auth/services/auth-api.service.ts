import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthResponse } from '@auth/interfaces/auth-response.interface';
import { environment } from '@env/environment';

export interface SignInRequest {
  usernameOrEmail: string;
  password: string;
}

export interface SignUpRequest {
  username: string;
  email: string;
  password: string;
}

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
          console.warn('AuthApiService: Error en sign-out del backend:', error);
          return [];
        })
      );
  }

  private handleError(error: HttpErrorResponse, context: string): Observable<never> {
    console.error(`AuthApiService: ${context}:`, error);

    let errorMessage = 'Error inesperado';

    switch (error.status) {
      case 400:
        errorMessage = 'Datos inválidos en la solicitud';
        break;
      case 401:
        errorMessage = 'Credenciales inválidas o token expirado';
        break;
      case 403:
        errorMessage = 'Acceso denegado';
        break;
      case 409:
        errorMessage = 'Usuario ya existe';
        break;
      case 422:
        errorMessage = 'Datos de validación incorrectos';
        break;
      case 500:
        errorMessage = 'Error interno del servidor';
        break;
      case 0:
        errorMessage = 'Error de conexión - verifica tu conexión a internet';
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

  ping(): Observable<any> {
    return this.http.get(`${this.baseUrl}/ping`)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.warn('AuthApiService: Backend no disponible:', error);
          return throwError(() => error);
        })
      );
  }
}
