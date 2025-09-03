import { Component, inject, input, signal, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { User } from '@users/interfaces/user.interface';
import { Person } from '@person/interfaces/person.interface';
import { ReactiveFormsModule } from '@angular/forms';
import { UserService } from '@users/services/user.service';
import { PersonService } from '@person/services/person.service';
import { Router, ActivatedRoute } from '@angular/router';
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
import { LoadingComponent } from '@shared/components/loading/loading.component';

@Component({
  selector: 'user-details',
  imports: [ReactiveFormsModule, CommonModule, FeedbackMessageComponent, UserFormComponent,
    IconComponent, LoadingComponent],
  templateUrl: './user-details.component.html',
})
export class UserDetailsComponent implements OnInit, OnDestroy {
  user = input.required<User>();

  // Services
  private readonly formBuilders = inject(FormBuildersManagerService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly userService = inject(UserService);
  private readonly personService = inject(PersonService);
  private readonly httpErrorService = inject(HttpErrorService);
  private readonly feedbackService = inject(FeedbackMessageService);
  private readonly navigationHistory = inject(NavigationHistoryService);
  private readonly personAssociationService = inject(PersonAssociationService);
  private readonly authService = inject(AuthService);
  private readonly cdr = inject(ChangeDetectorRef);

  // State signals
  readonly wasSaved = signal(false);
  readonly isLoading = signal(false);
  readonly isEditMode = signal(false);
  readonly showDeleteModal = signal(false);
  readonly currentUser = signal<User | null>(null);
  readonly personalProfile = signal<Person | null>(null);
  readonly isLoadingPerson = signal(false);
  readonly targetPersonId = signal<string | null>(null);
  readonly returnUrl = signal<string | null>(null);

  // Form
  userForm = this.formBuilders.buildUserForm({ isEditMode: false });

  ngOnInit(): void {
    this.initializeComponent();
  }

  ngOnDestroy(): void {
    this.feedbackService.clearMessage();
  }

  // Public methods
  async submitForm(): Promise<void> {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      this.feedbackService.showError('Please fix the validation errors before saving.');
      return;
    }

    if (this.isEditMode() && !this.validatePasswordFields()) {
      return;
    }

    this.isLoading.set(true);
    try {
      await (this.isEditMode() ? this.updateUser() : this.createUser());
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while saving the user.');
      this.resetFormToOriginalValues();
    } finally {
      this.isLoading.set(false);
    }
  }

  async deleteUser(): Promise<void> {
    const user = this.currentUser();
    if (!user || user.id === 'new') return;

    this.isLoading.set(true);
    this.feedbackService.clearMessage();

    try {
      await firstValueFrom(
        this.userService.deleteUser(user.id).pipe(
          catchError(error => this.httpErrorService.handleError(error, 'deleting user'))
        )
      );

      this.feedbackService.showSuccess('User deleted successfully!');
      this.router.navigate(['/admin/users']);
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while deleting the user.');
    } finally {
      this.isLoading.set(false);
      this.showDeleteModal.set(false);
    }
  }

  showDeleteConfirmation(): void {
    this.showDeleteModal.set(true);
  }

  cancelDelete(): void {
    this.showDeleteModal.set(false);
  }

  goBack(): void {
    this.feedbackService.clearMessage();
    this.navigationHistory.goBack('/admin/users');
  }

  onRoleSelected(roleName: string): void {
    this.userForm.get('selectedRole')?.setValue(roleName);
  }

  removePersonalProfile(): void {
    const person = this.personalProfile();
    if (!person) return;
    this.performRemoval();
  }

  linkExistingPerson(): void {
    this.router.navigate(['/admin/persons'], {
      queryParams: { mode: 'select', userId: this.currentUser()?.id }
    });
  }

  createNewPerson(): void {
    this.router.navigate(['/admin/persons/new'], {
      queryParams: { userId: this.currentUser()?.id }
    });
  }

  goToPersonManagement(): void {
    this.router.navigate(['/admin/users', this.currentUser()?.id, 'person']);
  }

  toggleUserStatus(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.formBuilders.patchForm(this.userForm, { isActive: target.checked });
  }

  // Private methods
  private initializeComponent(): void {
    this.feedbackService.clearMessage();
    const user = this.user();

    this.currentUser.set(user);
    this.isEditMode.set(user.id !== 'new');
    this.capturePersonAssociationParams();
    this.setupForm();
    this.setFormValues(user);
    this.setPasswordValidations();

    if (user.id !== 'new') {
      this.loadPersonalProfile();
    }
  }

  private capturePersonAssociationParams(): void {
    const personId = this.route.snapshot.queryParamMap.get('personId');
    const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl');

    if (personId) {
      this.targetPersonId.set(personId);
      this.returnUrl.set(returnUrl);
    }
  }

  private setupForm(): void {
    this.userForm = this.formBuilders.buildUserForm({
      isEditMode: this.isEditMode()
    });
  }

  private setFormValues(user: Partial<User>): void {
    this.formBuilders.patchForm(this.userForm, user);

    if (user.roles && user.roles.length > 0) {
      this.userForm.get('selectedRole')?.setValue(user.roles[0]);
    }

    this.cdr.detectChanges();
  }

  private setPasswordValidations(): void {
    // Password validations are handled in the form builder
  }

  private async createUser(): Promise<void> {
    const userData = this.prepareUserData(this.userForm.value);
    const createdUser = await firstValueFrom(
      this.userService.createUser(userData).pipe(
        catchError(error => this.httpErrorService.handleError(error, 'creating user'))
      )
    );

    if (createdUser?.id) {
      await this.handleUserCreationSuccess(createdUser);
    }
  }

  private async updateUser(): Promise<void> {
    const originalUser = this.currentUser();
    if (!originalUser) return;

    const userData = this.prepareUserData(this.userForm.value);
    const changes = this.formBuilders.detectUserChanges(userData, originalUser, {
      includeRoles: true,
      includePassword: true
    });

    if (Object.keys(changes).length === 0) {
      this.feedbackService.showWarning('No changes detected.');
      return;
    }

    const updatedUser = await firstValueFrom(
      this.userService.updateUser(originalUser.id, changes).pipe(
        catchError(error => this.httpErrorService.handleError(error, 'updating user'))
      )
    );

    if (updatedUser?.id) {
      this.handleUserUpdateSuccess(updatedUser);
    }
  }

  private validatePasswordFields(): boolean {
    const password = this.userForm.get('password')?.value?.trim();
    const confirmPassword = this.userForm.get('confirmPassword')?.value?.trim();

    if (password || confirmPassword) {
      if (!password) {
        this.feedbackService.showError('If you enter password confirmation, you must also enter the new password');
        return false;
      }
      if (!confirmPassword) {
        this.feedbackService.showError('If you enter a new password, you must also confirm it');
        return false;
      }
      if (password !== confirmPassword) {
        this.feedbackService.showError('Passwords do not match');
        return false;
      }
    }

    return true;
  }

  private async loadPersonalProfile(): Promise<void> {
    const user = this.currentUser();
    if (!user || user.id === 'new') return;

    this.isLoadingPerson.set(true);
    try {
      const searchOptions = { page: 0, size: 1000, sort: 'id', order: 'asc' as const, search: '' };
      const personsResponse = await firstValueFrom(this.personService.getPersons(searchOptions));
      const personalProfile = personsResponse.data.find(person => person.userId === user.id);
      this.personalProfile.set(personalProfile || null);
    } catch (error) {
      this.personalProfile.set(null);
    } finally {
      this.isLoadingPerson.set(false);
    }
  }

  private async performRemoval(): Promise<void> {
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

  private prepareUserData(formValue: any): any {
    const processedData = this.formBuilders.prepareUserFormData(formValue);
    processedData.roles = formValue.selectedRole ? [formValue.selectedRole] : [];

    if (this.isEditMode()) {
      if (formValue.password?.trim()) {
        processedData.password = formValue.password.trim();
      } else {
        delete processedData.password;
      }
      delete processedData.confirmPassword;
    }

    return processedData;
  }

  private async handleUserCreationSuccess(createdUser: User): Promise<void> {
    this.currentUser.set(createdUser);
    this.wasSaved.set(true);
    this.feedbackService.showSuccess('User created successfully!');

    const personId = this.targetPersonId();
    if (personId && createdUser.id) {
      await this.associateUserWithPerson(personId, createdUser.id);
    } else {
      this.navigateAfterDelay(['/admin/users'], 500);
    }
  }

  private async associateUserWithPerson(personId: string, userId: string): Promise<void> {
    try {
      this.isLoading.set(true);
      this.feedbackService.showSuccess('Associating user with person...');

      await firstValueFrom(this.personService.associateUser(personId, userId));
      this.feedbackService.showSuccess('User created and associated with person successfully!');

      const returnUrl = this.returnUrl();
      if (returnUrl) {
        this.navigateAfterDelay(returnUrl, 1000);
      } else {
        this.navigateAfterDelay(['/admin/users'], 1000);
      }
    } catch (error: any) {
      console.error('Error associating user with person:', error);
      this.feedbackService.showWarning('User created successfully, but failed to associate with person. You can associate manually later.');
      this.navigateAfterDelay(['/admin/users'], 1000);
    } finally {
      this.isLoading.set(false);
    }
  }

  private navigateAfterDelay(route: string | string[], delay: number): void {
    setTimeout(() => {
      if (typeof route === 'string') {
        this.router.navigateByUrl(route);
      } else {
        this.router.navigate(route);
      }
    }, delay);
  }

  private handleUserUpdateSuccess(updatedUser: User): void {
    this.currentUser.set(updatedUser);
    this.setFormValues(updatedUser);
    this.wasSaved.set(true);
    this.feedbackService.showSuccess('User updated successfully!');
  }

  private resetFormToOriginalValues(): void {
    this.setFormValues(this.currentUser()!);
  }
}
