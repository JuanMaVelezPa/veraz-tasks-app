import { computed, inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AuthResponse, AuthStatus, CacheAuthResponse } from '@auth/interfaces/auth-response';
import { User } from '@auth/interfaces/user';
import { catchError, map, Observable, of, tap, throwError } from 'rxjs';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private http = inject(HttpClient);

  private _user = signal<User | null>(null);
  private _token = signal<string | null>(null);
  private _authStatus = signal<AuthStatus>('checking');

  private readonly CACHE_DURATION = 60 * 60 * 1000; // 1 hour
  private readonly CACHE_KEY = 'authResponseCache';

  user = computed(() => this._user());
  token = computed(() => this._token());
  authStatus = computed(() => {
    if (this._authStatus() === 'checking') { return 'checking'; }
    if (this._authStatus() === 'authenticated') { return 'authenticated'; }
    return 'not-authenticated';
  });

  signIn(usernameOrEmail: string, password: string): Observable<boolean> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/signIn`, { usernameOrEmail, password })
      .pipe(
        map((response) => this.handleAuthSuccess(response)),
        catchError((error: HttpErrorResponse) => this.handleAuthError(error))
      );
  }

  signUp(username: string, email: string, password: string): Observable<boolean> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/signUp`, { username, email, password })
      .pipe(
        map((response) => {
          // Para signUp, el backend no devuelve token, solo crea el usuario
          if (response.user && !response.token) {
            // Usuario creado exitosamente, pero no autenticado automáticamente
            console.log('Usuario creado exitosamente:', response.message);
            return false; // No autenticado automáticamente
          }
          return this.handleAuthSuccess(response);
        }),
        catchError((error: HttpErrorResponse) => this.handleAuthError(error))
      );
  }

  signOut(): Observable<boolean> {
    this._authStatus.set('not-authenticated');
    this._user.set(null);
    this._token.set(null);
    this.clearCache();
    return of(true);
  }

  checkAuthStatus(): Observable<boolean> {
    const currentToken = this._token();
    if (!currentToken) { return this.signOut(); }

    const authResponseCache = this.getCache();
    if (authResponseCache && this.isCacheValid(authResponseCache)) {
      this._authStatus.set(authResponseCache.authStatus);
      this._user.set(authResponseCache.user ?? null);
      this._token.set(authResponseCache.token ?? null);
      return of(true);
    }

    return this.http.get<AuthResponse>(`${environment.apiUrl}/auth/check-status`, {
      headers: {
        'Authorization': `Bearer ${currentToken}`
      }
    })
      .pipe(
        map((response) => this.handleAuthSuccess(response)),
        catchError((error: HttpErrorResponse) => this.handleAuthError(error))
      );
  }

  private getCache(): CacheAuthResponse | null {
    try {
      const cacheData = localStorage.getItem(this.CACHE_KEY);
      return cacheData ? JSON.parse(cacheData) : null;
    } catch (error) {
      this.clearCache();
      return null;
    }
  }

  private isCacheValid(cache: CacheAuthResponse): boolean {
    const now = new Date().getTime();
    return now < cache.lastAuthCheck + this.CACHE_DURATION;
  }

  private clearCache(): void {
    localStorage.removeItem(this.CACHE_KEY);
  }

  private isValidToken(token: string): boolean {
    return Boolean(token && token.length > 0 && token.split('.').length === 3);
  }

  private handleAuthSuccess(response: AuthResponse): boolean {
    if (!response || !response.user) { return false; }

    // Para signIn, debe tener token
    if (!response.token) {
      console.warn('No token received in auth response');
      return false;
    }

    if (!this.isValidToken(response.token)) { return false; }

    const cacheValue: CacheAuthResponse = {
      authStatus: 'authenticated',
      user: response.user,
      token: response.token,
      lastAuthCheck: Date.now()
    };

    this._user.set(response.user);
    this._token.set(response.token);
    this._authStatus.set('authenticated');

    localStorage.setItem(this.CACHE_KEY, JSON.stringify(cacheValue));

    return true;
  }

  private handleAuthError(error: HttpErrorResponse): Observable<boolean> {

    switch (error.status) {
      case 401:
        console.warn('Credenciales inválidas');
        break;
      case 409:
        console.warn('Usuario ya existe');
        break;
      case 400:
        console.warn('Datos inválidos en la solicitud');
        break;
      case 500:
        console.error('Error interno del servidor');
        break;
      default:
        console.error('Error inesperado:', error.message);
    }

    this._authStatus.set('not-authenticated');
    this._user.set(null);
    this._token.set(null);
    this.clearCache();
    return of(false);
  }

}
