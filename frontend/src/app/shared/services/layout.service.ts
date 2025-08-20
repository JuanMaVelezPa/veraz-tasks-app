import { Injectable, signal, inject } from '@angular/core';
import { CacheService } from './cache.service';

@Injectable({
  providedIn: 'root'
})
export class LayoutService {
  private readonly SIDEBAR_KEY = 'sidebar-open-state';
  private cacheService = inject(CacheService);

  private _isSidebarOpen = signal<boolean>(this.getInitialState());

  readonly isSidebarOpen = this._isSidebarOpen.asReadonly();

  toggleSidebar() {
    this._isSidebarOpen.update(current => {
      const newState = !current;
      this.saveToStorage(newState);
      return newState;
    });
  }

  private getInitialState(): boolean {
    try {
      const cached = this.cacheService.get<boolean>(this.SIDEBAR_KEY);
      if (cached !== null) {
        return cached;
      }

      const stored = localStorage.getItem(this.SIDEBAR_KEY);
      if (stored) {
        const parsed = JSON.parse(stored);
        this.cacheService.setPreferences(this.SIDEBAR_KEY, parsed);
        return parsed;
      }
      return false;
    } catch (error) {
      return false;
    }
  }

  private saveToStorage(state: boolean): void {
    this.cacheService.setPreferences(this.SIDEBAR_KEY, state);
    localStorage.setItem(this.SIDEBAR_KEY, JSON.stringify(state));
  }
}
