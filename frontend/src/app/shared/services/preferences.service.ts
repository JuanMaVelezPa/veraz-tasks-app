import { Injectable, signal, inject } from '@angular/core';
import { CacheService } from './cache.service';

export interface TablePreferences {
  sortField: string;
  sortOrder: 'asc' | 'desc';
  showLastNameFirst: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class PreferencesService {

  private readonly CACHE_KEY = 'table_preferences';
  private cacheService = inject(CacheService);

  private preferences = signal<TablePreferences>({
    sortField: 'firstName',
    sortOrder: 'asc',
    showLastNameFirst: false
  });

  constructor() {
    this.loadPreferences();
  }

  getPreferences(): TablePreferences {
    return this.preferences();
  }

  updatePreferences(preferences: Partial<TablePreferences>): void {
    this.preferences.update(current => ({
      ...current,
      ...preferences
    }));
    this.savePreferences();
  }

  toggleLastNameFirst(): boolean {
    const current = this.preferences().showLastNameFirst;
    const newValue = !current;
    this.updatePreferences({ showLastNameFirst: newValue });
    return newValue;
  }

  updateSortPreferences(sortField: string, sortOrder: 'asc' | 'desc'): void {
    this.updatePreferences({ sortField, sortOrder });
  }

  resetPreferences(): void {
    this.preferences.set({
      sortField: 'firstName',
      sortOrder: 'asc',
      showLastNameFirst: false
    });
    this.savePreferences();
  }

  private loadPreferences(): void {
    try {
      const cached = this.cacheService.get<TablePreferences>(this.CACHE_KEY);
      if (cached) {
        this.preferences.set(cached);
        return;
      }

      const stored = localStorage.getItem(this.CACHE_KEY);
      if (stored) {
        const parsed = JSON.parse(stored);
        this.preferences.set(parsed);
        this.cacheService.setPreferences(this.CACHE_KEY, parsed);
      }
    } catch (error) {
      console.warn('Failed to load preferences:', error);
    }
  }

  private savePreferences(): void {
    try {
      const prefs = this.preferences();
      this.cacheService.setPreferences(this.CACHE_KEY, prefs);
      localStorage.setItem(this.CACHE_KEY, JSON.stringify(prefs));
    } catch (error) {
      console.error('Failed to save preferences:', error);
    }
  }
}
