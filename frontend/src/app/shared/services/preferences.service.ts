import { Injectable, signal, inject } from '@angular/core';
import { CacheService } from './cache.service';

export interface TablePreferences {
  sortField: string;
  sortOrder: 'asc' | 'desc';
  showLastNameFirst: boolean;
  personFilter?: string;
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
    showLastNameFirst: false,
    personFilter: 'all'
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

  updatePersonFilterPreferences(personFilter: string): void {
    this.updatePreferences({ personFilter });
  }

  resetPreferences(): void {
    this.preferences.set(this.getDefaultPreferences());
    this.savePreferences();
  }

  private getDefaultPreferences(): TablePreferences {
    return {
      sortField: 'firstName',
      sortOrder: 'asc',
      showLastNameFirst: false,
      personFilter: 'all'
    };
  }

  private loadPreferences(): void {
    try {
      const cached = this.cacheService.get<TablePreferences>(this.CACHE_KEY);
      if (cached) {
        this.preferences.set(cached);
        return;
      }

      const stored = this.loadFromLocalStorage();
      if (stored) {
        this.preferences.set(stored);
        this.cacheService.setPreferences(this.CACHE_KEY, stored);
      }
    } catch (error) {
      console.warn('Failed to load preferences:', error);
    }
  }

  private loadFromLocalStorage(): TablePreferences | null {
    const stored = localStorage.getItem(this.CACHE_KEY);
    if (stored) {
      return JSON.parse(stored);
    }
    return null;
  }

  private savePreferences(): void {
    try {
      const prefs = this.preferences();
      this.cacheService.setPreferences(this.CACHE_KEY, prefs);
      this.saveToLocalStorage(prefs);
    } catch (error) {
      console.error('Failed to save preferences:', error);
    }
  }

  private saveToLocalStorage(preferences: TablePreferences): void {
    localStorage.setItem(this.CACHE_KEY, JSON.stringify(preferences));
  }
}
