import { Injectable, signal, computed } from '@angular/core';
import { ThemeOption } from '@shared/interfaces/theme.interface';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private readonly THEME_KEY = 'theme';
  private readonly VALID_THEMES = ['forest', 'corporate', 'garden', 'lofi', 'winter'];
  private readonly DEFAULT_THEME = 'corporate';

  private _currentTheme = signal<string>(this.getInitialTheme());

  readonly currentTheme = this._currentTheme.asReadonly();

  readonly themes: ThemeOption[] = [
    {
      value: 'forest',
      label: 'Forest',
      description: 'Tema natural y verde',
      gradient: 'bg-gradient-to-br from-green-600 to-green-800',
      icon: 'M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z'
    },
    {
      value: 'corporate',
      label: 'Corporate',
      description: 'Tema profesional',
      gradient: 'bg-gradient-to-br from-blue-500 to-blue-700',
      icon: 'M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z'
    },
    {
      value: 'garden',
      label: 'Garden',
      description: 'Tema floral y fresco',
      gradient: 'bg-gradient-to-br from-emerald-400 to-teal-500',
      icon: 'M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z'
    },
    {
      value: 'lofi',
      label: 'Lo-Fi',
      description: 'Tema minimalista y relajante',
      gradient: 'bg-gradient-to-br from-purple-400 to-pink-400',
      icon: 'M11.3 1.046A1 1 0 0112 2v5h4a1 1 0 01.82 1.573l-7 10A1 1 0 018 18v-5H4a1 1 0 01-.82-1.573l7-10a1 1 0 011.12-.38z'
    },
    {
      value: 'winter',
      label: 'Winter',
      description: 'Tema frío y elegante',
      gradient: 'bg-gradient-to-br from-cyan-300 to-blue-400',
      icon: 'M7 21a4 4 0 01-4-4V5a2 2 0 012-2h4a2 2 0 012 2v12a4 4 0 01-4 4zM21 5a2 2 0 00-2-2h-4a2 2 0 00-2 2v12a4 4 0 004 4h4a2 2 0 002-2V5z'
    }
  ];

  constructor() {
    this.initializeTheme();
  }

  private getInitialTheme(): string {
    try {
      const savedTheme = localStorage.getItem(this.THEME_KEY);
      if (savedTheme && this.VALID_THEMES.includes(savedTheme)) {
        return savedTheme;
      }
      return this.DEFAULT_THEME;
    } catch (error) {
      return this.DEFAULT_THEME;
    }
  }

  private initializeTheme(): void {
    const theme = this._currentTheme();
    this.applyTheme(theme);
  }

  setTheme(theme: string): void {
    if (!this.VALID_THEMES.includes(theme)) { return; }
    this._currentTheme.set(theme);
    this.applyTheme(theme);
    this.saveTheme(theme);
  }

  private applyTheme(theme: string): void {
    document.documentElement.setAttribute('data-theme', theme);
    document.body.setAttribute('data-theme', theme);
  }

  private saveTheme(theme: string): void {
    localStorage.setItem(this.THEME_KEY, theme);
  }

  getThemeByValue(value: string): ThemeOption | undefined {
    return this.themes.find(theme => theme.value === value);
  }

  isCurrentTheme(themeValue: string): boolean {
    return this._currentTheme() === themeValue;
  }

  getCurrentTheme(): string {
    return this._currentTheme();
  }

  getValidThemes(): string[] {
    return [...this.VALID_THEMES];
  }
}
