import { inject } from '@angular/core';
import { CanMatchFn, Route, Router, UrlSegment } from '@angular/router';
import { AuthService } from '@auth/services/auth.service';
import { firstValueFrom } from 'rxjs';

export const authGuard: CanMatchFn = async (route: Route, segments: UrlSegment[]) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  try {
    const isAuthenticated = await firstValueFrom(authService.checkAuthStatus());
    console.log("authGuard - isAuthenticated", isAuthenticated);

    if (!isAuthenticated) {
      console.log("authGuard - Usuario no autenticado, redirigiendo a sign-in");
      router.navigateByUrl("/auth/sign-in");
      return false;
    }

    console.log("authGuard - Usuario autenticado, permitiendo acceso");
    return true;
  } catch (error) {
    console.error("authGuard - Error verificando autenticación:", error);
    router.navigateByUrl("/auth/sign-in");
    return false;
  }
}
