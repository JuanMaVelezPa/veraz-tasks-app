import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { authGuard } from '@core/guards';

export const mainRoutes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
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
        path: '**',
        component: NotFoundComponent
      }
    ]
  },
  {
    path: '**',
    redirectTo: ''
  }
]

export default mainRoutes;
