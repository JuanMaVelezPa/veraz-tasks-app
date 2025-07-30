import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LayoutService {
  private readonly STORAGE_KEY = 'sidebar-open-state';

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
      const stored = localStorage.getItem(this.STORAGE_KEY);
      return stored ? JSON.parse(stored) : false;
    } catch (error) {
      return false;
    }
  }

  private saveToStorage(state: boolean): void {
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(state));
  }
}
