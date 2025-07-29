import { inject } from '@angular/core';
import { Route, Router, UrlSegment, type CanMatchFn } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AuthService } from '@auth/services/auth.service';

export const isAdminGuard: CanMatchFn = async (route: Route, segments: UrlSegment[]) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const isAuthenticated = await firstValueFrom(authService.checkAuthStatus());

  if (!isAuthenticated) {
    router.navigateByUrl("/auth/sign-in");
    return false;
  }

  if (!authService.isAdmin()) {
    router.navigateByUrl("/");
    return false;
  }

  return true;
};
