import { Injectable, signal } from '@angular/core';

export interface TablePreferences {
  sortField: string;
  sortOrder: 'asc' | 'desc';
  showLastNameFirst: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class PreferencesService {

  private readonly STORAGE_KEY = 'table_preferences';
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
      const stored = localStorage.getItem(this.STORAGE_KEY);
      if (stored) {
        const parsed = JSON.parse(stored);
        this.preferences.set(parsed);
      }
    } catch (error) {
      console.warn('Failed to load preferences from localStorage:', error);
    }
  }

  private savePreferences(): void {
    try {
      localStorage.setItem(this.STORAGE_KEY, JSON.stringify(this.preferences()));
    } catch (error) {
      console.error('Failed to save preferences to localStorage:', error);
    }
  }
}
