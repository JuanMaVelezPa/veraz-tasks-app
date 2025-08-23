import { Component, inject, input, signal, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { User } from '@users/interfaces/user.interface';
import { Person } from '@person/interfaces/person.interface';
import { ReactiveFormsModule } from '@angular/forms';
import { UserService } from '@users/services/user.service';
import { PersonService } from '@person/services/person.service';
import { Router } from '@angular/router';
import { FormBuildersManagerService } from '@shared/services/form-builders-manager.service';
import { HttpErrorService } from '@shared/services/http-error.service';
import { CommonModule } from '@angular/common';
import { FeedbackMessageComponent } from '@shared/components/feedback-message/feedback-message.component';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { NavigationHistoryService } from '@shared/services/navigation-history.service';
import { PersonAssociationService } from '@person/services/person-association.service';
import { UserFormComponent } from '@users/components/user-form/user-form.component';

import { AuthService } from '@auth/services/auth.service';
import { IconComponent } from '@shared/components/icon/icon.component';
import { firstValueFrom, catchError } from 'rxjs';

@Component({
  selector: 'user-details',
  imports: [ReactiveFormsModule, CommonModule, FeedbackMessageComponent, UserFormComponent, IconComponent],
  templateUrl: './user-details.component.html',
})
export class UserDetailsComponent implements OnInit, OnDestroy {

  user = input.required<User>();

  formBuilders = inject(FormBuildersManagerService);
  router = inject(Router);

  userService = inject(UserService);
  personService = inject(PersonService);

  httpErrorService = inject(HttpErrorService);
  feedbackService = inject(FeedbackMessageService);
  navigationHistory = inject(NavigationHistoryService);
  personAssociationService = inject(PersonAssociationService);
  authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  wasSaved = signal(false);
  isLoading = signal(false);
  isEditMode = signal(false);
  showDeleteModal = signal(false);
  currentUser = signal<User | null>(null);

  // Person related signals
  personalProfile = signal<Person | null>(null);
  isLoadingPerson = signal(false);

  userForm = this.formBuilders.buildUserForm({ isEditMode: false });

  ngOnInit() {
    this.feedbackService.clearMessage();
    const user = this.user();
    this.currentUser.set(user);
    this.isEditMode.set(user.id !== 'new');

    // Recreate form with correct edit mode
    this.userForm = this.formBuilders.buildUserForm({
      isEditMode: this.isEditMode()
    });

    this.setFormValues(user);
    this.setPasswordValidations();
    if (user.id !== 'new') {
      this.loadPersonalProfile();
    }
  }

  private setFormValues(user: Partial<User>) {
    this.formBuilders.patchForm(this.userForm, user);
    this.cdr.detectChanges();
  }

  private setPasswordValidations() {
    // No need to recreate the form, just update password validations if needed
    // The form is already built with the correct isEditMode in the constructor
  }

  async onSubmit() {
    this.feedbackService.clearMessage();

    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      this.feedbackService.showError('Please fix the validation errors before saving.');
      return;
    }

    this.isLoading.set(true);

    try {
      if (this.isEditMode()) {
        await this.updateUser();
      } else {
        await this.createUser();
      }
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while saving the user.');
      // Reset form to original values after error
      this.setFormValues(this.currentUser()!);
    } finally {
      this.isLoading.set(false);
    }
  }

  private async createUser() {
    const formValue = this.userForm.value;
    const userData = {
      ...this.formBuilders.prepareUserFormData(formValue),
      roles: formValue.selectedRole ? [formValue.selectedRole] : []
    };

    try {
      const createdUser = await firstValueFrom(
        this.userService.createUser(userData).pipe(
          catchError(error => this.httpErrorService.handleError(error, 'creating user'))
        )
      );

      if (createdUser?.id) {
        this.currentUser.set(createdUser);
        this.wasSaved.set(true);
        this.feedbackService.showSuccess('User created successfully!');
        // Use setTimeout to ensure the message is shown before navigation
        setTimeout(() => {
          this.router.navigate(['/admin/users']);
        }, 500);
      }
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while creating the user.');
      // Reset form to original values after error
      this.setFormValues(this.currentUser()!);
      throw error;
    }
  }

  private async updateUser() {
    const originalUser = this.currentUser();
    if (!originalUser) { return; }

    // Validate password fields
    if (!this.validatePasswordFields()) {
      return;
    }

    const formValue = this.userForm.value;
    const changes = this.formBuilders.detectUserChanges(formValue, originalUser, {
      includeRoles: true,
      includePassword: true
    });

    if (Object.keys(changes).length === 0) {
      this.feedbackService.showWarning('No changes detected. Nothing to save.');
      return;
    }

    // Check if username or email changed
    const hasLoginChanges = changes.username || changes.email;
    const isCurrentUser = this.authService.user()?.id === originalUser.id;

    try {
      const updatedUser: User = await firstValueFrom(
        this.userService.updateUser(originalUser.id, changes).pipe(
          catchError(error => this.httpErrorService.handleError(error, 'updating user'))
        )
      );

      if (updatedUser?.id) {
        this.currentUser.set(updatedUser);

        // Update auth state if it's the current user
        if (isCurrentUser) {
          await firstValueFrom(this.authService.updateCurrentUserAndRefreshIfNeeded(updatedUser));
          this.feedbackService.showSuccess('User updated successfully!');
        } else {
          this.feedbackService.showSuccess('User updated successfully!');
        }

        // Reload personal profile if user data changed (username/email might affect profile)
        await this.loadPersonalProfile();

        this.formBuilders.patchForm(this.userForm, {
          password: '',
          confirmPassword: ''
        });

        this.wasSaved.set(true);
      }
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while updating the user.');
      // Reset form to original values after error
      this.setFormValues(this.currentUser()!);
    }
  }

  private validatePasswordFields(): boolean {
    const passwordValue = this.userForm.get('password')?.value?.trim();
    const confirmPasswordValue = this.userForm.get('confirmPassword')?.value?.trim();

    // If either password field has a value, both must have values
    if (passwordValue || confirmPasswordValue) {
      if (!passwordValue) {
        this.feedbackService.showError('If you enter password confirmation, you must also enter the new password');
        return false;
      }
      if (!confirmPasswordValue) {
        this.feedbackService.showError('If you enter a new password, you must also confirm it');
        return false;
      }
      if (passwordValue !== confirmPasswordValue) {
        this.feedbackService.showError('Passwords do not match');
        return false;
      }
    }

    return true;
  }

  async deleteUser() {
    const user = this.currentUser();
    if (!user || user.id === 'new') return;

    this.isLoading.set(true);
    this.feedbackService.clearMessage();

    try {
      const response = await firstValueFrom(
        this.userService.deleteUser(user.id).pipe(
          catchError(error => this.httpErrorService.handleError(error, 'deleting user'))
        )
      );

      // For delete operations, response will be true if successful
      this.feedbackService.showSuccess('User deleted successfully!');
      this.router.navigate(['/admin/users']);
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while deleting the user.');
    } finally {
      this.isLoading.set(false);
      this.showDeleteModal.set(false);
    }
  }

  showDeleteConfirmation() {
    this.showDeleteModal.set(true);
  }

  cancelDelete() {
    this.showDeleteModal.set(false);
  }

  goBack() {
    this.feedbackService.clearMessage();
    this.navigationHistory.goBack('/admin/users');
  }

  onRoleSelected(roleName: string) {
    const control = this.userForm.get('selectedRole');
    control?.setValue(roleName);
  }

  private async loadPersonalProfile() {
    const user = this.currentUser();
    if (!user || user.id === 'new') {
      return;
    }

    this.isLoadingPerson.set(true);

    try {
      const searchOptions = { page: 0, size: 1000, sort: 'id', order: 'asc' as const, search: '' };
      const personsResponse = await firstValueFrom(
        this.personService.getPersons(searchOptions)
      );

      const personalProfile = personsResponse.data.find(person => person.userId === user.id);
      this.personalProfile.set(personalProfile || null);
    } catch (error) {
      this.personalProfile.set(null);
    } finally {
      this.isLoadingPerson.set(false);
    }
  }

  removePersonalProfile() {
    const person = this.personalProfile();
    const user = this.currentUser();
    if (!person || !user) return;

    this.performRemoval();
  }

  private async performRemoval() {
    const person = this.personalProfile();
    if (!person) return;

    this.isLoadingPerson.set(true);
    try {
      await this.personAssociationService.removePersonalProfile(person.id, `${person.firstName} ${person.lastName}`);
      this.personalProfile.set(null);
    } finally {
      this.isLoadingPerson.set(false);
    }
  }

  linkExistingPerson() {
    this.router.navigate(['/admin/persons'], {
      queryParams: {
        mode: 'select',
        userId: this.currentUser()?.id
      }
    });
  }

  createNewPerson() {
    this.router.navigate(['/admin/persons/new'], {
      queryParams: {
        userId: this.currentUser()?.id
      }
    });
  }

  goToPersonManagement() {
    this.router.navigate(['/admin/users', this.currentUser()?.id, 'person']);
  }

  toggleUserStatus(event: Event) {
    const target = event.target as HTMLInputElement;
    const isActive = target.checked;

    this.formBuilders.patchForm(this.userForm, {
      isActive: isActive
    });
  }

  ngOnDestroy() {
    this.feedbackService.clearMessage();
  }
}
