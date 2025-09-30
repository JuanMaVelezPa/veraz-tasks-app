import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '@auth/services/auth.service';
import { ProfileService } from '@profile/services/profile.service';
import { PersonManagementService } from '@person/services/person-management.service';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { FormBuildersManagerService } from '@shared/services/form-builders-manager.service';
import { HttpErrorService } from '@shared/services/http-error.service';
import { ScrollService } from '@shared/services/scroll.service';

import { FeedbackMessageComponent } from '@shared/components/feedback-message/feedback-message.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { PersonalInfoTabComponent } from '@person/components/personal-info-tab/personal-info-tab.component';
import { EmployeeTabComponent } from '@employee/components/employee-tab/employee-tab.component';
import { ClientTabComponent } from '@client/components/client-tab/client-tab.component';
import { UserTabComponent } from '@users/components/user-tab/user-tab.component';
import { IconComponent } from '@shared/components/icon/icon.component';
import { User } from '@users/interfaces/user.interface';
import { Person } from '@person/interfaces/person.interface';
import { Employee } from '@employee/interfaces/employee.interface';
import { Client } from '@client/interfaces/client.interface';
import { firstValueFrom, catchError } from 'rxjs';
import { TabsComponent } from '@shared/components/tabs/tabs.component';
import { TabItem } from '@shared/interfaces/tab.interface';
import { TabsService } from '@shared/services/tabs.service';

@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FeedbackMessageComponent,
    LoadingComponent,
    PersonalInfoTabComponent,
    EmployeeTabComponent,
    ClientTabComponent,
    UserTabComponent,
    IconComponent,
    TabsComponent
  ],
  templateUrl: './profile-page.component.html'
})
export class ProfilePageComponent implements OnInit {
  private authService = inject(AuthService);
  private profileService = inject(ProfileService);
  private personManagementService = inject(PersonManagementService);
  private feedbackService = inject(FeedbackMessageService);
  private formBuilders = inject(FormBuildersManagerService);
  private httpErrorService = inject(HttpErrorService);
  private scrollService = inject(ScrollService);
  private tabsService = inject(TabsService);

  private router = inject(Router);

  currentUser = signal<User | null>(null);
  personalProfile = signal<Person | null>(null);
  employmentProfile = signal<Employee | null>(null);
  clientProfile = signal<Client | null>(null);
  isLoadingUser = signal(false);
  isLoadingPerson = signal(false);
  isLoadingEmployment = signal(false);
  isLoadingClient = signal(false);
  activeTab = signal<'user' | 'person' | 'employment' | 'client'>('user');
  tabs = signal<TabItem[]>([]);

  userForm = this.formBuilders.buildUserForm({ includePasswordValidation: true });
  personForm = this.formBuilders.buildPersonForm();
  employmentForm = this.formBuilders.buildEmployeeForm();
  clientForm = this.formBuilders.buildClientForm();

  ngOnInit() {
    this.feedbackService.clearMessage();
    this.loadCurrentUser();
    this.initializeTabs();
  }

  private initializeTabs(): void {
    const context = {
      type: 'profile' as const,
      hasPersonalProfile: !!this.personalProfile(),
      hasEmploymentProfile: !!this.employmentProfile(),
      hasClientProfile: !!this.clientProfile()
    };

    const tabs = this.tabsService.initializeTabs(context);
    this.tabs.set(tabs);
  }

  private updateTabs(): void {
    const context = {
      type: 'profile' as const,
      hasPersonalProfile: !!this.personalProfile(),
      hasEmploymentProfile: !!this.employmentProfile(),
      hasClientProfile: !!this.clientProfile()
    };

    const tabs = this.tabsService.initializeTabs(context);
    this.tabs.set(tabs);
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
      const user = await firstValueFrom(
        this.profileService.getMyUserAccount().pipe(
          catchError(error => this.httpErrorService.handleError(error, 'loading user profile'))
        )
      );
      this.currentUser.set(user);
      this.setUserFormValues(user);
      this.loadPersonalProfile();
      this.updateTabs();
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'Failed to load user profile');
    } finally {
      this.isLoadingUser.set(false);
    }
  }

  private async loadPersonalProfile() {
    const user = this.currentUser();
    if (!user) return;

    this.isLoadingPerson.set(true);
    try {
      const person = await firstValueFrom(
        this.profileService.getMyProfile()
      );
      this.personalProfile.set(person);
      if (person) {
        this.setPersonFormValues(person);
        // Load employment info if person exists
        this.loadEmploymentProfile(person.id);
        // Load client info if person exists
        this.loadClientProfile(person.id);
      }
    } catch (error: any) {
      this.personalProfile.set(null);
      this.feedbackService.showError(error.message || 'Failed to load personal information.');
    } finally {
      this.isLoadingPerson.set(false);
      this.updateTabs();
    }
  }

  private async loadEmploymentProfile(personId: string) {
    if (!personId) return;

    this.isLoadingEmployment.set(true);
    try {
      const employee = await firstValueFrom(
        this.profileService.getMyEmployee()
      );
      this.employmentProfile.set(employee);
      if (employee) {
        this.setEmploymentFormValues(employee);
      }
    } catch (error: any) {
      this.employmentProfile.set(null);
    } finally {
      this.isLoadingEmployment.set(false);
      this.updateTabs();
    }
  }

  private async loadClientProfile(personId: string) {
    if (!personId) return;

    this.isLoadingClient.set(true);
    try {
      const client = await firstValueFrom(
        this.profileService.getMyClient()
      );
      this.clientProfile.set(client);
      if (client) {
        this.setClientFormValues(client);
      }
    } catch (error: any) {
      this.clientProfile.set(null);
    } finally {
      this.isLoadingClient.set(false);
      this.updateTabs();
    }
  }

  private setUserFormValues(user: User) {
    const userData = {
      ...user,
      selectedRoles: user.roles || [],
      password: '',
      confirmPassword: ''
    };

    this.formBuilders.patchForm(this.userForm, userData);
  }

  private setPersonFormValues(person: Person) {
    this.formBuilders.patchForm(this.personForm, person);
  }

  private setEmploymentFormValues(employee: Employee) {
    this.formBuilders.patchForm(this.employmentForm, employee);
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
      this.setUserFormValues(this.currentUser()!);
    } finally {
      this.isLoadingUser.set(false);
    }
  }

  private async updateUser(): Promise<void> {
    const formValue = this.userForm.value;
    const changes = this.formBuilders.detectUserChanges(formValue, this.currentUser()!, {
      includeRoles: false,
      includePassword: true
    });

    if (Object.keys(changes).length === 0) {
      this.feedbackService.showWarning('No changes detected. Nothing to save.');
      return;
    }

    const hasLoginChanges = changes.username || changes.email;

    const updatedUser = await firstValueFrom(
      this.profileService.updateMyUserAccount(changes).pipe(
        catchError(error => this.httpErrorService.handleError(error, 'updating user profile'))
      )
    );

    if (updatedUser?.id) {
      this.currentUser.set(updatedUser);
      await firstValueFrom(this.authService.updateCurrentUserAndRefreshIfNeeded(updatedUser));
      this.setUserFormValues(updatedUser);
      await this.loadPersonalProfile();
      this.feedbackService.showSuccess('User profile updated successfully!');
      this.scrollService.scrollToTop();
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
      const formData = this.formBuilders.preparePersonFormData(formValue);

      if (this.personalProfile()) {
        await this.personManagementService.updatePerson(
          this.personalProfile()!,
          formData,
          {
            context: 'profile',
            onSuccess: (person) => {
              this.personalProfile.set(person);
              this.setPersonFormValues(person);
              this.scrollService.scrollToTop();
            },
            onError: (error) => {
              this.feedbackService.showError(error.message || 'An error occurred while updating personal information.');
              this.setPersonFormValues(this.personalProfile()!);
            }
          }
        );
      } else {
        const validation = this.formBuilders.validateRequiredPersonFields(formData);
        if (!validation.isValid) {
          this.feedbackService.showError(`Please fill in all required fields: ${validation.missingFields.join(', ')}`);
          return;
        }

        await this.personManagementService.createPerson(
          formData,
          {
            context: 'profile',
            onSuccess: (person) => {
              this.personalProfile.set(person);
              this.setPersonFormValues(person);
              this.scrollService.scrollToTop();
            },
            onError: (error) => {
              this.feedbackService.showError(error.message || 'An error occurred while creating personal information.');
            }
          }
        );
      }
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while saving personal information.');
      if (this.personalProfile()) {
        this.setPersonFormValues(this.personalProfile()!);
      }
    } finally {
      this.isLoadingPerson.set(false);
    }
  }

  onRolesSelected(roles: string[]) {
    const control = this.userForm.get('selectedRoles');
    control?.setValue(roles);
    control?.markAsTouched();
  }

  onPersonUpdated(updatedPerson: Person) {
    this.personalProfile.set(updatedPerson);
    this.setPersonFormValues(updatedPerson);
    this.feedbackService.showSuccess('Personal information updated successfully!');
    this.scrollService.scrollToTop();
    this.updateTabs();
  }

  onPersonCreated(newPerson: Person) {
    this.personalProfile.set(newPerson);
    this.setPersonFormValues(newPerson);
    this.feedbackService.showSuccess('Personal information created successfully!');
    this.scrollService.scrollToTop();
    this.updateTabs();
  }

  async onEmploymentFormSubmitted() {
    if (this.employmentForm.invalid) {
      this.employmentForm.markAllAsTouched();
      this.feedbackService.showError('Please fix the validation errors before saving.');
      return;
    }

    this.isLoadingEmployment.set(true);

    try {
      const formValue = this.employmentForm.value;
      const formData = this.formBuilders.prepareEmployeeFormData(formValue);

      if (this.employmentProfile()) {
        const updatedEmployee = await firstValueFrom(
          this.profileService.updateMyEmployee(formData).pipe(
            catchError(error => this.httpErrorService.handleError(error, 'updating employment information'))
          )
        );
        this.employmentProfile.set(updatedEmployee);
        this.setEmploymentFormValues(updatedEmployee);
        this.feedbackService.showSuccess('Employment information updated successfully!');
        this.scrollService.scrollToTop();
        this.updateTabs();
      } else {
        const person = this.personalProfile();
        if (!person) {
          this.feedbackService.showError('Personal information must be completed first.');
          return;
        }

        const employeeData = { ...formData, personId: person.id };
        const newEmployee = await firstValueFrom(
          this.profileService.createMyEmployee(employeeData).pipe(
            catchError(error => this.httpErrorService.handleError(error, 'creating employment information'))
          )
        );
        this.employmentProfile.set(newEmployee);
        this.setEmploymentFormValues(newEmployee);
        this.feedbackService.showSuccess('Employment information created successfully!');
        this.scrollService.scrollToTop();
        this.updateTabs();
      }
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while saving employment information.');
      if (this.employmentProfile()) {
        this.setEmploymentFormValues(this.employmentProfile()!);
      }
    } finally {
      this.isLoadingEmployment.set(false);
    }
  }

  setActiveTab(tab: string) {
    this.activeTab.set(tab as 'user' | 'person' | 'employment' | 'client');

    if (tab === 'person' && !this.personalProfile()) {
      this.formBuilders.resetForm(this.personForm);
    }

    if (tab === 'employment' && !this.employmentProfile()) {
      this.formBuilders.resetForm(this.employmentForm);
    }

    if (tab === 'client' && !this.clientProfile()) {
      this.formBuilders.resetForm(this.clientForm);
    }
    this.scrollService.scrollToTop();
  }

  async onClientFormSubmitted() {
    if (this.clientForm.invalid) {
      this.clientForm.markAllAsTouched();
      this.feedbackService.showError('Please fix the validation errors before saving.');
      return;
    }

    this.isLoadingClient.set(true);

    try {
      const formValue = this.clientForm.value;
      const formData = this.formBuilders.prepareClientFormData(formValue);

      if (this.clientProfile()) {
        const updatedClient = await firstValueFrom(
          this.profileService.updateMyClient(formData).pipe(
            catchError(error => this.httpErrorService.handleError(error, 'updating client information'))
          )
        );
        this.clientProfile.set(updatedClient);
        this.setClientFormValues(updatedClient);
        this.feedbackService.showSuccess('Client information updated successfully!');
        this.scrollService.scrollToTop();
        this.updateTabs();
      } else {
        const person = this.personalProfile();
        if (!person) {
          this.feedbackService.showError('Personal information must be completed first.');
          return;
        }

        const clientData = { ...formData, personId: person.id };
        const newClient = await firstValueFrom(
          this.profileService.createMyClient(clientData).pipe(
            catchError(error => this.httpErrorService.handleError(error, 'creating client information'))
          )
        );
        this.clientProfile.set(newClient);
        this.setClientFormValues(newClient);
        this.feedbackService.showSuccess('Client information created successfully!');
        this.scrollService.scrollToTop();
        this.updateTabs();
      }
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while saving client information.');
      if (this.clientProfile()) {
        this.setClientFormValues(this.clientProfile()!);
      }
    } finally {
      this.isLoadingClient.set(false);
    }
  }

  private setClientFormValues(client: Client) {
    this.clientForm.patchValue({
      personId: client.personId,
      type: client.type,
      category: client.category,
      source: client.source,
      companyName: client.companyName,
      companyWebsite: client.companyWebsite,
      companyIndustry: client.companyIndustry,
      contactPerson: client.contactPerson,
      contactPosition: client.contactPosition,
      address: client.address,
      city: client.city,
      country: client.country,
      postalCode: client.postalCode,
      taxId: client.taxId,
      creditLimit: client.creditLimit,
      currency: client.currency,
      paymentTerms: client.paymentTerms,
      paymentMethod: client.paymentMethod,
      notes: client.notes,
      preferences: client.preferences,
      tags: client.tags,
      rating: client.rating,
      status: client.status,
      isActive: client.isActive
    });
  }

}
