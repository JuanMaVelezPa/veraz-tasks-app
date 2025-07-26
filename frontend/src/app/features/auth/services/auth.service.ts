import { inject, Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AuthResponse } from '@auth/interfaces/auth-response';
import { AuthStateService } from './auth-state.service';
import { TokenService } from './token.service';
import { CacheService } from './cache.service';
import { AuthApiService } from './auth-api.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(
    private authState: AuthStateService,
    private tokenService: TokenService,
    private cacheService: CacheService,
    private authApi: AuthApiService
  ) {
    this.initializeAuthState();
  }

  // Computed properties delegadas al AuthStateService
  get user() { return this.authState.user; }
  get token() { return this.authState.token; }
  get authStatus() { return this.authState.authStatus; }
  get isAdmin() { return this.authState.isAdmin; }



  private initializeAuthState(): void {
    try {
      const cache = this.cacheService.getValidCache();
      if (cache && cache.token) {
        this.authState.setAuthState(cache.user ?? null, cache.token, cache.authStatus);
        return;
      }

      const storedToken = this.tokenService.getToken();
      if (storedToken && this.tokenService.isValidToken(storedToken)) {
        this.authState.setToken(storedToken);
        this.authState.setAuthStatus('checking');
        this.checkAuthStatus().subscribe();
      } else {
        this.authState.setAuthStatus('not-authenticated');
      }
    } catch (error) {
      console.error('AuthService: Error inicializando estado:', error);
      this.authState.setAuthStatus('not-authenticated');
      this.clearAllStorage();
    }
  }

  signIn(usernameOrEmail: string, password: string): Observable<boolean> {
    return this.authApi.signIn({ usernameOrEmail, password })
      .pipe(
        map((response) => this.handleAuthSuccess(response)),
        catchError((error) => this.handleAuthError(error))
      );
  }

  signUp(username: string, email: string, password: string): Observable<boolean> {
    return this.authApi.signUp({ username, email, password })
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
        catchError((error) => this.handleAuthError(error))
      );
  }

  signOut(): Observable<boolean> {
    this.authState.clearState();
    this.clearAllStorage();
    return of(false);
  }

  checkAuthStatus(): Observable<boolean> {
    const currentToken = this.authState.token();

    if (!currentToken) {
      return this.signOut();
    }

    const authResponseCache = this.cacheService.getValidCache();
    if (authResponseCache) {
      this.authState.setAuthState(authResponseCache.user ?? null, authResponseCache.token ?? null, authResponseCache.authStatus);
      return of(true);
    }

    if (this.tokenService.isValidToken(currentToken)) {
      const user = this.authState.user();
      if (user) {
        const cacheValue = this.cacheService.createCache('authenticated', user, currentToken);
        this.cacheService.saveCache(cacheValue);
        this.authState.setAuthStatus('authenticated');
        return of(true);
      }
    }

    return this.authApi.checkStatus()
      .pipe(
        map((response) => this.handleAuthSuccess(response)),
        catchError((error) => this.handleAuthError(error))
      );
  }

  // Método para refrescar el token si es necesario
  refreshToken(): Observable<boolean> {
    const currentToken = this.authState.token();
    if (!currentToken) {
      return of(false);
    }

    return this.authApi.refreshToken()
      .pipe(
        map((response) => this.handleAuthSuccess(response)),
        catchError((error) => {
          console.error("AuthService: Error refrescando token:", error);
          return this.handleAuthError(error);
        })
      );
  }

  isTokenExpiringSoon(): boolean {
    const currentToken = this.authState.token();
    if (!currentToken) return false;
    return this.tokenService.isTokenExpiringSoon(currentToken);
  }

  private clearAllStorage(): void {
    this.cacheService.clearCache();
    this.tokenService.removeToken();
  }

  private handleAuthSuccess(response: AuthResponse): boolean {
    if (!response || !response.user) {
      console.warn('AuthService: Respuesta de autenticación inválida');
      return false;
    }

    if (!response.token) {
      console.warn('AuthService: No token recibido en la respuesta');
      return false;
    }

    if (!this.tokenService.isValidToken(response.token)) {
      console.warn('AuthService: Token inválido');
      return false;
    }

    const cacheValue = this.cacheService.createCache('authenticated', response.user, response.token);
    this.cacheService.saveCache(cacheValue);
    this.tokenService.saveToken(response.token);
    this.authState.setAuthState(response.user, response.token, 'authenticated');

    return true;
  }

  private handleAuthError(error: any): Observable<boolean> {
    console.error('AuthService: Error de autenticación:', error);

    // Limpiar estado y storage
    this.authState.clearState();
    this.clearAllStorage();

    return of(false);
  }

}
