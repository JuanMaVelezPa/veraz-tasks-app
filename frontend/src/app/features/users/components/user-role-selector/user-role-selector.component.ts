import { Component, inject, input, OnInit, output, signal, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RoleService } from '@users/services/role.service';
import { RoleResponse } from '@users/interfaces/role.interface';
import { LoadingComponent } from '@shared/components/loading/loading.component';

@Component({
  selector: 'app-user-role-selector',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, LoadingComponent],
  templateUrl: './user-role-selector.component.html'
})
export class UserRoleSelectorComponent implements OnInit {

  selectedRoles = input<string[]>([]);
  isReadOnly = input<boolean>(false);
  rolesSelected = output<string[]>();

  private roleService = inject(RoleService);

  availableRoles = signal<RoleResponse[]>([]);
  isLoadingRoles = signal(true);
  selectedRoleNames = signal<string[]>([]);
  private isUserInteracting = false;

  ngOnInit() {
    this.loadAvailableRoles();
    if (this.selectedRoleNames().length === 0) {
      this.selectedRoleNames.set(this.selectedRoles());
    }
  }

  constructor() {
    effect(() => {
      if (this.isUserInteracting) {
        return;
      }

      const inputRoles = this.selectedRoles();
      const currentRoles = this.selectedRoleNames();
      if (JSON.stringify(inputRoles.sort()) !== JSON.stringify(currentRoles.sort())) {
        this.selectedRoleNames.set(inputRoles);
      }
    });
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

  toggleRole(roleName: string) {
    this.isUserInteracting = true;

    const currentRoles = [...this.selectedRoleNames()];
    const index = currentRoles.indexOf(roleName);

    if (index > -1) {
      // Remove role if already selected
      currentRoles.splice(index, 1);
    } else {
      // Add role if not selected
      currentRoles.push(roleName);
    }

    this.selectedRoleNames.set(currentRoles);
    this.rolesSelected.emit(currentRoles);

    // Reset the flag after a short delay to allow for external updates
    setTimeout(() => {
      this.isUserInteracting = false;
    }, 100);
  }

  isRoleSelected(roleName: string): boolean {
    return this.selectedRoleNames().includes(roleName);
  }

  getSelectedRolesCount(): number {
    return this.selectedRoleNames().length;
  }

  getSelectedRolesNames(): string {
    if (this.selectedRoleNames().length === 0) {
      return 'No roles selected';
    }

    return this.selectedRoleNames()
      .map(roleName => roleName.charAt(0).toUpperCase() + roleName.slice(1).toLowerCase())
      .join(', ');
  }
}
