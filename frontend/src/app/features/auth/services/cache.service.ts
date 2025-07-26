import { Injectable } from '@angular/core';
import { CacheAuthResponse } from '@auth/interfaces/auth-response';

@Injectable({
  providedIn: 'root'
})
export class CacheService {
  private readonly CACHE_KEY = 'authResponseCache';
  private readonly CACHE_DURATION = 24 * 60 * 60 * 1000; // 24 horas

  saveCache(data: CacheAuthResponse): void {
    try {
      const cacheData = {
        ...data,
        lastAuthCheck: Date.now()
      };
      localStorage.setItem(this.CACHE_KEY, JSON.stringify(cacheData));
    } catch (error) {
      console.error('CacheService: Error guardando cache:', error);
    }
  }

  getCache(): CacheAuthResponse | null {
    try {
      const cacheData = localStorage.getItem(this.CACHE_KEY);
      if (!cacheData) return null;

      const parsed = JSON.parse(cacheData);
      return parsed;
    } catch (error) {
      console.error('CacheService: Error leyendo cache:', error);
      this.clearCache();
      return null;
    }
  }

  isCacheValid(cache: CacheAuthResponse): boolean {
    if (!cache || !cache.lastAuthCheck) return false;

    const now = new Date().getTime();
    return now < cache.lastAuthCheck + this.CACHE_DURATION;
  }

  clearCache(): void {
    try {
      localStorage.removeItem(this.CACHE_KEY);
    } catch (error) {
      console.error('CacheService: Error limpiando cache:', error);
    }
  }

  getValidCache(): CacheAuthResponse | null {
    const cache = this.getCache();
    if (cache && this.isCacheValid(cache)) {
      return cache;
    }
    return null;
  }

  createCache(
    authStatus: 'authenticated' | 'not-authenticated',
    user: any,
    token: string
  ): CacheAuthResponse {
    return {
      authStatus,
      user,
      token,
      lastAuthCheck: Date.now()
    };
  }

  hasCache(): boolean {
    return localStorage.getItem(this.CACHE_KEY) !== null;
  }

  getCacheInfo(): {
    exists: boolean;
    isValid: boolean;
    timeRemaining: number;
  } | null {
    const cache = this.getCache();
    if (!cache) {
      return { exists: false, isValid: false, timeRemaining: 0 };
    }

    const isValid = this.isCacheValid(cache);
    const now = new Date().getTime();
    const timeRemaining = Math.max(0, cache.lastAuthCheck + this.CACHE_DURATION - now);

    return {
      exists: true,
      isValid,
      timeRemaining: Math.round(timeRemaining / 1000)
    };
  }
}
