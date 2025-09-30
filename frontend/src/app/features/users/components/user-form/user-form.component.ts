import { Component, inject, input, output, signal, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup } from '@angular/forms';
import { FormUtilsService } from '@shared/services/form-utils.service';
import { PasswordUtilsService } from '@shared/services/password-utils.service';
import { ScrollService } from '@shared/services/scroll.service';
import { UserRoleSelectorComponent } from '../user-role-selector/user-role-selector.component';
import { IconComponent } from '@shared/components/icon/icon.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, UserRoleSelectorComponent,
    IconComponent, LoadingComponent],
  templateUrl: './user-form.component.html'
})
export class UserFormComponent {

  userForm = input.required<FormGroup>();
  isEditMode = input<boolean>(false);
  isLoading = input<boolean>(false);
  user = input.required<any>();
  isReadOnly = input<boolean>(false);

  formSubmitted = output<void>();
  roleSelected = output<string>();
  rolesSelected = output<string[]>();

  formUtils = inject(FormUtilsService);
  passwordUtils = inject(PasswordUtilsService);
  scrollService = inject(ScrollService);

  showPasswordSection = signal(false);

  private editModeEffect = effect(() => {
    this.showPasswordSection.set(!this.isEditMode());
  });

  onSubmit(): void {
    if (this.userForm().invalid) {
      this.userForm().markAllAsTouched();
      return;
    }
    this.formSubmitted.emit();
    this.scrollService.scrollToTop();
  }

  togglePasswordSection(): void {
    this.showPasswordSection.update(current => !current);
  }

  isPasswordRequired(): boolean {
    return !this.isEditMode() || this.showPasswordSection();
  }

  onRoleSelected(roleName: string): void {
    this.roleSelected.emit(roleName);
  }

  onRolesSelected(roles: string[]): void {
    this.rolesSelected.emit(roles);
  }

  getSelectedRoles(): string {
    const roles = this.userForm().get('selectedRoles')?.value ?? [];
    return roles.length > 0 ? roles.join(', ') : 'No roles selected';
  }
}
