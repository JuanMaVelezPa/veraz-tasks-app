import { Component, signal, inject } from '@angular/core';
import { RouterOutlet, Router, NavigationStart, NavigationEnd } from '@angular/router';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-admin-dashboard-layout',
  imports: [RouterOutlet, LoadingComponent],
  templateUrl: './admin-layout.component.html',
})
export class AdminDashboardLayoutComponent {
  private router = inject(Router);
  isLoading = signal(false);

  constructor() {
    this.router.events
      .pipe(filter(event => event instanceof NavigationStart || event instanceof NavigationEnd))
      .subscribe(event => {
        this.isLoading.set(event instanceof NavigationStart);
      });
  }
}
