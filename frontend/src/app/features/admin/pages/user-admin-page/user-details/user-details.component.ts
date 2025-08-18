import { Component, inject, input, signal, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { User } from '@users/interfaces/user.interface';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '@users/services/user.service';
import { Router } from '@angular/router';
import { FormUtilsService } from '@shared/services/form-utils.service';
import { PasswordUtilsService } from '@shared/services/password-utils.service';
import { CommonModule } from '@angular/common';
import { FeedbackMessageComponent } from '@shared/components/feedback-message/feedback-message.component';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { UserFormComponent } from '@users/components/user-form/user-form.component';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'user-details',
  imports: [ReactiveFormsModule, CommonModule, FeedbackMessageComponent, UserFormComponent],
  templateUrl: './user-details.component.html',
})
export class UserDetailsComponent implements OnDestroy {

  user = input.required<User>();

  fb = inject(FormBuilder);
  router = inject(Router);

  userService = inject(UserService);

  formUtils = inject(FormUtilsService);
  passwordUtils = inject(PasswordUtilsService);
  feedbackService = inject(FeedbackMessageService);
  private cdr = inject(ChangeDetectorRef);

  wasSaved = signal(false);
  isLoading = signal(false);
  isEditMode = signal(false);
  showDeleteModal = signal(false);
  currentUser = signal<User | null>(null);

  userForm = this.fb.nonNullable.group({
    username: ['', [
      Validators.required,
      this.formUtils.usernameValidator
    ]],
    email: ['', [
      Validators.required,
      Validators.pattern(FormUtilsService.emailPattern),
    ]],
    password: ['', [
      // Validators.required,
      this.passwordUtils.passwordValidator
    ]],
    confirmPassword: ['', [
      // Validators.required,
      this.passwordUtils.passwordValidator
    ]],
    isActive: [true],
    selectedRole: ['' as string, [Validators.required]],
  }, {
    validators: [
      FormUtilsService.isFieldOneEqualFieldTwo('password', 'confirmPassword')
    ]
  });

  ngOnInit() {
    this.feedbackService.clearMessage();
    const user = this.user();
    this.currentUser.set(user);
    this.setFormValues(user);
    this.isEditMode.set(user.id !== 'new');
  }

  private setFormValues(user: Partial<User>) {
    const selectedRole = user.roles && user.roles.length > 0 ? user.roles[0] : '';

    this.userForm.patchValue({
      username: user.username || '',
      email: user.email || '',
      password: '',
      confirmPassword: '',
      isActive: user.isActive ?? true,
      selectedRole: selectedRole,
    });

    this.cdr.detectChanges();
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
    } catch (error) {
      this.handleUserError(error);
    } finally {
      this.isLoading.set(false);
    }
  }

  private async createUser() {
    const formValue = this.userForm.value;
    const userData = {
      username: formValue.username?.trim().toLowerCase(),
      email: formValue.email?.trim().toLowerCase(),
      password: formValue.password,
      isActive: formValue.isActive,
      roles: formValue.selectedRole ? [formValue.selectedRole] : []
    };

    try {
      const createdUser = await firstValueFrom(
        this.userService.createUser(userData)
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
    } catch (error) {
      this.handleUserError(error);
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
    const changes = this.detectChanges(formValue, originalUser);

    if (Object.keys(changes).length === 0) {
      this.feedbackService.showWarning('No changes detected. Nothing to save.');
      return;
    }

    try {
      const updatedUser: User = await firstValueFrom(
        this.userService.updateUser(originalUser.id, changes)
      );

      if (updatedUser?.id) {
        this.currentUser.set(updatedUser);
        this.setFormValues(updatedUser);
        this.feedbackService.showSuccess('User updated successfully!');
        this.wasSaved.set(true);
      }
    } catch (error) {
      this.handleUserError(error);
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
        this.userService.deleteUser(user.id)
      );

      if (response) {
        this.feedbackService.showSuccess('User deleted successfully!');
        this.router.navigate(['/admin/users']);
      } else {
        this.feedbackService.showError('Failed to delete user');
      }
    } catch (error) {
      this.handleUserError(error);
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

  private handleUserError(error: any): void {
    this.setFormValues(this.currentUser() || this.user());

    // Extract error message from ErrorResponse if available
    let errorMessage = 'An error occurred while saving the user. Please try again.';

    if (error?.errorResponse?.message) {
      errorMessage = error.errorResponse.message;
    } else if (error?.message) {
      errorMessage = error.message;
    }

    this.feedbackService.showError(errorMessage);
  }

  private detectChanges(formValue: any, originalUser: User): any {
    const changes: any = {};

    const compareStrings = (original: string | undefined, newValue: string | undefined) => {
      return (original?.trim() || '') !== (newValue?.trim() || '');
    };

    if (compareStrings(originalUser.username, formValue.username)) {
      changes.username = formValue.username?.trim();
    }

    if (compareStrings(originalUser.email, formValue.email)) {
      changes.email = formValue.email?.trim();
    }

    if (formValue.isActive !== originalUser.isActive) {
      changes.isActive = formValue.isActive;
    }

    const currentRole = originalUser.roles?.[0] || '';
    const newRole = formValue.selectedRole || '';

    if (newRole !== currentRole) {
      changes.roles = newRole ? [newRole] : [];
    }

    if (formValue.password?.trim()) {
      changes.password = formValue.password.trim();
    }

    return changes;
  }

  resetForm() {
    this.feedbackService.clearMessage();
    const user = this.currentUser() || this.user();
    this.setFormValues(user);
  }

  goBack() {
    this.feedbackService.clearMessage();
    this.router.navigate(['/admin/users']);
  }

  goToPersonData() {
    this.router.navigate(['/admin/users/person', this.user().id]);
  }

  onRoleSelected(roleName: string) {
    const control = this.userForm.get('selectedRole');
    control?.setValue(roleName);
  }

  onReset() {
    this.resetForm();
  }

  ngOnDestroy() {
    this.feedbackService.clearMessage();
  }
}
