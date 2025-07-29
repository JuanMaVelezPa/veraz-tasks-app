import { Component, inject, input, signal } from '@angular/core';
import { User } from '@auth/interfaces/user';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '@users/services/user.service';
import { RoleService } from '@users/services/role.service';
import { Role } from '@users/interfaces/role.interface';
import { Router } from '@angular/router';
import { FormUtilsService } from '@shared/services/form-utils.service';
import { PasswordUtilsService } from '@shared/services/password-utils.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'user-details',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './user-details.component.html',
})
export class UserDetailsComponent {

  user = input.required<User>();

  fb = inject(FormBuilder);
  userService = inject(UserService);
  roleService = inject(RoleService);
  router = inject(Router);
  formUtils = inject(FormUtilsService);
  passwordUtils = inject(PasswordUtilsService);

  wasSaved = signal(false);
  isLoading = signal(false);
  isEditMode = signal(false);
  availableRoles = signal<Role[]>([]);
  showPasswordSection = signal(false);

  userForm = this.fb.nonNullable.group({
    username: ['', [
      Validators.required,
      this.formUtils.usernameValidator
    ]],
    email: ['', [
      Validators.required,
      Validators.email,
    ]],
    isActive: [true],
    selectedRoles: [[] as string[]],
  });

  ngOnInit() {
    this.setFormValues(this.user());
    this.isEditMode.set(this.user().id !== 'new');
    this.loadAvailableRoles();
  }

  private setFormValues(user: Partial<User>) {
    this.userForm.reset({
      username: user.username || '',
      email: user.email || '',
      isActive: user.isActive ?? true,
      selectedRoles: user.roles || [],
    });
  }

  private loadAvailableRoles() {
    this.roleService.getActiveRoles().subscribe({
      next: (roles: Role[]) => {
        this.availableRoles.set(roles);
      },
      error: (error: any) => {
        console.error('Error loading roles:', error);
        // Fallback: roles básicos si no se pueden cargar
        this.availableRoles.set([
          { id: '1', name: 'ADMIN', description: 'Administrator', isActive: true, createdAt: '', updatedAt: null },
          { id: '2', name: 'USER', description: 'Regular User', isActive: true, createdAt: '', updatedAt: null },
          { id: '3', name: 'MANAGER', description: 'Manager', isActive: true, createdAt: '', updatedAt: null },
        ]);
      }
    });
  }

  onSubmit() {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }

    const formValue = this.userForm.value;
    const { username, email, isActive, selectedRoles } = formValue;

    if (!username || !email) {
      return;
    }

    this.isLoading.set(true);

    const userData = {
      username: username.toLowerCase(),
      email: email.toLowerCase(),
      isActive: isActive ?? true,
      roles: selectedRoles || [],
    };

    if (this.isEditMode()) {
      // Actualizar usuario existente
      this.userService.updateUser(this.user().id, userData).subscribe({
        next: (response: User) => {
          this.wasSaved.set(true);
          this.isLoading.set(false);
          console.log('Usuario actualizado:', response);
        },
        error: (error: any) => {
          this.isLoading.set(false);
          console.error('Error al actualizar usuario:', error);
        }
      });
    } else {
      // Crear nuevo usuario
      this.userService.createUser(userData).subscribe({
        next: (response: User) => {
          this.wasSaved.set(true);
          this.isLoading.set(false);
          console.log('Usuario creado:', response);
        },
        error: (error: any) => {
          this.isLoading.set(false);
          console.error('Error al crear usuario:', error);
        }
      });
    }
  }

  togglePasswordSection() {
    this.showPasswordSection.update(show => !show);
  }

  resetForm() {
    this.formUtils.resetForm(this.userForm);
    this.setFormValues(this.user());
    this.showPasswordSection.set(false);
  }

  goBack() {
    this.router.navigate(['/admin/users']);
  }

  goToPersonData() {
    // TODO: Implementar navegación a la página de datos de persona
    // Por ahora navega a la misma página pero con parámetro para indicar que es modo persona
    this.router.navigate(['/admin/users/person', this.user().id]);
  }

  isRoleSelected(roleName: string): boolean {
    return this.userForm.get('selectedRoles')?.value?.includes(roleName) || false;
  }

  toggleRole(roleName: string) {
    const selectedRoles = this.userForm.get('selectedRoles');
    if (selectedRoles) {
      const currentRoles = selectedRoles.value || [];
      const updatedRoles = currentRoles.includes(roleName)
        ? currentRoles.filter(role => role !== roleName)
        : [...currentRoles, roleName];
      selectedRoles.setValue(updatedRoles);
    }
  }
}
