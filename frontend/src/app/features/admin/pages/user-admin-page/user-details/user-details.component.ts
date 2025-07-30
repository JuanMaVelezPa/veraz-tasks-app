import { Component, inject, input, signal, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { User } from '@auth/interfaces/user.interface';
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
  private currentUser = signal<User | null>(null);

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
      this.passwordUtils.passwordValidator
    ]],
    confirmPassword: ['', [
      this.passwordUtils.passwordValidator
    ]],
    isActive: [true],
    selectedRole: ['' as string],
  }, {
    validators: [
      FormUtilsService.isFieldOneEqualFieldTwo('password', 'confirmPassword')
    ]
  });

  ngOnInit() {
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
      this.feedbackService.showError('An error occurred while saving the user. Please try again.');
      this.router.navigate(['/admin/users']);
    } finally {
      this.isLoading.set(false);
    }
  }

  private async createUser() {
    const formValue = this.userForm.value;
    const userData = {
      username: formValue.username,
      email: formValue.email,
      isActive: formValue.isActive,
      roles: formValue.selectedRole ? [formValue.selectedRole] : [],
      password: formValue.password
    };

    const createdUser = await firstValueFrom(
      this.userService.createUser(userData)
    );

    if (createdUser?.id) {
      this.feedbackService.showSuccess('User created successfully!');
      this.router.navigate(['/admin/users', createdUser.id]);
      this.wasSaved.set(true);
    } else {
      throw new Error('Created user has invalid ID');
    }
  }

  private async updateUser() {
    const originalUser = this.currentUser();
    if (!originalUser) {
      this.feedbackService.showError('User data not available.');
      return;
    }

    const formValue = this.userForm.value;
    const changes = this.detectChanges(formValue, originalUser);

    if (Object.keys(changes).length === 0) {
      this.feedbackService.showWarning('No changes detected. Nothing to save.');
      return;
    }

    const updatedUser = await firstValueFrom(
      this.userService.updateUser(originalUser.id, changes)
    );

    this.currentUser.set(updatedUser);
    this.setFormValues(updatedUser);

    this.feedbackService.showSuccess('User updated successfully!');
    this.wasSaved.set(true);
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

  updateFormDisabledState(disabled: boolean) {
    if (disabled) {
      this.userForm.disable();
    } else {
      this.userForm.enable();
    }
  }

  ngOnDestroy() {
    this.feedbackService.clearMessage();
  }


}
