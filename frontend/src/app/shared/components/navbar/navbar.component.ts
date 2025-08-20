import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AppInitService } from '@core/services/app-init.service';
import { AuthService } from '@auth/services/auth.service';
import { LayoutService } from '@shared/services/layout.service';
import { ThemeSelectorComponent } from '@shared/components/theme-selector/theme-selector.component';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'shared-navbar',
  imports: [ThemeSelectorComponent, RouterLink, IconComponent],
  templateUrl: './navbar.component.html',
})
export class NavbarComponent {
  layoutService = inject(LayoutService);
  authService = inject(AuthService);
  router = inject(Router);
  appInitService = inject(AppInitService);

  // Getters to display user information
  get currentUser() {
    return this.authService.user();
  }

  get isAuthenticated() {
    return this.authService.authStatus() === 'authenticated';
  }

  get userRole() {
    const user = this.currentUser;
    if (!user || !user.roles || user.roles.length === 0) {
      return 'Usuario';
    }
    return user.roles[0];
  }

  onSignOut() {
    this.authService.signOut().subscribe(() => {
      this.appInitService.stopTokenRefresh();
      this.router.navigateByUrl('/auth/sign-in');
    });
  }
}
