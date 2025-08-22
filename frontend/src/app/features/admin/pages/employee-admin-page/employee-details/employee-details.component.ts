import { Component, inject, input, signal, OnDestroy, ChangeDetectorRef, OnInit } from '@angular/core';
import { Employee, EmployeeCreateRequest, EmployeeUpdateRequest } from '@employee/interfaces/employee.interface';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { EmployeeService } from '@employee/services/employee.service';
import { PersonService } from '@person/services/person.service';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FeedbackMessageComponent } from '@shared/components/feedback-message/feedback-message.component';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { NavigationHistoryService } from '@shared/services/navigation-history.service';
import { HttpErrorService } from '@shared/services/http-error.service';
import { EmployeeFormComponent } from '@employee/components/employee-form/employee-form.component';
import { TimestampInfoComponent } from '@shared/components/timestamp-info/timestamp-info.component';
import { IconComponent } from '@shared/components/icon/icon.component';
import { PersonInfoCardComponent } from '@person/components/person-info-card/person-info-card.component';
import { firstValueFrom, catchError } from 'rxjs';
import { FormUtilsService } from '@shared/services/form-utils.service';

@Component({
  selector: 'employee-details',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, FeedbackMessageComponent, EmployeeFormComponent, IconComponent, TimestampInfoComponent, PersonInfoCardComponent],
  templateUrl: './employee-details.component.html',
})
export class EmployeeDetailsComponent implements OnInit, OnDestroy {
  employee = input.required<Employee>();

  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private employeeService = inject(EmployeeService);
  private personService = inject(PersonService);
  private httpErrorService = inject(HttpErrorService);
  private feedbackService = inject(FeedbackMessageService);
  private navigationHistory = inject(NavigationHistoryService);
  private cdr = inject(ChangeDetectorRef);

  wasSaved = signal(false);
  isLoading = signal(false);
  isEditMode = signal(false);
  showDeleteModal = signal(false);
  currentEmployee = signal<Employee | null>(null);
  currentPerson = signal<any>(null);
  isLoadingPerson = signal(false);

  employeeForm = this.fb.nonNullable.group({
    employeeCode: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
    position: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    department: ['', [Validators.maxLength(100)]],
    employmentType: ['', [Validators.required, Validators.maxLength(20)]],
    status: ['ACTIVE', [Validators.maxLength(20)]],
    hireDate: ['', [Validators.required]],
    terminationDate: [''],
    probationEndDate: [''],
    salary: [null as number | null],
    currency: ['USD', [Validators.maxLength(3)]],
    salaryType: ['', [Validators.maxLength(20)]],
    workEmail: ['', [Validators.pattern(FormUtilsService.emailPattern), Validators.maxLength(100)]],
    workPhone: ['', [Validators.maxLength(20)]],
    workLocation: ['', [Validators.maxLength(100)]],
    workSchedule: ['', [Validators.maxLength(100)]],
    jobLevel: ['', [Validators.maxLength(20)]],
    costCenter: ['', [Validators.maxLength(50)]],
    workShift: ['', [Validators.maxLength(20)]],
    skills: [''],
    certifications: [''],
    education: [''],
    benefits: [''],
    notes: [''],
    isActive: [true]
  });

  ngOnInit(): void {
    this.feedbackService.clearMessage();

    // Check if we're creating a new employee with personId from query params
    const personId = this.route.snapshot.queryParamMap.get('personId');

    let employee = this.employee();

    // If creating new employee and we have personId, create employee object
    if (employee.id === 'new' && personId && personId.trim() !== '') {
      employee = {
        ...employee,
        personId: personId.trim()
      };
      this.currentEmployee.set(employee);
    } else {
      this.currentEmployee.set(employee);
    }

    this.setFormValues(employee);
    this.isEditMode.set(employee.id !== 'new');

    // Load person information if we have a valid personId (both for edit and create modes)
    const validPersonId = employee.personId && employee.personId !== 'new' && employee.personId.trim() !== '';
    if (validPersonId) {
      this.loadPersonInfo(employee.personId);
    }
  }

  ngOnDestroy(): void {
    this.feedbackService.clearMessage();
  }

  private async loadPersonInfo(personId: string): Promise<void> {
    this.isLoadingPerson.set(true);
    try {
      const person = await firstValueFrom(this.personService.getPersonById(personId));
      this.currentPerson.set(person);
    } catch (error) {
      console.error('Error loading person info:', error);
    } finally {
      this.isLoadingPerson.set(false);
    }
  }

  private setFormValues(employee: Employee): void {
    this.employeeForm.patchValue({
      employeeCode: employee.employeeCode || '',
      position: employee.position || '',
      department: employee.department || '',
      employmentType: employee.employmentType || '',
      status: employee.status || 'ACTIVE',
      hireDate: employee.hireDate || '',
      terminationDate: employee.terminationDate || '',
      probationEndDate: employee.probationEndDate || '',
      salary: employee.salary || null,
      currency: employee.currency || 'USD',
      salaryType: employee.salaryType || '',
      workEmail: employee.workEmail || '',
      workPhone: employee.workPhone || '',
      workLocation: employee.workLocation || '',
      workSchedule: employee.workSchedule || '',
      jobLevel: employee.jobLevel || '',
      costCenter: employee.costCenter || '',
      workShift: employee.workShift || '',
      skills: employee.skills || '',
      certifications: employee.certifications || '',
      education: employee.education || '',
      benefits: employee.benefits || '',
      notes: employee.notes || '',
      isActive: employee.isActive ?? true
    });
  }

  async onSubmit(): Promise<void> {
    if (this.employeeForm.invalid || this.isLoading()) {
      return;
    }

    // Get personId from current employee or query params for creation
    let personId = this.currentEmployee()?.personId;

    // If we're in creation mode and don't have a personId, try to get it from query params
    if (!this.isEditMode() && (!personId || personId === '' || personId === 'new')) {
      const queryPersonId = this.route.snapshot.queryParamMap.get('personId');
      if (queryPersonId) {
        personId = queryPersonId;
      }
    }

    // Validate that we have a valid personId for creation
    if (!this.isEditMode() && (!personId || personId === '' || personId === 'new')) {
      this.feedbackService.showError('A valid person must be selected to create an employee');
      return;
    }

    this.isLoading.set(true);
    this.feedbackService.clearMessage();

    try {
      const formValue = this.employeeForm.getRawValue();
      const employeeData: EmployeeCreateRequest | EmployeeUpdateRequest = {
        personId: personId,
        employeeCode: formValue.employeeCode,
        position: formValue.position,
        department: formValue.department || undefined,
        employmentType: formValue.employmentType,
        status: formValue.status,
        hireDate: formValue.hireDate,
        terminationDate: formValue.terminationDate || undefined,
        probationEndDate: formValue.probationEndDate || undefined,
        salary: formValue.salary || undefined,
        currency: formValue.currency || undefined,
        salaryType: formValue.salaryType || undefined,
        workEmail: formValue.workEmail || undefined,
        workPhone: formValue.workPhone || undefined,
        workLocation: formValue.workLocation || undefined,
        workSchedule: formValue.workSchedule || undefined,
        jobLevel: formValue.jobLevel || undefined,
        costCenter: formValue.costCenter || undefined,
        workShift: formValue.workShift || undefined,
        skills: formValue.skills || undefined,
        certifications: formValue.certifications || undefined,
        education: formValue.education || undefined,
        benefits: formValue.benefits || undefined,
        notes: formValue.notes || undefined,
        isActive: formValue.isActive
      };

      let result: Employee;

      if (this.isEditMode()) {
        result = await firstValueFrom(
          this.employeeService.updateEmployee(this.employee().id, employeeData as EmployeeUpdateRequest)
            .pipe(
              catchError(error => this.httpErrorService.handleError(error, 'updating employee'))
            )
        );
        this.feedbackService.showSuccess('Employee updated successfully');
      } else {
        result = await firstValueFrom(
          this.employeeService.createEmployee(employeeData as EmployeeCreateRequest)
            .pipe(
              catchError(error => this.httpErrorService.handleError(error, 'creating employee'))
            )
        );
        this.feedbackService.showSuccess('Employee created successfully');
      }

      this.currentEmployee.set(result);
      this.wasSaved.set(true);
      this.cdr.detectChanges();

      // Navigate to the employee details page
      this.router.navigate(['/admin/employees', result.id]);
    } catch (error: any) {
      // Show error message from backend or fallback to generic message
      this.feedbackService.showError(error.message || 'An error occurred while saving the employee.');
      console.error('Employee operation failed:', error);
    } finally {
      this.isLoading.set(false);
    }
  }

  async deleteEmployee(): Promise<void> {
    if (!this.currentEmployee() || this.isLoading()) {
      return;
    }

    this.isLoading.set(true);
    this.feedbackService.clearMessage();

    try {
      await firstValueFrom(
        this.employeeService.deleteEmployee(this.currentEmployee()!.id)
          .pipe(
            catchError(error => this.httpErrorService.handleError(error, 'deleting employee'))
          )
      );
      this.feedbackService.showSuccess('Employee deleted successfully');
      this.showDeleteModal.set(false);
      this.goBack();
    } catch (error: any) {
      // Show error message from backend or fallback to generic message
      this.feedbackService.showError(error.message || 'An error occurred while deleting the employee.');
      console.error('Employee deletion failed:', error);
    } finally {
      this.isLoading.set(false);
    }
  }

  showDeleteConfirmation(): void {
    this.showDeleteModal.set(true);
  }

  cancelDelete(): void {
    this.showDeleteModal.set(false);
  }

  goBack(): void {
    this.navigationHistory.goBack();
  }

  onPersonAction(): void {
    if (this.currentPerson()) {
      this.router.navigate(['/admin/persons', this.currentPerson().id]);
    }
  }
}
