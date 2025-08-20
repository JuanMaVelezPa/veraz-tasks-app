import { Injectable } from '@angular/core';

export interface TokenPayload {
  exp: number;
  iat: number;
  sub: string;
  username: string;
  roles?: string[];
  [key: string]: any;
}

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private readonly TOKEN_KEY = 'authToken';
  private readonly REFRESH_THRESHOLD = 7200; // 2 hours

  isValidToken(token: string): boolean {
    if (!token || token.length === 0) return false;

    const parts = token.split('.');
    if (parts.length !== 3) return false;

    try {
      const payload = this.decodeToken(token);
      if (!payload || !payload.exp) return false;

      const now = Math.floor(Date.now() / 1000);
      if (now >= payload.exp) return false;

      return true;
    } catch (error) {
      console.error('TokenService: Error validando token:', error);
      return false;
    }
  }

  isTokenExpiringSoon(token: string): boolean {
    if (!token) return false;

    try {
      const payload = this.decodeToken(token);
      if (!payload || !payload.exp) return false;

      const now = Math.floor(Date.now() / 1000);
      const timeUntilExpiry = payload.exp - now;

      return timeUntilExpiry < this.REFRESH_THRESHOLD;
    } catch (error) {
      console.error('TokenService: Error verificando expiración del token:', error);
      return false;
    }
  }

  decodeToken(token: string): TokenPayload | null {
    try {
      const parts = token.split('.');
      if (parts.length !== 3) return null;

      const payload = JSON.parse(atob(parts[1]));
      return payload;
    } catch (error) {
      console.error('TokenService: Error decodificando token:', error);
      return null;
    }
  }

  getTokenInfo(token: string): {
    isValid: boolean;
    expiresIn: number;
    roles: string[];
    username: string;
  } | null {
    if (!this.isValidToken(token)) return null;

    try {
      const payload = this.decodeToken(token);
      if (!payload) return null;

      const now = Math.floor(Date.now() / 1000);
      const expiresIn = payload.exp - now;

      return {
        isValid: true,
        expiresIn,
        roles: payload.roles || [],
        username: payload.username || ''
      };
    } catch (error) {
      console.error('TokenService: Error obteniendo información del token:', error);
      return null;
    }
  }

  saveToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  removeToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  hasValidStoredToken(): boolean {
    const token = this.getToken();
    return token ? this.isValidToken(token) : false;
  }
}
