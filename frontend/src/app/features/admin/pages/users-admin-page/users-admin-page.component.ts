import { Component } from '@angular/core';
import { UserTableComponent } from 'src/app/features/users/components/user-table/user-table.component';

@Component({
  selector: 'app-users-admin-page',
  imports: [UserTableComponent],
  templateUrl: './users-admin-page.component.html',
})
export class UsersAdminPageComponent {
}
