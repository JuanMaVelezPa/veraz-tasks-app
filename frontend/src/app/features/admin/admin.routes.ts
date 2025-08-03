import { Routes } from '@angular/router';
import { AdminDashboardLayoutComponent } from './layout/admin-layout/admin-layout.component';
import { UsersAdminPageComponent } from './pages/users-admin-page/users-admin-page.component';
import { UserAdminPageComponent } from './pages/user-admin-page/user-admin-page.component';
import { PersonsAdminPageComponent } from './pages/persons-admin-page/persons-admin-page.component';
import { PersonAdminPageComponent } from './pages/person-admin-page/person-admin-page.component';

export const adminRoutes: Routes = [

  {
    path: '',
    component: AdminDashboardLayoutComponent,
    children: [
      {
        path: 'users',
        component: UsersAdminPageComponent
      },
      {
        path: 'users/:id',
        component: UserAdminPageComponent
      },
      {
        path: 'persons',
        component: PersonsAdminPageComponent
      },
      {
        path: 'persons/:id',
        component: PersonAdminPageComponent
      },
      {
        path: '**',
        redirectTo: 'admin/users'
      }
    ]
  }

]

export default adminRoutes;
