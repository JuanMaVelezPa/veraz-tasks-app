import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AuthResponse } from '@auth/interfaces/auth-response.interface';
import { AuthStateService } from './auth-state.service';
import { TokenService } from './token.service';
import { CacheService } from './cache.service';
import { AuthApiService } from './auth-api.service';
import { SignInResponse } from '@auth/interfaces/sign-in.interface';
import { ApiResponse } from '@shared/interfaces/api-response.interface';
import { User } from '@users/interfaces/user.interface';

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

  get user() { return this.authState.user; }
  get token() { return this.authState.token; }
  get authStatus() { return this.authState.authStatus; }
  get isAdmin() { return this.authState.isAdmin; }

  updateCurrentUser(updatedUser: User): void {
    this.authState.setUser(updatedUser);

    // Update cache if authenticated
    const currentToken = this.authState.token();
    const currentStatus = this.authState.authStatus();
    if (currentToken && currentStatus === 'authenticated') {
      const cacheValue = this.cacheService.createCache(currentStatus, updatedUser, currentToken);
      this.cacheService.saveCache(cacheValue);
    }
  }

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
      this.authState.setAuthStatus('not-authenticated');
      this.clearAllStorage();
    }
  }

  signIn(usernameOrEmail: string, password: string): Observable<SignInResponse> {
    return this.authApi.signIn({ usernameOrEmail, password })
      .pipe(
        map((apiResponse) => this.handleSignInSuccess(apiResponse)),
        catchError((error) => this.handleSignInError(error))
      );
  }

  signUp(username: string, email: string, password: string): Observable<SignInResponse> {
    return this.authApi.signUp({ username, email, password })
      .pipe(
        map((apiResponse) => this.handleSignInSuccess(apiResponse)),
        catchError((error) => this.handleSignInError(error))
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
      return of(false);
    }

    const cacheValue = this.cacheService.getCache();
    if (cacheValue && cacheValue.authStatus === 'authenticated' && cacheValue.user && cacheValue.token) {
      this.authState.setAuthStatus('authenticated');
      return of(true);
    }

    return this.authApi.checkAuthStatus()
      .pipe(
        map((apiResponse) => this.handleCheckStatusSuccess(apiResponse)),
        catchError((error) => this.handleCheckStatusError(error))
      );
  }

  refreshToken(): Observable<boolean> {
    const currentToken = this.authState.token();
    if (!currentToken) {
      return of(false);
    }

    return this.authApi.refreshToken()
      .pipe(
        map((apiResponse) => this.handleRefreshTokenSuccess(apiResponse)),
        catchError((error) => this.handleRefreshTokenError(error))
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

  private handleSignInSuccess(apiResponse: ApiResponse<AuthResponse>): SignInResponse {
    // Verificar si la respuesta de la API fue exitosa
    if (!apiResponse.success || !apiResponse.data) {
      return {
        authStatus: 'not-authenticated',
        message: apiResponse.message || 'Error en la autenticación'
      };
    }

    const response = apiResponse.data;

    if (!response || !response.token || !response.user || !this.tokenService.isValidToken(response.token)) {
      return {
        authStatus: 'not-authenticated',
        message: 'Invalid credentials. Please verify your username and password.'
      };
    }

    const cacheValue = this.cacheService.createCache('authenticated', response.user, response.token);
    this.cacheService.saveCache(cacheValue);
    this.tokenService.saveToken(response.token);
    this.authState.setAuthState(response.user, response.token, 'authenticated');

    return {
      authStatus: 'authenticated',
      message: 'User authenticated successfully'
    };
  }

  private handleSignInError(error: any): Observable<SignInResponse> {
    this.authState.clearState();
    this.clearAllStorage();

    // Extract error message from ErrorResponse structure if available
    let errorMessage = 'Unexpected error authenticating. Please try again.';

    if (error?.errorResponse?.message) {
      errorMessage = error.errorResponse.message;
    } else if (error?.message) {
      errorMessage = error.message;
    }

    return of({
      authStatus: 'not-authenticated',
      message: errorMessage
    });
  }

  private handleCheckStatusSuccess(apiResponse: ApiResponse<AuthResponse>): boolean {
    // Verificar si la respuesta de la API fue exitosa
    if (!apiResponse.success || !apiResponse.data) {
      this.authState.clearState();
      this.clearAllStorage();
      return false;
    }

    const response = apiResponse.data;

    if (!response || !response.token || !response.user || !this.tokenService.isValidToken(response.token)) {
      this.authState.clearState();
      this.clearAllStorage();
      return false;
    }

    const cacheValue = this.cacheService.createCache('authenticated', response.user, response.token);
    this.cacheService.saveCache(cacheValue);
    this.tokenService.saveToken(response.token);
    this.authState.setAuthState(response.user, response.token, 'authenticated');

    return true;
  }

  private handleCheckStatusError(error: any): Observable<boolean> {
    this.authState.clearState();
    this.clearAllStorage();
    return of(false);
  }

  private handleRefreshTokenSuccess(apiResponse: ApiResponse<AuthResponse>): boolean {
    // Verificar si la respuesta de la API fue exitosa
    if (!apiResponse.success || !apiResponse.data) {
      this.authState.clearState();
      this.clearAllStorage();
      return false;
    }

    const response = apiResponse.data;

    if (!response || !response.token || !response.user || !this.tokenService.isValidToken(response.token)) {
      this.authState.clearState();
      this.clearAllStorage();
      return false;
    }

    const cacheValue = this.cacheService.createCache('authenticated', response.user, response.token);
    this.cacheService.saveCache(cacheValue);
    this.tokenService.saveToken(response.token);
    this.authState.setAuthState(response.user, response.token, 'authenticated');

    return true;
  }

  private handleRefreshTokenError(error: any): Observable<boolean> {
    this.authState.clearState();
    this.clearAllStorage();
    return of(false);
  }
}
