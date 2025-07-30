import { Component, inject, input, output, signal } from '@angular/core';
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

  ngOnInit() {
    if (!this.isEditMode()) {
      this.showPasswordSection.set(true);
    }
    this.showPasswordSection.set(false);
  }

  onSubmit() {
    this.formSubmitted.emit();
  }

  onReset() {
    this.formReset.emit();
  }

  togglePasswordSection() {
    const newState = !this.showPasswordSection();
    this.showPasswordSection.set(newState);
  }

  isPasswordRequired(): boolean {
    return !this.isEditMode() || this.showPasswordSection();
  }

  hasPasswordValue(): boolean {
    return !!this.userForm().get('password')?.value?.trim();
  }

  shouldValidatePassword(): boolean {
    return this.isPasswordRequired() || this.hasPasswordValue();
  }

  onRoleSelected(roleName: string) {
    this.roleSelected.emit(roleName);
  }

  getSelectedRole(): string {
    return this.userForm().get('selectedRole')?.value || '';
  }
}
