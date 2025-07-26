import { inject } from '@angular/core';
import { Router, type CanMatchFn, type Route, type UrlSegment } from '@angular/router';
import { AuthService } from '@auth/services/auth.service';
import { firstValueFrom } from 'rxjs';

export const isAdminGuard: CanMatchFn = async (route: Route, segments: UrlSegment[]) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Primero verificar si está autenticado
  const isAuthenticated = await firstValueFrom(authService.checkAuthStatus());
  console.log("isAdminGuard - isAuthenticated", isAuthenticated);

  if (!isAuthenticated) {
    router.navigateByUrl("/auth/sign-in");
    return false;
  }

  // Luego verificar si es admin
  if (!authService.isAdmin()) {
    router.navigateByUrl("/");
    return false;
  }

  return true;
};
