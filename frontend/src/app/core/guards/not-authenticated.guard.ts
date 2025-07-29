import { inject } from '@angular/core';
import { Route, Router, UrlSegment, type CanMatchFn } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AuthService } from '@auth/services/auth.service';

export const notAuthenticatedGuard: CanMatchFn = async (route: Route, segments: UrlSegment[]) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  try {
    const isAuthenticated = await firstValueFrom(authService.checkAuthStatus());

    if (isAuthenticated) {
      router.navigateByUrl('/');
      return false;
    }

    return true;
  } catch (error) {
    return true;
  }
};
