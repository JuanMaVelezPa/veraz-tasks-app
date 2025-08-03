import { Component, inject, input, output, signal, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup } from '@angular/forms';
import { FormUtilsService } from '@shared/services/form-utils.service';
import { PasswordUtilsService } from '@shared/services/password-utils.service';
import { UserRoleSelectorComponent } from '../user-role-selector/user-role-selector.component';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, UserRoleSelectorComponent],
  templateUrl: './user-form.component.html'
})
export class UserFormComponent {

  userForm = input.required<FormGroup>();
  isEditMode = input<boolean>(false);
  isLoading = input<boolean>(false);
  user = input.required<any>();

  formSubmitted = output<void>();
  roleSelected = output<string>();
  formReset = output<void>();

  formUtils = inject(FormUtilsService);
  passwordUtils = inject(PasswordUtilsService);

  showPasswordSection = signal(false);

  private editModeEffect = effect(() => {
    this.showPasswordSection.set(!this.isEditMode());
  });

  onSubmit() {
    if (this.userForm().invalid) {
      this.userForm().markAllAsTouched();
      return;
    }
    this.formSubmitted.emit();
  }

  onReset() {
    this.formReset.emit();
  }

  togglePasswordSection() {
    this.showPasswordSection.update(current => !current);
  }

  isPasswordRequired(): boolean {
    return !this.isEditMode() || this.showPasswordSection();
  }

  onRoleSelected(roleName: string) {
    this.roleSelected.emit(roleName);
  }

  getSelectedRole(): string {
    return this.userForm().get('selectedRole')?.value ?? '';
  }
}
