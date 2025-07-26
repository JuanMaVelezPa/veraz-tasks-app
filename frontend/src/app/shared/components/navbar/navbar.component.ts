import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { LayoutService } from '@shared/services/layout.service';
import { AuthService } from '@auth/services/auth.service';
import { AppInitService } from '@core/services/app-init.service';

@Component({
  selector: 'app-navbar',
  imports: [],
  templateUrl: './navbar.component.html',
})
export class NavbarComponent {

  layoutService = inject(LayoutService);
  authService = inject(AuthService);
  router = inject(Router);
  appInitService = inject(AppInitService);

  // Getters para mostrar información del usuario
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
    return user.roles[0]; // Mostrar el primer rol
  }

  onSignOut() {
    console.log('Cerrando sesión...');
    this.authService.signOut().subscribe(() => {
      console.log('Sesión cerrada exitosamente, deteniendo TokenRefreshService');
      this.appInitService.stopTokenRefresh();
      this.router.navigateByUrl('/auth/sign-in');
    });
  }
}
