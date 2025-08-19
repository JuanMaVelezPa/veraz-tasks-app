import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RoleService } from '@users/services/role.service';
import { RoleResponse } from '@users/interfaces/role.interface';

@Component({
  selector: 'app-user-role-selector',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-role-selector.component.html'
})
export class UserRoleSelectorComponent implements OnInit {

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
        console.error('Error loading roles:', error);
        this.availableRoles.set([]);
        this.isLoadingRoles.set(false);
      }
    });
  }

  selectRole(roleName: string) {
    this.roleSelected.emit(roleName);
  }

  onRoleChange(event: Event) {
    const select = event.target as HTMLSelectElement;
    const roleName = select.value;
    this.selectRole(roleName);
  }

  isRoleSelected(roleName: string): boolean {
    return this.selectedRole() === roleName;
  }

  getSelectedRoleDescription(): string | null {
    const selectedRole = this.availableRoles().find(role => role.name === this.selectedRole());
    return selectedRole?.description || null;
  }
}
