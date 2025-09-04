import { Routes } from '@angular/router';
import { UsersAdminPageComponent } from './users-admin-page.component';
import { UserAdminPageComponent } from './user-admin-page/user-admin-page.component';
import { UserPersonManagementComponent } from './user-admin-page/user-person-management/user-person-management.component';

export const usersRoutes: Routes = [
  {
    path: '',
    component: UsersAdminPageComponent
  },
  {
    path: ':id',
    component: UserAdminPageComponent
  },
  {
    path: ':id/person',
    component: UserPersonManagementComponent
  }
];

export default usersRoutes;
