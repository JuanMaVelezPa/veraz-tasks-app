import { Routes } from '@angular/router';
import { AdminDashboardLayoutComponent } from './layout/admin-layout/admin-layout.component';
import { AdminDashboardComponent } from './pages/admin-dashboard/admin-dashboard.component';
import { UsersAdminPageComponent } from './pages/users-admin-page/users-admin-page.component';
import { UserAdminPageComponent } from './pages/user-admin-page/user-admin-page.component';
import { UserPersonManagementComponent } from './pages/user-admin-page/user-person-management/user-person-management.component';
import { PersonsAdminPageComponent } from './pages/persons-admin-page/persons-admin-page.component';
import { PersonAdminPageComponent } from './pages/person-admin-page/person-admin-page.component';

export const adminRoutes: Routes = [

  {
    path: '',
    component: AdminDashboardLayoutComponent,
    children: [
      {
        path: '',
        component: AdminDashboardComponent
      },
      {
        path: 'users',
        component: UsersAdminPageComponent
      },
      {
        path: 'users/:id',
        component: UserAdminPageComponent
      },
      {
        path: 'users/:id/person',
        component: UserPersonManagementComponent
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
        redirectTo: ''
      }
    ]
  }

]

export default adminRoutes;
