import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { IconComponent } from '@shared/components/icon/icon.component';
import { FeedbackMessageComponent } from '@shared/components/feedback-message/feedback-message.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { PersonFormComponent } from '@person/components/person-form/person-form.component';
import { EmployeeFormComponent } from '@employee/components/employee-form/employee-form.component';
import { UserFormComponent } from '@users/components/user-form/user-form.component';
import { PersonService } from '@person/services/person.service';
import { EmployeeService } from '@employee/services/employee.service';
import { UserService } from '@users/services/user.service';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { HttpErrorService } from '@shared/services/http-error.service';
import { FormBuildersManagerService } from '@shared/services/form-builders-manager.service';
import { Person } from '@person/interfaces/person.interface';
import { Employee } from '@employee/interfaces/employee.interface';
import { User } from '@users/interfaces/user.interface';
import { IconType } from '@shared/constants/icons.constant';
import { firstValueFrom, catchError } from 'rxjs';

interface Step {
  id: number;
  title: string;
  description: string;
  icon: IconType;
  completed: boolean;
  current: boolean;
}

@Component({
  selector: 'app-employee-creation',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    IconComponent,
    FeedbackMessageComponent,
    LoadingComponent,
    PersonFormComponent,
    EmployeeFormComponent,
    UserFormComponent
  ],
  templateUrl: './employee-creation.component.html',
})
export class EmployeeCreationComponent implements OnInit {

  private formBuilders = inject(FormBuildersManagerService);
  private router = inject(Router);
  private personService = inject(PersonService);
  private employeeService = inject(EmployeeService);
  private userService = inject(UserService);
  private feedbackService = inject(FeedbackMessageService);
  private httpErrorService = inject(HttpErrorService);

  currentStep = signal(1);
  isLoading = signal(false);
  showUserForm = signal(false);

  createdPerson = signal<Person | null>(null);
  createdEmployee = signal<Employee | null>(null);
  createdUser = signal<User | null>(null);

  steps: Step[] = [
    {
      id: 1,
      title: 'Personal Information',
      description: 'Basic personal data',
      icon: 'user',
      completed: false,
      current: true
    },
    {
      id: 2,
      title: 'Employment Information',
      description: 'Employee details and position',
      icon: 'user-tie',
      completed: false,
      current: false
    },
    {
      id: 3,
      title: 'System Access',
      description: 'Create user account (optional)',
      icon: 'settings',
      completed: false,
      current: false
    }
  ];

  personForm = this.formBuilders.buildPersonForm();
  employeeForm = this.formBuilders.buildEmployeeForm();
  userForm = this.formBuilders.buildUserForm({
    isEditMode: false,
    includePasswordValidation: true
  });

  ngOnInit() {
    this.feedbackService.clearMessage();
    this.updateSteps();
  }

  nextStep() {
    if (this.currentStep() < 3) {
      this.currentStep.set(this.currentStep() + 1);
      this.updateSteps();
    }
  }

  previousStep() {
    if (this.currentStep() > 1) {
      this.currentStep.set(this.currentStep() - 1);
      this.updateSteps();
    }
  }

  goToStep(stepId: number) {
    if (this.canGoToStep(stepId)) {
      this.currentStep.set(stepId);
      this.updateSteps();
    }
  }

  private updateSteps() {
    this.steps.forEach(step => {
      step.current = step.id === this.currentStep();
      step.completed = step.id < this.currentStep();
    });
  }

  async onPersonFormSubmitted() {
    if (this.personForm.invalid) {
      this.personForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    try {
      const rawPersonData = this.personForm.getRawValue();
      const personData = this.formBuilders.preparePersonFormData(rawPersonData);
      const createdPerson = await firstValueFrom(
        this.personService.createPerson(personData)
          .pipe(
            catchError(error => this.httpErrorService.handleError(error, 'creating person'))
          )
      );

      this.createdPerson.set(createdPerson);
      this.feedbackService.showSuccess('Personal information saved successfully');
      this.nextStep();
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'Failed to save personal information');
    } finally {
      this.isLoading.set(false);
    }
  }

  async onEmployeeFormSubmitted() {
    if (this.employeeForm.invalid || !this.createdPerson()) {
      this.employeeForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    try {
      const rawEmployeeData = this.employeeForm.getRawValue();
      const employeeData = {
        ...this.formBuilders.prepareEmployeeFormData(rawEmployeeData),
        personId: this.createdPerson()!.id
      };

      const createdEmployee = await firstValueFrom(
        this.employeeService.createEmployee(employeeData)
          .pipe(
            catchError(error => this.httpErrorService.handleError(error, 'creating employee'))
          )
      );

      this.createdEmployee.set(createdEmployee);
      this.feedbackService.showSuccess('Employment information saved successfully');
      this.nextStep();
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'Failed to save employment information');
    } finally {
      this.isLoading.set(false);
    }
  }

  async onUserFormSubmitted() {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    try {
      const rawUserData = this.userForm.getRawValue();
      const userData = {
        ...this.formBuilders.prepareUserFormData(rawUserData),
        roles: rawUserData.selectedRoles || [],
        personId: this.createdPerson()!.id
      };

      const createdUser = await firstValueFrom(
        this.userService.createUser(userData)
          .pipe(
            catchError(error => this.httpErrorService.handleError(error, 'creating user'))
          )
      );

      this.createdUser.set(createdUser);
      this.feedbackService.showSuccess('User account created successfully');
      this.completeProcess();
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'Failed to create user account');
    } finally {
      this.isLoading.set(false);
    }
  }

  skipUserCreation() {
    this.showUserForm.set(false);
    this.completeProcess();
  }

  private completeProcess() {
    this.feedbackService.showSuccess('Employee creation process completed successfully!');
    if (this.createdEmployee()) {
      this.router.navigate(['/admin/employees', this.createdEmployee()!.id]);
    } else {
      this.router.navigate(['/admin/employees']);
    }
  }

  onPersonUpdated(person: Person) {
    this.createdPerson.set(person);
    if (person.email && !this.userForm.get('email')?.value) {
      this.formBuilders.patchForm(this.userForm, { email: person.email });
    }
  }

  onPersonCreated(person: Person) {
    this.createdPerson.set(person);
    if (person.email && !this.userForm.get('email')?.value) {
      this.formBuilders.patchForm(this.userForm, { email: person.email });
    }
  }

  onRolesSelected(roles: string[]) {
    this.formBuilders.patchForm(this.userForm, { selectedRoles: roles });
    // Mark the field as touched to trigger change detection
    this.userForm.get('selectedRoles')?.markAsTouched();
  }


  getCurrentStepTitle(): string {
    const step = this.steps.find(s => s.id === this.currentStep());
    return step ? step.title : '';
  }

  getCurrentStepDescription(): string {
    const step = this.steps.find(s => s.id === this.currentStep());
    return step ? step.description : '';
  }

  getCurrentStepIcon(): IconType {
    const step = this.steps.find(s => s.id === this.currentStep());
    return step ? step.icon : 'user';
  }

  canProceedToNextStep(): boolean {
    switch (this.currentStep()) {
      case 1:
        return this.personForm.valid && this.createdPerson() !== null;
      case 2:
        return this.employeeForm.valid && this.createdEmployee() !== null;
      case 3:
        return this.showUserForm() ? this.userForm.valid : true;
      default:
        return false;
    }
  }

  canGoToPreviousStep(): boolean {
    return this.currentStep() > 1;
  }

  canGoToStep(stepId: number): boolean {
    const step = this.steps.find(s => s.id === stepId);
    if (!step) return false;

    // Can always go to current step
    if (step.current) return true;

    // Can go to completed steps
    if (step.completed) return true;

    // Can go to next step if current step is completed
    if (stepId === this.currentStep() + 1) {
      return this.canProceedToNextStep();
    }

    return false;
  }

  getStepStatus(stepId: number): 'completed' | 'current' | 'pending' | 'disabled' {
    const step = this.steps.find(s => s.id === stepId);
    if (!step) return 'disabled';

    if (step.completed) return 'completed';
    if (step.current) return 'current';

    // Check if we can proceed to this step
    if (this.canGoToStep(stepId)) {
      return 'pending';
    }

    return 'disabled';
  }
}
