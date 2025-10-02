import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { User } from '@users/interfaces/user.interface';
import { UserFormComponent } from '../user-form/user-form.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { IconComponent } from '@shared/components/icon/icon.component';

@Component({
  selector: 'app-user-tab',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, UserFormComponent, LoadingComponent, IconComponent],
  templateUrl: './user-tab.component.html'
})
export class UserTabComponent {
  currentUser = input.required<User | null>();
  isEditMode = input.required<boolean>();
  isLoadingUser = input.required<boolean>();
  userForm = input.required<any>();
  isReadOnly = input<boolean>(false);

  editUser = output<void>();
  removeUserAssociation = output<void>();
  linkExistingUser = output<void>();
  createNewUser = output<void>();
  formSubmitted = output<void>();
  rolesSelected = output<string[]>();
}
