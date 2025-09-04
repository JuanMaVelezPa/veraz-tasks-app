import { Routes } from '@angular/router';
import { PersonsAdminPageComponent } from './persons-admin-page.component';
import { PersonAdminPageComponent } from './person-admin-page/person-admin-page.component';

export const personsRoutes: Routes = [
  {
    path: '',
    component: PersonsAdminPageComponent
  },
  {
    path: ':id',
    component: PersonAdminPageComponent
  }
];

export default personsRoutes;
