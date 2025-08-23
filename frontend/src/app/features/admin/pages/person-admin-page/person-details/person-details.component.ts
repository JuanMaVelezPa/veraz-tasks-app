import { Component, inject, input, signal, OnDestroy, ChangeDetectorRef, OnInit } from '@angular/core';
import { Person } from '@person/interfaces/person.interface';
import { Employee } from '@employee/interfaces/employee.interface';
import { ReactiveFormsModule } from '@angular/forms';
import { PersonService } from '@person/services/person.service';
import { PersonManagementService } from '@person/services/person-management.service';
import { EmployeeService } from '@employee/services/employee.service';
import { EmployeeAssociationService } from '@employee/services/employee-association.service';
import { Router, ActivatedRoute } from '@angular/router';
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
import { firstValueFrom, catchError } from 'rxjs';
import { FormBuildersManagerService } from '@shared/services/form-builders-manager.service';

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
  private employeeService = inject(EmployeeService);
  private employeeAssociationService = inject(EmployeeAssociationService);
  private httpErrorService = inject(HttpErrorService);
  private feedbackService = inject(FeedbackMessageService);
  private navigationHistory = inject(NavigationHistoryService);
  private cdr = inject(ChangeDetectorRef);

  wasSaved = signal(false);
  isLoading = signal(false);
  isEditMode = signal(false);
  showDeleteModal = signal(false);
  currentPerson = signal<Person | null>(null);

  // Employee signals
  currentEmployee = signal<Employee | null>(null);
  isLoadingEmployee = signal(false);

  // Tab management
  activeTab = signal<'person' | 'employee'>('person');

  // Forms
  personForm = this.formBuilders.buildPersonForm();
  employeeForm = this.formBuilders.buildEmployeeForm();

  ngOnInit(): void {
    this.feedbackService.clearMessage();
    const person = this.person();
    this.currentPerson.set(person);
    this.setFormValues(person);
    this.isEditMode.set(person.id !== 'new');

    // Load employment info if person exists and has a valid ID
    if (this.isEditMode() && person.id && person.id !== 'new' && person.id !== 'undefined' && person.id !== 'null') {
      this.loadEmployeeInfo(person.id);
    }

    // Reset employee form if no employee exists
    if (!this.currentEmployee()) {
      this.formBuilders.resetForm(this.employeeForm);
    }
  }

  private setFormValues(person: Partial<Person>): void {
    this.formBuilders.patchForm(this.personForm, person);
    this.cdr.detectChanges();
  }

  async onSubmit(): Promise<void> {
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
      // Reset form to original values after error
      this.setFormValues(this.currentPerson()!);
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

    // Check if we're creating a person for a specific user
    const userId = this.route.snapshot.queryParamMap.get('userId');

    try {
      const createdPerson = await this.personManagementService.createPerson(
        formData,
        {
          context: 'admin',
          userId: userId || undefined,
          onSuccess: (person) => {
            this.currentPerson.set(person);
            this.wasSaved.set(true);
            this.isEditMode.set(true);

            // Load employment info after person is created
            if (person.id && person.id !== 'new') {
              this.loadEmployeeInfo(person.id);
            }

            // Navigate back after success
            setTimeout(() => {
              if (userId) {
                this.navigationHistory.goBackToUser(userId);
              } else {
                this.navigationHistory.goBackToPersons();
              }
            }, 500);
          },
          onError: () => {
            // Reset form to original values after error
            this.setFormValues(this.currentPerson() || this.person());
          }
        }
      );
    } catch (error: any) {
      // Error handling is done in the service
      this.setFormValues(this.currentPerson() || this.person());
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
            this.currentPerson.set(person);
            this.setFormValues(person);
            this.wasSaved.set(true);
          },
          onError: () => {
            // Reset form to original values after error
            this.setFormValues(this.currentPerson()!);
          }
        }
      );
    } catch (error: any) {
      // Error handling is done in the service
      this.setFormValues(this.currentPerson()!);
    }
  }

  async deletePerson(): Promise<void> {
    const person = this.currentPerson();
    if (!person || person.id === 'new') return;

    this.isLoading.set(true);
    this.feedbackService.clearMessage();

    try {
      const response = await firstValueFrom(
        this.personService.deletePerson(person.id).pipe(
          catchError(error => this.httpErrorService.handleError(error, 'deleting person'))
        )
      );
      // For delete operations, response will be true if successful
      this.feedbackService.showSuccess('Person deleted successfully');

      // Check if we came from a user context
      const userId = this.route.snapshot.queryParamMap.get('userId');
      if (userId) {
        this.navigationHistory.goBackToUser(userId);
      } else {
        this.navigationHistory.goBackToPersons();
      }
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
  }

  goBack(): void {
    this.feedbackService.clearMessage();
    this.navigationHistory.goBack('/admin/persons');
  }

  // Employment methods
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

  setActiveTab(tab: 'person' | 'employee') {
    this.activeTab.set(tab);
  }

  async onEmployeeFormSubmitted(): Promise<void> {
    this.feedbackService.clearMessage();
    if (this.employeeForm.invalid) {
      this.employeeForm.markAllAsTouched();
      this.feedbackService.showError('Please fix validation errors before saving.');
      return;
    }

    this.isLoadingEmployee.set(true);
    try {
      const formValue = this.employeeForm.value;
      const formData = this.formBuilders.prepareEmployeeFormData(formValue);

      const person = this.currentPerson();
      if (!person || person.id === 'new') {
        this.feedbackService.showError('Person must be saved first before creating employment information.');
        return;
      }

      if (this.currentEmployee()) {
        // Update existing employee
        await this.updateEmployee(formData);
      } else {
        // Create new employee
        await this.createEmployee(formData);
      }
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while saving employment information.');
    } finally {
      this.isLoadingEmployee.set(false);
    }
  }

  private async createEmployee(formData: any): Promise<void> {
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
  }

  private async updateEmployee(formData: any): Promise<void> {
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
  }

  private setEmployeeFormValues(employee: Employee): void {
    this.formBuilders.patchForm(this.employeeForm, employee);
    this.cdr.detectChanges();
  }

  onEmployeeAction(): void {
    const person = this.currentPerson();
    if (!person || person.id === 'new') return;

    const employee = this.currentEmployee();
    if (employee) {
      // Navigate to edit employee
      this.router.navigate(['/admin/employees', employee.id]);
    } else {
      // Navigate to create employee with person ID
      this.router.navigate(['/admin/employees/new'], {
        queryParams: { personId: person.id }
      });
    }
  }

  ngOnDestroy(): void {
    this.feedbackService.clearMessage();
  }
}
