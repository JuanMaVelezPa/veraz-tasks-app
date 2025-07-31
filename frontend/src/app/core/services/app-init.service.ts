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
    if (this.authService.authStatus() === 'authenticated') {
      this.tokenRefreshService.startTokenRefresh();
    }
  }

  startTokenRefresh(): void {
    this.tokenRefreshService.startTokenRefresh();
  }

  stopTokenRefresh(): void {
    this.tokenRefreshService.stopTokenRefresh();
  }
}
