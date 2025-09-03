import { Component, inject, input, signal, OnDestroy, ChangeDetectorRef, OnInit } from '@angular/core';
import { ScrollService } from '@shared/services/scroll.service';
import { Person } from '@person/interfaces/person.interface';
import { Employee } from '@employee/interfaces/employee.interface';
import { User } from '@users/interfaces/user.interface';
import { ReactiveFormsModule } from '@angular/forms';
import { PersonService } from '@person/services/person.service';
import { PersonManagementService } from '@person/services/person-management.service';
import { EmployeeAssociationService } from '@employee/services/employee-association.service';
import { UserService } from '@users/services/user.service';

import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FeedbackMessageComponent } from '@shared/components/feedback-message/feedback-message.component';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { NavigationHistoryService } from '@shared/services/navigation-history.service';
import { HttpErrorService } from '@shared/services/http-error.service';
import { PersonFormComponent } from '@person/components/person-form/person-form.component';
import { TimestampInfoComponent } from '@shared/components/timestamp-info/timestamp-info.component';
import { EmployeeFormComponent } from '@employee/components/employee-form/employee-form.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { IconComponent } from '@shared/components/icon/icon.component';
import { firstValueFrom, catchError, filter } from 'rxjs';
import { FormBuildersManagerService } from '@shared/services/form-builders-manager.service';
import { EmployeeService } from '@employee/services/employee.service';

@Component({
  selector: 'person-details',
  imports: [ReactiveFormsModule, CommonModule, FeedbackMessageComponent, PersonFormComponent, TimestampInfoComponent, EmployeeFormComponent, LoadingComponent, IconComponent],
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
  private userService = inject(UserService);

  private httpErrorService = inject(HttpErrorService);
  private feedbackService = inject(FeedbackMessageService);
  private navigationHistory = inject(NavigationHistoryService);
  private cdr = inject(ChangeDetectorRef);
  private employeeService = inject(EmployeeService);
  private scrollService = inject(ScrollService);

  wasSaved = signal(false);
  isLoading = signal(false);
  isEditMode = signal(false);
  showDeleteModal = signal(false);
  currentPerson = signal<Person | null>(null);

  currentEmployee = signal<Employee | null>(null);
  isLoadingEmployee = signal(false);

  currentUser = signal<User | null>(null);
  isLoadingUser = signal(false);

  activeTab = signal<'person' | 'employee' | 'user'>('person');

  personForm = this.formBuilders.buildPersonForm();
  employeeForm = this.formBuilders.buildEmployeeForm();

  ngOnInit(): void {
    this.feedbackService.clearMessage();
    const person = this.person();
    this.currentPerson.set(person);
    this.setFormValues(person);
    this.isEditMode.set(person.id !== 'new');

    if (this.isEditMode()) {
      this.loadEmployeeInfo(person.id);
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
      const createdPerson = await this.personManagementService.createPerson(
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
    } catch (error) {
      this.currentEmployee.set(null);
    } finally {
      this.isLoadingEmployee.set(false);
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
    } catch (error) {
      console.error('Error fetching user:', error);
      this.currentUser.set(null);
    } finally {
      this.isLoadingUser.set(false);
    }
  }

  setActiveTab(tab: 'person' | 'employee' | 'user') {
    this.activeTab.set(tab);
    this.scrollService.scrollToTop();
  }

  getTabClasses(tab: 'person' | 'employee' | 'user'): string {
    const isActive = this.activeTab() === tab;
    const isDisabled = (tab === 'user' || tab === 'employee') && !this.isEditMode();

    const baseClasses = 'tab tab-sm sm:tab-md font-medium transition-all duration-200 rounded-t-lg border-2 flex-1 sm:flex-none min-w-0';
    const activeClasses = isActive ? 'tab-active bg-primary text-primary-content border-primary' : 'border-base-300';
    const disabledClasses = isDisabled ? 'opacity-50 cursor-not-allowed' : '';

    return `${baseClasses} ${activeClasses} ${disabledClasses}`.trim();
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



  async removeUserAssociation(): Promise<void> {
    const person = this.currentPerson();
    if (!person || person.id === 'new') return;

    this.isLoadingUser.set(true);
    try {
      await firstValueFrom(
        this.personService.removeUserAssociation(person.id).pipe(
          catchError(error => this.httpErrorService.handleError(error, 'removing user association'))
        )
      );

      this.currentUser.set(null);
      this.feedbackService.showSuccess('User association removed successfully.');
      this.scrollService.scrollToTop();
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while removing user association.');
    } finally {
      this.isLoadingUser.set(false);
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
    this.feedbackService.showSuccess('Employment information updated successfully!');
    this.scrollService.scrollToTop();
  }

}
