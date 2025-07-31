import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '@env/environment';
import { SignInRequest } from '@auth/interfaces/sign-in.interface';
import { SignUpRequest } from '@auth/interfaces/sign-up.interface';
import { HttpErrorService } from '@shared/services/http-error.service';

@Injectable({
  providedIn: 'root'
})
export class AuthApiService {
  private http = inject(HttpClient);
  private httpErrorService = inject(HttpErrorService);
  private readonly baseUrl = `${environment.apiUrl}/auth`;

  signIn(request: SignInRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/sign-in`, request)
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error sign-in'))
      );
  }

  signUp(request: SignUpRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/sign-up`, request)
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error sign-up'))
      );
  }

  checkAuthStatus(): Observable<any> {
    return this.http.get(`${this.baseUrl}/check-status`)
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error check-status'))
      );
  }

  refreshToken(): Observable<any> {
    return this.http.post(`${this.baseUrl}/refresh-token`, {})
      .pipe(
        catchError((error: HttpErrorResponse) => this.httpErrorService.handleError(error, 'Error refresh'))
      );
  }
}
