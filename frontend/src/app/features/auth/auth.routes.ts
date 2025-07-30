import { Routes } from '@angular/router';
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component';
import { SignInPageComponent } from './pages/sign-in-page/sign-in-page.component';
import { SignUpPageComponent } from './pages/sign-up-page/sign-up-page.component';

export const authRoutes: Routes = [
  {
    path: '',
    component: AuthLayoutComponent,
    children: [
      { path: 'sign-in', component: SignInPageComponent },
      // { path: 'sign-up', component: SignUpPageComponent, },
      { path: '**', redirectTo: 'sign-in', },
    ],
  },
]

export default authRoutes;
