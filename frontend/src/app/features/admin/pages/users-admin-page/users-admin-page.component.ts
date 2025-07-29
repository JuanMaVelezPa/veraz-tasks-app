import { Component } from '@angular/core';
import { UserTableComponent } from 'src/app/features/users/components/user-table/user-table.component';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-users-admin-page',
  imports: [UserTableComponent, RouterLink],
  templateUrl: './users-admin-page.component.html',
})
export class UsersAdminPageComponent {
}
