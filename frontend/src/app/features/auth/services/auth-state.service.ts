import { computed, Injectable, signal } from '@angular/core';
import { AuthStatus } from '@auth/interfaces/auth-response';
import { User } from '@auth/interfaces/user';

@Injectable({
  providedIn: 'root'
})
export class AuthStateService {
  private _user = signal<User | null>(null);
  private _token = signal<string | null>(null);
  private _authStatus = signal<AuthStatus>('checking');

  user = computed(() => this._user());
  token = computed(() => this._token());
  authStatus = computed(() => this._authStatus());

  isAdmin = computed(() => {
    const user = this._user();
    if (!user || !user.roles) return false;

    for (const role of user.roles) {
      if (role === 'ADMIN' || role === 'MANAGER') {
        return true;
      }
    }
    return false;
  });

  setUser(user: User | null): void {
    this._user.set(user);
  }

  setToken(token: string | null): void {
    this._token.set(token);
  }

  setAuthStatus(status: AuthStatus): void {
    this._authStatus.set(status);
  }

  clearState(): void {
    this._user.set(null);
    this._token.set(null);
    this._authStatus.set('not-authenticated');
  }

  setAuthState(user: User | null, token: string | null, status: AuthStatus): void {
    this._user.set(user);
    this._token.set(token);
    this._authStatus.set(status);
  }

  isAuthenticated(): boolean {
    return this._authStatus() === 'authenticated' &&
           this._token() !== null &&
           this._user() !== null;
  }
}
