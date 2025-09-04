import { Routes } from '@angular/router';
import { AdminDashboardLayoutComponent } from './layout/admin-layout/admin-layout.component';
import { AdminDashboardComponent } from './pages/admin-dashboard/admin-dashboard.component';

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
        loadChildren: () => import('./pages/users/users.routes').then(m => m.usersRoutes)
      },
      {
        path: 'persons',
        loadChildren: () => import('./pages/persons/persons.routes').then(m => m.personsRoutes)
      },
      {
        path: 'employees',
        loadChildren: () => import('./pages/employees/employees.routes').then(m => m.employeesRoutes)
      },
      {
        path: '**',
        redirectTo: ''
      }
    ]
  }
]

export default adminRoutes;
