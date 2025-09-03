import { Component, inject, input, output, signal, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup } from '@angular/forms';
import { FormUtilsService } from '@shared/services/form-utils.service';
import { PasswordUtilsService } from '@shared/services/password-utils.service';
import { ScrollService } from '@shared/services/scroll.service';
import { UserRoleSelectorComponent } from '../user-role-selector/user-role-selector.component';
import { IconComponent } from '@shared/components/icon/icon.component';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, UserRoleSelectorComponent, IconComponent],
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

  formUtils = inject(FormUtilsService);
  passwordUtils = inject(PasswordUtilsService);
  scrollService = inject(ScrollService);

  showPasswordSection = signal(false);

  private editModeEffect = effect(() => {
    // En modo creación, mostrar la sección de contraseña por defecto
    // En modo edición, ocultarla por defecto
    this.showPasswordSection.set(!this.isEditMode());
  });

  onSubmit() {
    if (this.userForm().invalid) {
      this.userForm().markAllAsTouched();
      return;
    }
    this.formSubmitted.emit();
    this.scrollService.scrollToTop();
  }

  togglePasswordSection() {
    this.showPasswordSection.update(current => !current);
  }

  isPasswordRequired(): boolean {
    // En modo creación, la contraseña siempre es requerida
    // En modo edición, solo es requerida si la sección está abierta
    return !this.isEditMode() || this.showPasswordSection();
  }

  onRoleSelected(roleName: string) {
    this.roleSelected.emit(roleName);
  }

  getSelectedRole(): string {
    return this.userForm().get('selectedRole')?.value ?? '';
  }
}
