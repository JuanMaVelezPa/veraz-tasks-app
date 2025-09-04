import { Routes } from '@angular/router';
import { EmployeesAdminPageComponent } from './employees-admin-page.component';
import { EmployeeAdminPageComponent } from './employee-admin-page/employee-admin-page.component';
import { EmployeeCreationComponent } from './employee-creation/employee-creation.component';

export const employeesRoutes: Routes = [
  {
    path: '',
    component: EmployeesAdminPageComponent
  },
  {
    path: 'create',
    component: EmployeeCreationComponent
  },
  {
    path: ':id',
    component: EmployeeAdminPageComponent
  }
];

export default employeesRoutes;
