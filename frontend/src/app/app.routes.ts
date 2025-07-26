import { Routes } from '@angular/router';
import { notAuthenticatedGuard } from '@core/guards/not-authenticated.guard';
import { authGuard } from '@core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('@auth/auth.routes').then(m => m.authRoutes),
    canMatch: [notAuthenticatedGuard]
  },
  {
    path: 'admin',
    loadChildren: () => import('@admin/admin.routes').then(m => m.adminRoutes),
    canMatch: [authGuard]
  },
  {
    path: 'person',
    loadChildren: () => import('@person/person.routes').then(m => m.personRoutes),
    canMatch: [authGuard]
  },
  {
    path: '',
    loadChildren: () => import('@shared/main-front.routes').then(m => m.mainRoutes),
    canMatch: [authGuard]
  }
];
