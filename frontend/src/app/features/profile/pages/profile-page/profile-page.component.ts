import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '@auth/services/auth.service';
import { UserService } from '@users/services/user.service';
import { PersonService } from '@person/services/person.service';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { FormUtilsService } from '@shared/services/form-utils.service';
import { PasswordUtilsService } from '@shared/services/password-utils.service';
import { FeedbackMessageComponent } from '@shared/components/feedback-message/feedback-message.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { UserFormComponent } from '@users/components/user-form/user-form.component';
import { PersonFormComponent } from '@person/components/person-form/person-form.component';
import { PersonInfoCardComponent } from '@person/components/person-info-card/person-info-card.component';
import { IconComponent } from '@shared/components/icon/icon.component';
import { User } from '@users/interfaces/user.interface';
import { Person } from '@person/interfaces/person.interface';
import { firstValueFrom } from 'rxjs';

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
  private userService = inject(UserService);
  private personService = inject(PersonService);
  private feedbackService = inject(FeedbackMessageService);
  private formUtils = inject(FormUtilsService);
  private passwordUtils = inject(PasswordUtilsService);
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
      const user = await firstValueFrom(this.userService.getUserById(authUser.id));
      this.currentUser.set(user);
      this.setUserFormValues(user);
      this.loadAssociatedPerson();
    } catch (error) {
      this.feedbackService.showError('Failed to load user profile');
    } finally {
      this.isLoadingUser.set(false);
    }
  }

  private async loadAssociatedPerson() {
    const user = this.currentUser();
    if (!user) return;

    this.isLoadingPerson.set(true);
    try {
      const searchOptions = { page: 0, size: 1000, sort: 'id', order: 'asc' as const, search: '' };
      const personsResponse = await firstValueFrom(this.personService.getPersons(searchOptions));
      const associatedPerson = personsResponse.data.find(person => person.userId === user.id);
      this.associatedPerson.set(associatedPerson || null);
      if (associatedPerson) {
        this.setPersonFormValues(associatedPerson);
      }
    } catch (error) {
      this.associatedPerson.set(null);
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
      const formValue = this.userForm.value;
      const changes = this.detectUserChanges(formValue, this.currentUser()!);

      if (Object.keys(changes).length === 0) {
        this.feedbackService.showWarning('No changes detected. Nothing to save.');
        return;
      }

      const updatedUser: User = await firstValueFrom(
        this.userService.updateUser(this.currentUser()!.id, changes)
      );

      if (updatedUser?.id) {
        this.currentUser.set(updatedUser);
        this.authService.updateCurrentUser(updatedUser);
        this.setUserFormValues(updatedUser);
        this.feedbackService.showSuccess('User profile updated successfully!');
      }
    } catch (error) {
      this.handleUserError(error);
    } finally {
      this.isLoadingUser.set(false);
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

      if (this.associatedPerson()) {
        // Update existing person
        const updateData = {
          ...formValue,
          userId: this.currentUser()!.id,
          birthDate: formValue.birthDate ? new Date(formValue.birthDate).toISOString() : undefined
        };

        const updatedPerson = await firstValueFrom(
          this.personService.updatePerson(this.associatedPerson()!.id, updateData)
        );
        this.associatedPerson.set(updatedPerson);
        this.setPersonFormValues(updatedPerson);
        this.feedbackService.showSuccess('Personal information updated successfully!');
      } else {
        // Create new person
        const createData = {
          identType: formValue.identType!,
          identNumber: formValue.identNumber!,
          firstName: formValue.firstName!,
          lastName: formValue.lastName!,
          email: formValue.email!,
          mobile: formValue.mobile!,
          address: formValue.address!,
          city: formValue.city!,
          country: formValue.country!,
          postalCode: formValue.postalCode!,
          birthDate: formValue.birthDate ? new Date(formValue.birthDate).toISOString() : undefined,
          gender: formValue.gender!,
          nationality: formValue.nationality!,
          notes: formValue.notes || '',
          isActive: formValue.isActive!
        };

        const newPerson = await firstValueFrom(
          this.personService.createPerson(createData)
        );
        this.associatedPerson.set(newPerson);
        this.setPersonFormValues(newPerson);
        this.feedbackService.showSuccess('Personal information created successfully!');
      }
    } catch (error) {
      this.handlePersonError(error);
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

    // Check role changes
    const currentRole = originalUser.roles?.[0] || '';
    const newRole = formValue.selectedRole || '';
    if (newRole !== currentRole) {
      changes.roles = newRole ? [newRole] : [];
    }

    // Check password changes (only if provided)
    if (formValue.password?.trim()) {
      changes.password = formValue.password.trim();
    }

    return changes;
  }

  private handleUserError(error: any): void {
    let errorMessage = 'An error occurred while saving the user. Please try again.';

    if (error?.errorResponse?.message) {
      errorMessage = error.errorResponse.message;
    } else if (error?.message) {
      errorMessage = error.message;
    }

    this.feedbackService.showError(errorMessage);
  }

  private handlePersonError(error: any): void {
    let errorMessage = 'An error occurred while saving the person. Please try again.';

    if (error?.errorResponse?.message) {
      errorMessage = error.errorResponse.message;
    } else if (error?.message) {
      errorMessage = error.message;
    }

    this.feedbackService.showError(errorMessage);
  }
}
