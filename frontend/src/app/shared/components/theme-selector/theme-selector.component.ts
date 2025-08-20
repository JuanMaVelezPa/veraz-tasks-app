import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ThemeService } from '@shared/services/theme.service';
import { IconComponent } from '../icon/icon.component';
import { IconType } from '../../constants/icons.constant';

@Component({
  selector: 'shared-theme-selector',
  standalone: true,
  imports: [CommonModule, IconComponent],
  templateUrl: './theme-selector.component.html',
  styles: [`
    .theme-controller {
      position: absolute;
      opacity: 0;
      pointer-events: none;
    }
  `]
})
export class ThemeSelectorComponent {
  private themeService = inject(ThemeService);

  readonly themes = this.themeService.themes;
  readonly currentTheme = this.themeService.currentTheme;

  onThemeChange(event: Event): void {
    const target = event.target as HTMLInputElement;
    if (target.checked) {
      this.themeService.setTheme(target.value);
    }
  }

  isCurrentTheme(themeValue: string): boolean {
    return this.themeService.isCurrentTheme(themeValue);
  }

  getThemeIcon(themeValue: string): IconType {
    const iconMap: Record<string, IconType> = {
      'forest': 'tree',
      'corporate': 'building',
      'garden': 'seedling',
      'lofi': 'music',
      'winter': 'snowflake'
    };
    return iconMap[themeValue] || 'palette';
  }
}
