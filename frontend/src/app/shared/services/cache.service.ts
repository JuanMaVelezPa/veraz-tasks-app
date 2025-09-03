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

  get<T>(key: string): T | null {
    const entry = this.cache.get(key);
    if (!entry) return null;

    if (this.isEntryExpired(entry)) {
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
    this.deleteEntriesByType('data');
  }

  clearPreferencesCache(): void {
    this.deleteEntriesByType('preferences');
  }

  clearPattern(pattern: string): void {
    this.deleteEntriesByPattern(pattern, 'data');
  }

  has(key: string): boolean {
    return this.get(key) !== null;
  }

  size(): number {
    return this.cache.size;
  }

  getStats(): { data: number; preferences: number; total: number } {
    const dataCount = this.countEntriesByType('data');
    const preferencesCount = this.countEntriesByType('preferences');

    return {
      data: dataCount,
      preferences: preferencesCount,
      total: this.cache.size
    };
  }

  cleanup(): void {
    this.deleteExpiredEntries('data');
  }

  cleanupAll(): void {
    this.deleteExpiredEntries();
  }

  clearOnLogout(): void {
    this.clearDataCache();
  }

  forceCleanup(): void {
    this.cleanup();
  }

  forceCleanupAll(): void {
    this.cleanupAll();
  }

  getKeysMatching(pattern: string): string[] {
    return Array.from(this.cache.keys()).filter(key => key.includes(pattern));
  }

  getDetailedStats(): {
    data: number;
    preferences: number;
    total: number;
    expired: number;
    patterns: Record<string, number>;
  } {
    const now = Date.now();
    let dataCount = 0;
    let preferencesCount = 0;
    let expiredCount = 0;
    const patterns: Record<string, number> = {};

    for (const [key, entry] of this.cache.entries()) {
      if (entry.type === 'data') {
        dataCount++;
        if (this.isEntryExpired(entry, now)) {
          expiredCount++;
        }
      } else {
        preferencesCount++;
      }

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

  isHealthy(): boolean {
    const stats = this.getDetailedStats();
    const expiredPercentage = stats.total > 0 ? (stats.expired / stats.total) * 100 : 0;
    return expiredPercentage < 50;
  }

  private startAutoCleanup(): void {
    this.cleanupSubscription = interval(5 * 60 * 1000).subscribe(() => {
      this.cleanup();
    });
  }

  private isEntryExpired(entry: CacheEntry<any>, currentTime: number = Date.now()): boolean {
    return entry.type === 'data' && currentTime - entry.timestamp > entry.ttl;
  }

  private deleteEntriesByType(type: 'data' | 'preferences'): void {
    const keysToDelete = this.getKeysByType(type);
    keysToDelete.forEach(key => this.cache.delete(key));
  }

  private deleteEntriesByPattern(pattern: string, type: 'data' | 'preferences'): void {
    const keysToDelete = this.getKeysByPatternAndType(pattern, type);
    keysToDelete.forEach(key => this.cache.delete(key));
  }

  private deleteExpiredEntries(type?: 'data' | 'preferences'): void {
    const now = Date.now();
    const keysToDelete = Array.from(this.cache.entries())
      .filter(([_, entry]) => {
        const isExpired = this.isEntryExpired(entry, now);
        return type ? isExpired && entry.type === type : isExpired;
      })
      .map(([key]) => key);

    keysToDelete.forEach(key => this.cache.delete(key));
  }

  private countEntriesByType(type: 'data' | 'preferences'): number {
    return this.getKeysByType(type).length;
  }

  private getKeysByType(type: 'data' | 'preferences'): string[] {
    return Array.from(this.cache.entries())
      .filter(([_, entry]) => entry.type === type)
      .map(([key]) => key);
  }

  private getKeysByPatternAndType(pattern: string, type: 'data' | 'preferences'): string[] {
    return Array.from(this.cache.entries())
      .filter(([key, entry]) => key.includes(pattern) && entry.type === type)
      .map(([key]) => key);
  }
}
