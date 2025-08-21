import { Injectable, OnDestroy } from '@angular/core';
import { interval, Subscription } from 'rxjs';

export interface CacheEntry<T> {
  data: T;
  timestamp: number;
  ttl: number;
  type: 'data' | 'preferences';
}

@Injectable({
  providedIn: 'root'
})
export class CacheService implements OnDestroy {
  private cache = new Map<string, CacheEntry<any>>();
  private readonly DEFAULT_TTL = 5 * 60 * 1000; // 5 minutes
  private readonly PREFERENCES_TTL = Infinity; // Never expire
  private cleanupSubscription?: Subscription;

  constructor() {
    this.startAutoCleanup();
  }

  ngOnDestroy() {
    this.cleanupSubscription?.unsubscribe();
  }

  private startAutoCleanup(): void {
    this.cleanupSubscription = interval(5 * 60 * 1000).subscribe(() => {
      this.cleanup();
    });
  }

  get<T>(key: string): T | null {
    const entry = this.cache.get(key);
    if (!entry) return null;

    if (entry.type === 'data' && Date.now() - entry.timestamp > entry.ttl) {
      this.cache.delete(key);
      return null;
    }

    return entry.data;
  }

  set<T>(key: string, data: T, ttl?: number, type: 'data' | 'preferences' = 'data'): void {
    const defaultTtl = type === 'preferences' ? this.PREFERENCES_TTL : this.DEFAULT_TTL;
    this.cache.set(key, {
      data,
      timestamp: Date.now(),
      ttl: ttl || defaultTtl,
      type
    });
  }

  setPreferences<T>(key: string, data: T): void {
    this.set(key, data, this.PREFERENCES_TTL, 'preferences');
  }

  delete(key: string): void {
    this.cache.delete(key);
  }

  clear(): void {
    this.cache.clear();
  }

  clearDataCache(): void {
    const keysToDelete = Array.from(this.cache.entries())
      .filter(([_, entry]) => entry.type === 'data')
      .map(([key]) => key);

    keysToDelete.forEach(key => this.cache.delete(key));
  }

  clearPreferencesCache(): void {
    const keysToDelete = Array.from(this.cache.entries())
      .filter(([_, entry]) => entry.type === 'preferences')
      .map(([key]) => key);

    keysToDelete.forEach(key => this.cache.delete(key));
  }

  clearPattern(pattern: string): void {
    const keysToDelete = Array.from(this.cache.entries())
      .filter(([key, entry]) => key.includes(pattern) && entry.type === 'data')
      .map(([key]) => key);

    keysToDelete.forEach(key => this.cache.delete(key));
  }

  has(key: string): boolean {
    return this.get(key) !== null;
  }

  size(): number {
    return this.cache.size;
  }

  getStats(): { data: number; preferences: number; total: number } {
    let dataCount = 0;
    let preferencesCount = 0;

    for (const entry of this.cache.values()) {
      if (entry.type === 'data') {
        dataCount++;
      } else {
        preferencesCount++;
      }
    }

    return {
      data: dataCount,
      preferences: preferencesCount,
      total: this.cache.size
    };
  }

  cleanup(): void {
    const now = Date.now();
    const keysToDelete = Array.from(this.cache.entries())
      .filter(([_, entry]) => entry.type === 'data' && now - entry.timestamp > entry.ttl)
      .map(([key]) => key);

    keysToDelete.forEach(key => this.cache.delete(key));
  }

  cleanupAll(): void {
    const now = Date.now();
    const keysToDelete = Array.from(this.cache.entries())
      .filter(([_, entry]) => now - entry.timestamp > entry.ttl)
      .map(([key]) => key);

    keysToDelete.forEach(key => this.cache.delete(key));
  }

  clearOnLogout(): void {
    this.clearDataCache();
  }

  // Utility methods for debugging (development only)
  forceCleanup(): void {
    this.cleanup();
  }

  forceCleanupAll(): void {
    this.cleanupAll();
  }

  /**
   * Get cache keys matching a pattern
   */
  getKeysMatching(pattern: string): string[] {
    return Array.from(this.cache.keys()).filter(key => key.includes(pattern));
  }

  /**
   * Get detailed cache statistics
   */
  getDetailedStats(): {
    data: number;
    preferences: number;
    total: number;
    expired: number;
    patterns: Record<string, number>;
  } {
    let dataCount = 0;
    let preferencesCount = 0;
    let expiredCount = 0;
    const patterns: Record<string, number> = {};
    const now = Date.now();

    for (const [key, entry] of this.cache.entries()) {
      // Count by type
      if (entry.type === 'data') {
        dataCount++;

        // Check if expired
        if (now - entry.timestamp > entry.ttl) {
          expiredCount++;
        }
      } else {
        preferencesCount++;
      }

      // Count by pattern (prefix before first colon)
      const pattern = key.split(':')[0];
      patterns[pattern] = (patterns[pattern] || 0) + 1;
    }

    return {
      data: dataCount,
      preferences: preferencesCount,
      total: this.cache.size,
      expired: expiredCount,
      patterns
    };
  }

  /**
   * Check if cache is healthy (not too many expired entries)
   */
  isHealthy(): boolean {
    const stats = this.getDetailedStats();
    const expiredPercentage = stats.total > 0 ? (stats.expired / stats.total) * 100 : 0;
    return expiredPercentage < 50; // Consider unhealthy if more than 50% are expired
  }
}
