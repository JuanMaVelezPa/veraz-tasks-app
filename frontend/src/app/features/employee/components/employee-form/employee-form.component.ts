import { Component, input, output, signal, computed, OnInit, inject, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { ReactiveFormsModule, FormGroup } from '@angular/forms';
import { Employee, EmployeeCreateRequest, EmployeeUpdateRequest } from '@employee/interfaces/employee.interface';
import { EmployeeParamsService } from '@employee/services/employee-params.service';
import { IconComponent } from '@shared/components/icon/icon.component';

@Component({
  selector: 'app-employee-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, IconComponent, CurrencyPipe],
  templateUrl: './employee-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush // Optimización clave
})
export class EmployeeFormComponent implements OnInit {
  // Inputs
  employeeForm = input.required<FormGroup>();
  employee = input<Employee | null>(null);
  isEditMode = input<boolean>(false);
  isLoading = input<boolean>(false);

  // Outputs
  formSubmitted = output<void>();
  formCancelled = output<void>();

  // Computed values
  submitButtonText = computed(() => this.isEditMode() ? 'Update Employee' : 'Create Employee');
  submitButtonIcon = computed(() => this.isEditMode() ? 'save' : 'add');

  // Employee params service con inject para mejor performance
  private employeeParamsService = inject(EmployeeParamsService);

  // Cache para mejorar performance
  private paramCache = new Map<string, any>();

  // Employee params for form options - signal estático
  employeeParams = signal(this.employeeParamsService.getEmployeeParams());

  // Computed signals optimizados para el template
  employmentTypes = computed(() => this.employeeParams().employmentTypes);
  employeeStatuses = computed(() => this.employeeParams().employeeStatuses);
  jobLevels = computed(() => this.employeeParams().jobLevels);
  salaryTypes = computed(() => this.employeeParams().salaryTypes);
  currencies = computed(() => this.employeeParams().currencies);
  workShifts = computed(() => this.employeeParams().workShifts);
  departments = computed(() => this.employeeParams().departments);

  ngOnInit() {
    // Cargar parámetros una sola vez
    this.employeeParams.set(this.employeeParamsService.getEmployeeParams());
  }

  onSubmit() {
    if (this.employeeForm().valid && !this.isLoading()) {
      this.formSubmitted.emit();
    }
  }

  onCancel() {
    this.formCancelled.emit();
  }

  getFieldError(fieldName: string): string | null {
    const field = this.employeeForm().get(fieldName);
    if (field?.invalid && field?.touched) {
      if (field.errors?.['required']) {
        return 'This field is required';
      }
      if (field.errors?.['minlength']) {
        return `Minimum length is ${field.errors['minlength'].requiredLength} characters`;
      }
      if (field.errors?.['maxlength']) {
        return `Maximum length is ${field.errors['maxlength'].requiredLength} characters`;
      }
      if (field.errors?.['pattern']) {
        return 'Invalid format';
      }
      if (field.errors?.['email']) {
        return 'Please enter a valid email address';
      }
    }
    return null;
  }

  // Métodos optimizados con cache
  getEmploymentTypeName(code: string): string {
    return this.getFromCache(`employmentType_${code}`, () => {
      const type = this.employeeParams().employmentTypes.find(t => t.code === code);
      return type?.name || code;
    });
  }

  getStatusName(code: string): string {
    return this.getFromCache(`status_${code}`, () => {
      const status = this.employeeParams().employeeStatuses.find(s => s.code === code);
      return status?.name || code;
    });
  }

  getJobLevelName(code: string): string {
    return this.getFromCache(`jobLevel_${code}`, () => {
      const level = this.employeeParams().jobLevels.find(l => l.code === code);
      return level?.name || code;
    });
  }

  getSalaryTypeName(code: string): string {
    return this.getFromCache(`salaryType_${code}`, () => {
      const type = this.employeeParams().salaryTypes.find(t => t.code === code);
      return type?.name || code;
    });
  }

  getCurrencyName(code: string): string {
    return this.getFromCache(`currency_${code}`, () => {
      const currency = this.employeeParams().currencies.find(c => c.code === code);
      return currency?.name || code;
    });
  }

  getWorkShiftName(code: string): string {
    return this.getFromCache(`workShift_${code}`, () => {
      const shift = this.employeeParams().workShifts.find(s => s.code === code);
      return shift?.name || code;
    });
  }

  getDepartmentName(code: string): string {
    return this.getFromCache(`department_${code}`, () => {
      const dept = this.employeeParams().departments.find(d => d.code === code);
      return dept?.name || code;
    });
  }

  getCurrencySymbol(code: string): string {
    return this.getFromCache(`currencySymbol_${code}`, () => {
      const currency = this.employeeParams().currencies.find(c => c.code === code);
      return currency?.symbol || '$';
    });
  }

  // Método helper para cache
  private getFromCache<T>(key: string, factory: () => T): T {
    if (this.paramCache.has(key)) {
      return this.paramCache.get(key);
    }
    const value = factory();
    this.paramCache.set(key, value);
    return value;
  }
}
