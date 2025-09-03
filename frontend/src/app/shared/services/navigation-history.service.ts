import { Injectable, inject } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

export interface NavigationEntry {
  url: string;
  timestamp: number;
  title?: string;
}

@Injectable({
  providedIn: 'root'
})
export class NavigationHistoryService {
  private router = inject(Router);
  private navigationHistory: NavigationEntry[] = [];
  private maxHistorySize = 50;

  constructor() {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.addToHistory(event.urlAfterRedirects);
      });
  }

  getPreviousUrl(): string | null {
    if (this.navigationHistory.length < 2) {
      return null;
    }
    return this.navigationHistory[this.navigationHistory.length - 2].url;
  }

  getCurrentUrl(): string | null {
    if (this.navigationHistory.length === 0) {
      return null;
    }
    return this.navigationHistory[this.navigationHistory.length - 1].url;
  }

  goBack(fallbackUrl: string = '/admin'): void {
    const previousUrl = this.getPreviousUrl();
    if (previousUrl) {
      this.router.navigateByUrl(previousUrl);
    } else {
      this.router.navigateByUrl(fallbackUrl);
    }
  }

  goBackToUser(userId: string): void {
    if (this.isValidUserId(userId)) {
      this.router.navigate(['/admin/users', userId]);
    } else {
      this.router.navigate(['/admin/users']);
    }
  }

  goBackToPersons(): void {
    this.router.navigate(['/admin/persons']);
  }

  goBackToUsers(): void {
    this.router.navigate(['/admin/users']);
  }

  navigateTo(url: string, title?: string): void {
    this.router.navigateByUrl(url);
    this.addToHistory(url, title);
  }

  clearHistory(): void {
    this.navigationHistory = [];
  }

  getHistory(): NavigationEntry[] {
    return [...this.navigationHistory];
  }

  hasPreviousUrl(): boolean {
    return this.navigationHistory.length > 1;
  }

  getHistorySize(): number {
    return this.navigationHistory.length;
  }

  private addToHistory(url: string, title?: string): void {
    if (this.isDuplicateUrl(url)) {
      return;
    }

    const entry: NavigationEntry = {
      url,
      timestamp: Date.now(),
      title
    };

    this.navigationHistory.push(entry);
    this.trimHistoryIfNeeded();
  }

  private isDuplicateUrl(url: string): boolean {
    return this.navigationHistory.length > 0 &&
           this.navigationHistory[this.navigationHistory.length - 1].url === url;
  }

  private trimHistoryIfNeeded(): void {
    if (this.navigationHistory.length > this.maxHistorySize) {
      this.navigationHistory.shift();
    }
  }

  private isValidUserId(userId: string): boolean {
    return Boolean(userId && userId !== 'new');
  }
}
