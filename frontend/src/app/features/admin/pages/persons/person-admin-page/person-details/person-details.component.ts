import { Component, inject, input, signal, OnDestroy, ChangeDetectorRef, OnInit } from '@angular/core';
import { ScrollService } from '@shared/services/scroll.service';
import { Person } from '@person/interfaces/person.interface';
import { Employee } from '@employee/interfaces/employee.interface';
import { Client } from '@client/interfaces/client.interface';
import { User } from '@users/interfaces/user.interface';
import { ReactiveFormsModule } from '@angular/forms';
import { PersonService } from '@person/services/person.service';
import { PersonManagementService } from '@person/services/person-management.service';
import { EmployeeAssociationService } from '@employee/services/employee-association.service';
import { ClientService } from '@client/services/client.service';
import { UserService } from '@users/services/user.service';

import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FeedbackMessageComponent } from '@shared/components/feedback-message/feedback-message.component';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { NavigationHistoryService } from '@shared/services/navigation-history.service';
import { HttpErrorService } from '@shared/services/http-error.service';
import { TimestampInfoComponent } from '@shared/components/timestamp-info/timestamp-info.component';
import { IconComponent } from '@shared/components/icon/icon.component';
import { firstValueFrom, catchError, filter } from 'rxjs';
import { FormBuildersManagerService } from '@shared/services/form-builders-manager.service';
import { EmployeeService } from '@employee/services/employee.service';
import { TabsComponent } from '@shared/components/tabs/tabs.component';
import { TabItem } from '@shared/interfaces/tab.interface';
import { TabsService } from '@shared/services/tabs.service';
import { PersonalInfoTabComponent } from '@person/components/personal-info-tab/personal-info-tab.component';
import { EmployeeTabComponent } from '@employee/components/employee-tab/employee-tab.component';
import { ClientTabComponent } from '@client/components/client-tab/client-tab.component';
import { UserTabComponent } from '@users/components/user-tab/user-tab.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';

@Component({
  selector: 'person-details',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, FeedbackMessageComponent,
    TimestampInfoComponent, IconComponent, TabsComponent,
    PersonalInfoTabComponent, EmployeeTabComponent, ClientTabComponent, UserTabComponent,
    LoadingComponent],
  templateUrl: './person-details.component.html',
})
export class PersonDetailsComponent implements OnInit, OnDestroy {
  person = input.required<Person>();

  private formBuilders = inject(FormBuildersManagerService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private personService = inject(PersonService);
  private personManagementService = inject(PersonManagementService);
  private employeeAssociationService = inject(EmployeeAssociationService);
  private clientService = inject(ClientService);
  private userService = inject(UserService);

  private httpErrorService = inject(HttpErrorService);
  private feedbackService = inject(FeedbackMessageService);
  private navigationHistory = inject(NavigationHistoryService);
  private cdr = inject(ChangeDetectorRef);
  private employeeService = inject(EmployeeService);
  private scrollService = inject(ScrollService);
  private tabsService = inject(TabsService);

  wasSaved = signal(false);
  isLoading = signal(false);
  isEditMode = signal(false);
  showDeleteModal = signal(false);
  currentPerson = signal<Person | null>(null);
  currentEmployee = signal<Employee | null>(null);
  isLoadingEmployee = signal(false);
  currentClient = signal<Client | null>(null);
  isLoadingClient = signal(false);
  currentUser = signal<User | null>(null);
  isLoadingUser = signal(false);
  activeTab = signal<'person' | 'employee' | 'client' | 'user'>('person');
  tabs = signal<TabItem[]>([]);

  personForm = this.formBuilders.buildPersonForm();
  employeeForm = this.formBuilders.buildEmployeeForm();
  clientForm = this.formBuilders.buildClientForm();
  userForm = this.formBuilders.buildUserForm({ isEditMode: true });

  ngOnInit(): void {
    this.feedbackService.clearMessage();
    const person = this.person();
    this.currentPerson.set(person);
    this.setFormValues(person);
    this.isEditMode.set(person.id !== 'new');

    this.initializeTabs();

    if (this.isEditMode()) {
      this.loadEmployeeInfo(person.id);
      this.loadClientInfo(person.id);
      this.loadUserInfo(person);

      const returnFromUser = this.route.snapshot.queryParamMap.get('returnFromUser');
      if (returnFromUser === 'true') {
        this.activeTab.set('user');
      }
    } else {
      this.formBuilders.resetForm(this.employeeForm);
    }

    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      filter(() => this.isEditMode() && this.currentPerson()?.id !== 'new')
    ).subscribe(async () => {
      try {
        const updatedPerson = await firstValueFrom(
          this.personService.getPersonById(this.currentPerson()!.id)
        );
        this.currentPerson.set(updatedPerson);

        if (updatedPerson.userId) {
          await this.loadUserInfo(updatedPerson);
        } else {
          this.currentUser.set(null);
          this.formBuilders.resetForm(this.userForm);
        }
      } catch (error) {
        console.error('Error refreshing person info:', error);
      }
    });
  }

  private setFormValues(person: Partial<Person>): void {
    this.formBuilders.patchForm(this.personForm, person);
    this.cdr.detectChanges();
  }

  private initializeTabs(): void {
    const context = {
      type: 'admin' as const,
      hasUserAccount: !!this.currentUser(),
      hasEmploymentProfile: !!this.currentEmployee(),
      hasClientProfile: !!this.currentClient(),
      isEditMode: this.isEditMode()
    };

    const tabs = this.tabsService.initializeTabs(context);
    this.tabs.set(tabs);
  }

  private updateTabs(): void {
    const context = {
      type: 'admin' as const,
      hasUserAccount: !!this.currentUser(),
      hasEmploymentProfile: !!this.currentEmployee(),
      hasClientProfile: !!this.currentClient(),
      isEditMode: this.isEditMode()
    };

    const tabs = this.tabsService.initializeTabs(context);
    this.tabs.set(tabs);
  }

  async submitForm(): Promise<void> {
    this.feedbackService.clearMessage();
    if (this.personForm.invalid) {
      this.personForm.markAllAsTouched();
      this.feedbackService.showError('Please fix validation errors before saving.');
      return;
    }
    this.isLoading.set(true);
    try {
      if (this.isEditMode()) {
        await this.updatePerson();
      } else {
        await this.createPerson();
      }
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while saving the person.');
      this.resetFormToOriginalValues();
    } finally {
      this.isLoading.set(false);
    }
  }

  private async createPerson(): Promise<void> {
    const formValue = this.personForm.value;
    const formData = this.formBuilders.preparePersonFormData(formValue);

    const validation = this.formBuilders.validateRequiredPersonFields(formData);
    if (!validation.isValid) {
      this.feedbackService.showError(`Please fill in all required fields: ${validation.missingFields.join(', ')}`);
      return;
    }

    const userId = this.route.snapshot.queryParamMap.get('userId');

    try {
      await this.personManagementService.createPerson(
        formData,
        {
          context: 'admin',
          userId: userId || undefined,
          onSuccess: (person) => {
            this.handlePersonCreationSuccess(person, userId);
          },
          onError: () => {
            this.resetFormToOriginalValues();
          }
        }
      );
    } catch (error: any) {
      this.resetFormToOriginalValues();
      throw error;
    }
  }

  private async updatePerson(): Promise<void> {
    const originalPerson = this.currentPerson();
    if (!originalPerson) return;

    const formValue = this.personForm.value;
    const formData = this.formBuilders.preparePersonFormData(formValue);

    try {
      await this.personManagementService.updatePerson(
        originalPerson,
        formData,
        {
          context: 'admin',
          onSuccess: (person) => {
            this.handlePersonUpdateSuccess(person);
          },
          onError: () => {
            this.resetFormToOriginalValues();
          }
        }
      );
    } catch (error: any) {
      this.resetFormToOriginalValues();
    }
  }

  async deletePerson(): Promise<void> {
    const person = this.currentPerson();
    if (!person || person.id === 'new') return;

    this.isLoading.set(true);
    this.feedbackService.clearMessage();

    try {
      await this.removeUserAssociation();

      await firstValueFrom(
        this.personService.deletePerson(person.id).pipe(
          catchError(error => this.httpErrorService.handleError(error, 'deleting person'))
        )
      );

      this.feedbackService.showSuccess('Person and employment information deleted successfully. User account has been disassociated.');
      this.navigationHistory.goBackToPersons();
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while deleting the person.');
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
    this.scrollService.scrollToTop();
  }

  goBack(): void {
    this.feedbackService.clearMessage();
    this.navigationHistory.goBack('/admin/persons');
  }

  private async loadEmployeeInfo(personId: string): Promise<void> {
    if (!personId || personId === 'undefined' || personId === 'null') {
      this.currentEmployee.set(null);
      this.isLoadingEmployee.set(false);
      return;
    }

    this.isLoadingEmployee.set(true);
    try {
      const employee = await firstValueFrom(
        this.employeeAssociationService.getEmployeeByPersonId(personId)
      );
      this.currentEmployee.set(employee);

      if (employee) {
        this.setEmployeeFormValues(employee);
      }
    } catch (error) {
      this.currentEmployee.set(null);
    } finally {
      this.isLoadingEmployee.set(false);
      this.updateTabs();
    }
  }

  private async loadClientInfo(personId: string): Promise<void> {
    if (!personId || personId === 'undefined' || personId === 'null') {
      this.currentClient.set(null);
      this.isLoadingClient.set(false);
      return;
    }

    this.isLoadingClient.set(true);
    try {
      const client = await firstValueFrom(
        this.clientService.getClientByPersonId(personId)
      );
      this.currentClient.set(client);
      if (client) {
        this.setClientFormValues(client);
      }
    } catch (error) {
      this.currentClient.set(null);
    } finally {
      this.isLoadingClient.set(false);
      this.updateTabs();
    }
  }

  private async loadUserInfo(person: Person): Promise<void> {
    if (!person.userId) {
      this.currentUser.set(null);
      this.isLoadingUser.set(false);
      return;
    }

    this.isLoadingUser.set(true);
    try {
      const user = await firstValueFrom(
        this.userService.getUserById(person.userId)
      );
      this.currentUser.set(user);
      // Actualizar el formulario con los datos del usuario
      this.formBuilders.patchForm(this.userForm, user);
    } catch (error) {
      console.error('Error fetching user:', error);
      this.currentUser.set(null);
    } finally {
      this.isLoadingUser.set(false);
      this.updateTabs();
    }
  }

  setActiveTab(tab: string) {
    this.activeTab.set(tab as 'person' | 'employee' | 'client' | 'user');
    if (tab === 'user' && this.currentUser()) {
      this.formBuilders.patchForm(this.userForm, this.currentUser());
    }
    this.scrollService.scrollToTop();
  }

  async submitEmployeeForm(): Promise<void> {
    if (this.employeeForm.invalid) {
      this.employeeForm.markAllAsTouched();
      this.feedbackService.showError('Please fix validation errors before saving.');
      return;
    }

    this.isLoadingEmployee.set(true);
    try {
      const employeeData = this.formBuilders.prepareEmployeeFormData(this.employeeForm.value);

      if (this.currentEmployee()) {
        await this.updateEmployeeData(employeeData);
      } else {
        await this.createEmployeeData(employeeData);
      }
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while saving the employee.');
    } finally {
      this.isLoadingEmployee.set(false);
    }
  }

  async submitClientForm(): Promise<void> {
    if (this.clientForm.invalid) {
      this.clientForm.markAllAsTouched();
      this.feedbackService.showError('Please fix validation errors before saving.');
      return;
    }

    this.isLoadingClient.set(true);
    try {
      const clientData = this.formBuilders.prepareClientFormData(this.clientForm.value);

      if (this.currentClient()) {
        await this.updateClientData(clientData);
      } else {
        await this.createClientData(clientData);
      }
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while saving the client.');
    } finally {
      this.isLoadingClient.set(false);
    }
  }

  async removeUserAssociation(): Promise<void> {
    const person = this.currentPerson();
    if (!person || person.id === 'new') return;

    this.isLoadingUser.set(true);
    try {
      if (person.userId) {
        try {

          await firstValueFrom(
            this.userService.updateUser(person.userId, { isActive: false }).pipe(
              catchError(error => this.httpErrorService.handleError(error, 'deactivating user'))
            )
          );

        } catch (userError) {
          this.feedbackService.showError('Failed to deactivate user');
        }
      }

      await firstValueFrom(
        this.personService.removeUserAssociation(person.id).pipe(
          catchError(error => this.httpErrorService.handleError(error, 'removing user association'))
        )
      );

      this.currentUser.set(null);
      this.formBuilders.resetForm(this.userForm);
      this.feedbackService.showSuccess('User association removed and user account deactivated successfully.');
      this.scrollService.scrollToTop();
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while removing user association.');
    } finally {
      this.isLoadingUser.set(false);
      this.updateTabs();
    }
  }

  linkExistingUser(): void {
    const person = this.currentPerson();
    if (!person || person.id === 'new') return;

    this.router.navigate(['/admin/users'], {
      queryParams: {
        mode: 'select',
        personId: person.id,
        personName: `${person.firstName} ${person.lastName}`,
        returnUrl: `/admin/persons/${person.id}?returnFromUser=true`,
        filter: 'available'
      }
    });
  }

  async associateUserFromTable(userId: string): Promise<void> {
    const person = this.currentPerson();
    if (!person || person.id === 'new') return;

    this.isLoadingUser.set(true);
    try {
      await firstValueFrom(
        this.personService.associateUser(person.id, userId).pipe(
          catchError(error => this.httpErrorService.handleError(error, 'associating user'))
        )
      );

      const updatedPerson = await firstValueFrom(
        this.personService.getPersonById(person.id)
      );
      this.currentPerson.set(updatedPerson);

      await this.loadUserInfo(updatedPerson);
      this.feedbackService.showSuccess('User associated successfully!');
      this.activeTab.set('user');
      this.scrollService.scrollToTop();
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while associating user.');
    } finally {
      this.isLoadingUser.set(false);
    }
  }

  createNewUser(): void {
    const person = this.currentPerson();
    if (!person || person.id === 'new') return;

    this.router.navigate(['/admin/users/new'], {
      queryParams: {
        personId: person.id,
        returnUrl: `/admin/persons/${person.id}?returnFromUser=true`
      }
    });
  }

  editUser(): void {
    const user = this.currentUser();
    if (!user) return;
    this.router.navigate(['/admin/users', user.id]);
  }

  onUserFormSubmitted(): void {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      this.feedbackService.showError('Please fix the validation errors before saving.');
      return;
    }
    this.updateUserInfo();
  }

  private async updateUserInfo(): Promise<void> {
    try {
      const user = this.currentUser();
      if (!user) {
        this.feedbackService.showError('No user associated with this person.');
        return;
      }

      this.isLoadingUser.set(true);

      const formValue = this.userForm.value;
      const changes = this.formBuilders.detectUserChanges(formValue, user, {
        includeRoles: true,
        includePassword: true
      });

      if (Object.keys(changes).length === 0) {
        this.feedbackService.showWarning('No changes detected. Nothing to save.');
        return;
      }

      const updatedUser = await firstValueFrom(
        this.userService.updateUser(user.id, changes).pipe(
          catchError(error => this.httpErrorService.handleError(error, 'updating user information'))
        )
      );

      if (updatedUser?.id) {
        this.currentUser.set(updatedUser);
        this.formBuilders.patchForm(this.userForm, updatedUser);
        this.feedbackService.showSuccess('User information updated successfully!');
        this.scrollService.scrollToTop();
        this.updateTabs();
      }
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while updating user information.');
      if (this.currentUser()) {
        this.formBuilders.patchForm(this.userForm, this.currentUser()!);
      }
    } finally {
      this.isLoadingUser.set(false);
    }
  }

  onRolesSelected(roles: string[]) {
    this.formBuilders.patchForm(this.userForm, { selectedRoles: roles });
    // Mark the field as touched to trigger change detection
    this.userForm.get('selectedRoles')?.markAsTouched();
  }

  ngOnDestroy(): void {
    this.feedbackService.clearMessage();
  }

  private handlePersonCreationSuccess(person: Person, userId: string | null): void {
    this.currentPerson.set(person);
    this.wasSaved.set(true);
    this.isEditMode.set(true);

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { id: person.id, mode: 'edit' },
      queryParamsHandling: 'merge'
    });

    if (person.id && person.id !== 'new') {
      this.loadEmployeeInfo(person.id);
      this.loadUserInfo(person);
    }

    this.feedbackService.showSuccess(
      'Person created successfully! Use the tabs above to add employment information or associate a user account.'
    );
    this.activeTab.set('person');
    this.cdr.detectChanges();
    this.scrollService.scrollToTop();
  }

  private handlePersonUpdateSuccess(person: Person): void {
    this.currentPerson.set(person);
    this.setFormValues(person);
    if (person.userId && this.currentUser()) {
      this.formBuilders.patchForm(this.userForm, this.currentUser());
    }
    this.wasSaved.set(true);
    this.scrollService.scrollToTop();
  }

  private resetFormToOriginalValues(): void {
    this.setFormValues(this.currentPerson() || this.person());
  }

  private setEmployeeFormValues(employee: Employee): void {
    this.formBuilders.patchForm(this.employeeForm, employee);
    this.cdr.detectChanges();
  }

  private setClientFormValues(client: Client): void {
    this.formBuilders.patchForm(this.clientForm, client);
    this.cdr.detectChanges();
  }

  private async createEmployeeData(formData: any): Promise<void> {
    const person = this.currentPerson();
    if (!person || person.id === 'new') return;

    const employeeData = { ...formData, personId: person.id };
    const createdEmployee = await firstValueFrom(
      this.employeeService.createEmployee(employeeData).pipe(
        catchError(error => this.httpErrorService.handleError(error, 'creating employee'))
      )
    );

    this.currentEmployee.set(createdEmployee);
    this.setEmployeeFormValues(createdEmployee);
    this.updateTabs();
    this.feedbackService.showSuccess('Employment information created successfully!');
    this.scrollService.scrollToTop();
  }

  private async updateEmployeeData(formData: any): Promise<void> {
    const employee = this.currentEmployee();
    if (!employee) return;

    const updatedEmployee = await firstValueFrom(
      this.employeeService.updateEmployee(employee.id, formData).pipe(
        catchError(error => this.httpErrorService.handleError(error, 'updating employee'))
      )
    );

    this.currentEmployee.set(updatedEmployee);
    this.setEmployeeFormValues(updatedEmployee);
    this.updateTabs();
    this.feedbackService.showSuccess('Employment information updated successfully!');
    this.scrollService.scrollToTop();
  }

  private async createClientData(formData: any): Promise<void> {
    const person = this.currentPerson();
    if (!person || person.id === 'new') return;

    const clientData = { ...formData, personId: person.id };
    const createdClient = await firstValueFrom(
      this.clientService.createClient(clientData).pipe(
        catchError(error => this.httpErrorService.handleError(error, 'creating client'))
      )
    );

    this.currentClient.set(createdClient);
    this.setClientFormValues(createdClient);
    this.updateTabs();
    this.feedbackService.showSuccess('Client information created successfully!');
    this.scrollService.scrollToTop();
  }

  private async updateClientData(formData: any): Promise<void> {
    const client = this.currentClient();
    if (!client) return;

    const updatedClient = await firstValueFrom(
      this.clientService.updateClient(client.id, formData).pipe(
        catchError(error => this.httpErrorService.handleError(error, 'updating client'))
      )
    );

    this.currentClient.set(updatedClient);
    this.setClientFormValues(updatedClient);
    this.updateTabs();
    this.feedbackService.showSuccess('Client information updated successfully!');
    this.scrollService.scrollToTop();
  }



}
