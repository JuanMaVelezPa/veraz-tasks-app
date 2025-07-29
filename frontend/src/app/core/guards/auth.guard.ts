import { inject } from '@angular/core';
import { CanMatchFn, Route, Router, UrlSegment } from '@angular/router';
import { AuthService } from '@auth/services/auth.service';
import { firstValueFrom } from 'rxjs';

export const authGuard: CanMatchFn = async (route: Route, segments: UrlSegment[]) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  try {
    const isAuthenticated = await firstValueFrom(authService.checkAuthStatus());

    if (!isAuthenticated) {
      router.navigateByUrl("/auth/sign-in");
      return false;
    }

    return true;
  } catch (error) {
    router.navigateByUrl("/auth/sign-in");
    return false;
  }
}
