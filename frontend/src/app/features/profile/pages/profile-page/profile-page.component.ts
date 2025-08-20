import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '@auth/services/auth.service';
import { ProfileService } from '@profile/services/profile.service';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { FormUtilsService } from '@shared/services/form-utils.service';
import { PasswordUtilsService } from '@shared/services/password-utils.service';
import { HttpErrorService } from '@shared/services/http-error.service';
import { PersonManagementService } from '@person/services/person-management.service';
import { FeedbackMessageComponent } from '@shared/components/feedback-message/feedback-message.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { UserFormComponent } from '@users/components/user-form/user-form.component';
import { PersonFormComponent } from '@person/components/person-form/person-form.component';
import { PersonInfoCardComponent } from '@person/components/person-info-card/person-info-card.component';
import { IconComponent } from '@shared/components/icon/icon.component';
import { User } from '@users/interfaces/user.interface';
import { Person } from '@person/interfaces/person.interface';
import { firstValueFrom, catchError, tap } from 'rxjs';

@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FeedbackMessageComponent,
    LoadingComponent,
    UserFormComponent,
    PersonFormComponent,
    PersonInfoCardComponent,
    IconComponent
  ],
  templateUrl: './profile-page.component.html'
})
export class ProfilePageComponent implements OnInit {
  private authService = inject(AuthService);
  private profileService = inject(ProfileService);
  private feedbackService = inject(FeedbackMessageService);
  private formUtils = inject(FormUtilsService);
  private passwordUtils = inject(PasswordUtilsService);
  private httpErrorService = inject(HttpErrorService);
  private personManagementService = inject(PersonManagementService);
  private fb = inject(FormBuilder);
  private router = inject(Router);

  // Signals
  currentUser = signal<User | null>(null);
  associatedPerson = signal<Person | null>(null);
  isLoadingUser = signal(false);
  isLoadingPerson = signal(false);
  activeTab = signal<'user' | 'person'>('user');

  // Forms
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
    selectedRole: ['' as string, [Validators.required]],
  }, {
    validators: [
      FormUtilsService.isFieldOneEqualFieldTwo('password', 'confirmPassword')
    ]
  });

  personForm = this.fb.nonNullable.group({
    identType: ['', [Validators.required]],
    identNumber: ['', [Validators.required, Validators.minLength(3)]],
    firstName: ['', [Validators.required, Validators.minLength(2)]],
    lastName: ['', [Validators.required, Validators.minLength(2)]],
    birthDate: [''],
    gender: [''],
    nationality: [''],
    mobile: [''],
    email: ['', [Validators.pattern(FormUtilsService.emailPattern)]],
    address: [''],
    city: [''],
    country: [''],
    postalCode: [''],
    notes: [''],
    isActive: [true]
  });

  ngOnInit() {
    this.feedbackService.clearMessage();
    this.loadCurrentUser();
  }

  private async loadCurrentUser() {
    const authUser = this.authService.user();
    if (!authUser) {
      this.feedbackService.showError('User not authenticated');
      this.router.navigate(['/auth/sign-in']);
      return;
    }

    this.isLoadingUser.set(true);
    try {
      const response = await firstValueFrom(
        this.profileService.getMyUserAccount().pipe(
          catchError(error => this.httpErrorService.handleError(error, 'loading user profile'))
        )
      );
      this.currentUser.set(response.data);
      this.setUserFormValues(response.data);
      this.loadAssociatedPerson();
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'Failed to load user profile');
    } finally {
      this.isLoadingUser.set(false);
    }
  }

  private async loadAssociatedPerson() {
    const user = this.currentUser();
    if (!user) return;

    this.isLoadingPerson.set(true);
    try {
      const response = await firstValueFrom(
        this.profileService.getMyProfile().pipe(
          catchError(error => this.httpErrorService.handleError(error, 'loading person profile'))
        )
      );
      this.associatedPerson.set(response.data);
      if (response.data) {
        this.setPersonFormValues(response.data);
      }
    } catch (error: any) {
      this.associatedPerson.set(null);
      // Don't show error for profile not found, it's expected for new users
      if (error.status !== 404) {
        this.feedbackService.showError(error.message || 'Failed to load personal information.');
      }
    } finally {
      this.isLoadingPerson.set(false);
    }
  }

  private setUserFormValues(user: User) {
    const selectedRole = user.roles && user.roles.length > 0 ? user.roles[0] : '';

    this.userForm.patchValue({
      username: user.username || '',
      email: user.email || '',
      password: '',
      confirmPassword: '',
      isActive: user.isActive ?? true,
      selectedRole: selectedRole,
    });
  }

  private setPersonFormValues(person: Person) {
    this.personForm.patchValue({
      firstName: person.firstName || '',
      lastName: person.lastName || '',
      identNumber: person.identNumber || '',
      identType: person.identType || '',
      email: person.email || '',
      mobile: person.mobile || '',
      address: person.address || '',
      city: person.city || '',
      country: person.country || '',
      postalCode: person.postalCode || '',
      birthDate: person.birthDate ? new Date(person.birthDate).toISOString().split('T')[0] : '',
      gender: person.gender || '',
      nationality: person.nationality || '',
      notes: person.notes || '',
      isActive: person.isActive ?? true
    });
  }

  async onUserFormSubmitted() {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      this.feedbackService.showError('Please fix the validation errors before saving.');
      return;
    }

    this.isLoadingUser.set(true);

    try {
      await this.updateUser();
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while updating the user profile.');
      // Reset form to original values after error
      this.setUserFormValues(this.currentUser()!);
    } finally {
      this.isLoadingUser.set(false);
    }
  }

  private async updateUser(): Promise<void> {
    const formValue = this.userForm.value;
    const changes = this.detectUserChanges(formValue, this.currentUser()!);

    if (Object.keys(changes).length === 0) {
      this.feedbackService.showWarning('No changes detected. Nothing to save.');
      return;
    }

    const response = await firstValueFrom(
      this.profileService.updateMyUserAccount(changes).pipe(
        catchError(error => this.httpErrorService.handleError(error, 'updating user profile'))
      )
    );

    if (response.data?.id) {
      this.currentUser.set(response.data);
      this.authService.updateCurrentUser(response.data);
      this.setUserFormValues(response.data);
      this.feedbackService.showSuccess('User profile updated successfully!');
    }
  }

  async onPersonFormSubmitted() {
    if (this.personForm.invalid) {
      this.personForm.markAllAsTouched();
      this.feedbackService.showError('Please fix the validation errors before saving.');
      return;
    }

    this.isLoadingPerson.set(true);

    try {
      const formValue = this.personForm.value;
      const formData = this.personManagementService.prepareFormData(formValue);

      if (this.associatedPerson()) {
        // Update existing person
        await this.personManagementService.updatePerson(
          this.associatedPerson()!,
          formData,
          {
            context: 'profile',
            onSuccess: (person) => {
              this.associatedPerson.set(person);
              this.setPersonFormValues(person);
            },
            onError: () => {
              // Reset form to original values after error
              this.setPersonFormValues(this.associatedPerson()!);
            }
          }
        );
      } else {
        // Create new person
        if (!this.personManagementService.validateRequiredFields(formData)) {
          return;
        }

        await this.personManagementService.createPerson(
          formData,
          {
            context: 'profile',
            onSuccess: (person) => {
              this.associatedPerson.set(person);
              this.setPersonFormValues(person);
            }
          }
        );
      }
    } catch (error: any) {
      // Error handling is done in the service
      if (this.associatedPerson()) {
        this.setPersonFormValues(this.associatedPerson()!);
      }
    } finally {
      this.isLoadingPerson.set(false);
    }
  }

  onRoleSelected(roleName: string) {
    const control = this.userForm.get('selectedRole');
    control?.setValue(roleName);
  }

  onPersonUpdated(updatedPerson: Person) {
    this.associatedPerson.set(updatedPerson);
    this.setPersonFormValues(updatedPerson);
    this.feedbackService.showSuccess('Personal information updated successfully!');
  }

  onPersonCreated(newPerson: Person) {
    this.associatedPerson.set(newPerson);
    this.setPersonFormValues(newPerson);
    this.feedbackService.showSuccess('Personal information created successfully!');
  }

  setActiveTab(tab: 'user' | 'person') {
    this.activeTab.set(tab);

    // Clear person form when switching to person tab and no associated person exists
    if (tab === 'person' && !this.associatedPerson()) {
      this.personForm.reset({
        firstName: '',
        lastName: '',
        identNumber: '',
        identType: '',
        email: '',
        mobile: '',
        address: '',
        city: '',
        country: '',
        postalCode: '',
        birthDate: '',
        gender: '',
        nationality: '',
        notes: '',
        isActive: true
      });
    }
  }

  private detectUserChanges(formValue: any, originalUser: User): any {
    const changes: any = {};

    const compareStrings = (original: string | undefined, newValue: string | undefined) => {
      return (original?.trim() || '') !== (newValue?.trim() || '');
    };

    // Check username changes
    if (compareStrings(originalUser.username, formValue.username)) {
      changes.username = formValue.username?.trim().toLowerCase();
    }

    // Check email changes
    if (compareStrings(originalUser.email, formValue.email)) {
      changes.email = formValue.email?.trim().toLowerCase();
    }

    // Check isActive changes
    if (formValue.isActive !== originalUser.isActive) {
      changes.isActive = formValue.isActive;
    }

    // Note: Role changes are not allowed in profile mode
    // Only admins can change roles through the admin interface

    // Check password changes (only if provided)
    if (formValue.password?.trim()) {
      changes.password = formValue.password.trim();
    }

    return changes;
  }




}
