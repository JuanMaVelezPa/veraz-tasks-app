import { Injectable, inject } from '@angular/core';
import { AuthService } from './auth.service';
import { interval, Subscription } from 'rxjs';
import { switchMap, filter } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TokenRefreshService {

  private refreshSubscription?: Subscription;
  private readonly REFRESH_INTERVAL = 60 * 60 * 1000; // 30 minutos

  // Lazy injection para evitar dependencia circular
  private get authService(): AuthService {
    return inject(AuthService);
  }

  startTokenRefresh(): void {
    if (this.refreshSubscription) {
      this.refreshSubscription.unsubscribe();
    }

    this.refreshSubscription = interval(this.REFRESH_INTERVAL)
      .pipe(
        filter(() => this.authService.authStatus() === 'authenticated'),
        switchMap(() => {
          if (this.authService.isTokenExpiringSoon()) {
            return this.authService.refreshToken();
          }
          return [];
        })
      )
      .subscribe({
        next: (success) => {
          if (!success) {
            console.warn('TokenRefreshService: Error refrescando token');
          }
        },
        error: (error) => {
          console.error('TokenRefreshService: Error en el proceso de refresco:', error);
        }
      });
  }

  stopTokenRefresh(): void {
    if (this.refreshSubscription) {
      this.refreshSubscription.unsubscribe();
      this.refreshSubscription = undefined;
    }
  }

  ngOnDestroy(): void {
    this.stopTokenRefresh();
  }
}
