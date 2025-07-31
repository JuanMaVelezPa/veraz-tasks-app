import { inject, Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AuthResponse } from '@auth/interfaces/auth-response.interface';
import { AuthStateService } from './auth-state.service';
import { TokenService } from './token.service';
import { CacheService } from './cache.service';
import { AuthApiService } from './auth-api.service';
import { SignInResponse } from '@auth/interfaces/sign-in.interface';

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
        map((response) => this.handleSignInSuccess(response)),
        catchError((error) => this.handleSignInError(error))
      );
  }

  signUp(username: string, email: string, password: string): Observable<SignInResponse> {
    return this.authApi.signUp({ username, email, password })
      .pipe(
        map((response) => this.handleSignInSuccess(response)),
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
        map((response) => this.handleCheckStatusSuccess(response)),
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
        map((response) => this.handleRefreshTokenSuccess(response)),
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

  private handleSignInSuccess(response: AuthResponse): SignInResponse {

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

    const errorMessage = error?.message || 'Unexpected error authenticating. Please try again.';

    return of({
      authStatus: 'not-authenticated',
      message: errorMessage
    });
  }

  private handleCheckStatusSuccess(response: AuthResponse): boolean {
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

  private handleRefreshTokenSuccess(response: AuthResponse): boolean {
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
