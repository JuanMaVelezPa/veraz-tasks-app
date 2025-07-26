import { Injectable, inject } from '@angular/core';
import { AuthService } from '@auth/services/auth.service';
import { TokenRefreshService } from '@auth/services/token-refresh.service';

@Injectable({
  providedIn: 'root'
})
export class AppInitService {
  private authService = inject(AuthService);
  private tokenRefreshService = inject(TokenRefreshService);

  initializeApp(): void {
    console.log('AppInitService: Inicializando aplicación...');

    // Iniciar el servicio de refresco de token si el usuario está autenticado
    if (this.authService.authStatus() === 'authenticated') {
      console.log('AppInitService: Usuario autenticado, iniciando TokenRefreshService');
      this.tokenRefreshService.startTokenRefresh();
    }
  }

  startTokenRefresh(): void {
    console.log('AppInitService: Iniciando TokenRefreshService');
    this.tokenRefreshService.startTokenRefresh();
  }

  stopTokenRefresh(): void {
    console.log('AppInitService: Deteniendo TokenRefreshService');
    this.tokenRefreshService.stopTokenRefresh();
  }
}
