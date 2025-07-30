import { Component, inject, input, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { RoleService } from '@users/services/role.service';
import { RoleResponse } from '@users/interfaces/role-response.interface';

@Component({
  selector: 'app-user-role-selector',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-role-selector.component.html'
})
export class UserRoleSelectorComponent {

  selectedRole = input<string>('');
  roleSelected = output<string>();

  private roleService = inject(RoleService);

  availableRoles = signal<RoleResponse[]>([]);
  isLoadingRoles = signal(true);

  ngOnInit() {
    this.loadAvailableRoles();
  }

  private loadAvailableRoles() {
    this.isLoadingRoles.set(true);
    this.roleService.getActiveRoles().subscribe({
      next: (roles: RoleResponse[]) => {
        this.availableRoles.set(roles);
        this.isLoadingRoles.set(false);
      },
      error: (error: any) => {
        this.availableRoles.set([]);
        this.isLoadingRoles.set(false);
      }
    });
  }

  selectRole(roleName: string) {
    this.roleSelected.emit(roleName);
  }

  isRoleSelected(roleName: string): boolean {
    return this.selectedRole() === roleName;
  }
}
