import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('@auth/auth.routes').then(m => m.authRoutes)
  },
  {
    path: 'person',
    loadChildren: () => import('@person/person.routes').then(m => m.personRoutes)
  },
  {
    path: '**',
    redirectTo: 'auth'
  }
];
