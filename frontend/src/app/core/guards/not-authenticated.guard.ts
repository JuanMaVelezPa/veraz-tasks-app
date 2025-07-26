import { inject } from '@angular/core';
import { CanMatchFn, Route, Router, UrlSegment } from '@angular/router';
import { AuthService } from '@auth/services/auth.service';
import { firstValueFrom } from 'rxjs';

export const notAuthenticatedGuard: CanMatchFn = async (route: Route, segments: UrlSegment[]) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  try {
    const isAuthenticated = await firstValueFrom(authService.checkAuthStatus());
    console.log("notAuthenticatedGuard - isAuthenticated", isAuthenticated);

    if (isAuthenticated) {
      console.log("notAuthenticatedGuard - Usuario ya autenticado, redirigiendo a página principal");
      router.navigateByUrl("/");
      return false;
    }

    console.log("notAuthenticatedGuard - Usuario no autenticado, permitiendo acceso a auth");
    return true;
  } catch (error) {
    console.error("notAuthenticatedGuard - Error verificando autenticación:", error);
    // Si hay error, permitimos acceso a auth para que pueda intentar autenticarse
    return true;
  }
}
