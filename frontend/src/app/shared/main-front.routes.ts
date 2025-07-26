import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';

export const mainRoutes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: '',
        component: HomePageComponent
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
