import { Component, input, output, signal, computed, OnInit, inject, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { ReactiveFormsModule, FormGroup } from '@angular/forms';
import { Employee } from '@employee/interfaces/employee.interface';
import { EmployeeParams } from '@employee/interfaces/employee-params.interface';
import { EmployeeParamsService } from '@employee/services/employee-params.service';
import { IconComponent } from '@shared/components/icon/icon.component';
import { ScrollService } from '@shared/services/scroll.service';
import { LoadingComponent } from '@shared/components/loading/loading.component';

@Component({
  selector: 'app-employee-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, IconComponent
    , CurrencyPipe, LoadingComponent],
  templateUrl: './employee-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class EmployeeFormComponent implements OnInit {
  // Inputs
  employeeForm = input.required<FormGroup>();
  employee = input<Employee | null>(null);
  isEditMode = input<boolean>(false);
  isLoading = input<boolean>(false);
  isReadOnly = input<boolean>(false);

  // Outputs
  formSubmitted = output<void>();
  formCancelled = output<void>();

  // Computed properties
  submitButtonText = computed(() => this.isEditMode() ? 'Update Employee' : 'Create Employee');
  submitButtonIcon = computed(() => this.isEditMode() ? 'save' : 'add');

  private employeeParamsService = inject(EmployeeParamsService);
  private scrollService = inject(ScrollService);
  private paramCache = new Map<string, any>();

  // Data
  employeeParams = signal(this.employeeParamsService.getEmployeeParams());

  // Computed param arrays
  employmentTypes = computed(() => this.employeeParams().employmentTypes);
  employeeStatuses = computed(() => this.employeeParams().employeeStatuses);
  jobLevels = computed(() => this.employeeParams().jobLevels);
  salaryTypes = computed(() => this.employeeParams().salaryTypes);
  currencies = computed(() => this.employeeParams().currencies);
  workShifts = computed(() => this.employeeParams().workShifts);
  departments = computed(() => this.employeeParams().departments);

  ngOnInit(): void {
    this.employeeParams.set(this.employeeParamsService.getEmployeeParams());
  }

  // Form actions
  submitForm(): void {
    if (this.employeeForm().valid && !this.isLoading()) {
      this.formSubmitted.emit();
      this.scrollService.scrollToTop();
    }
  }

  cancelForm(): void {
    this.formCancelled.emit();
  }

  // Generic method for getting display names
  getDisplayName(code: string, paramType: keyof EmployeeParams): string {
    const cacheKey = `${String(paramType)}_${code}`;
    return this.getCachedValue(cacheKey, () => {
      const items = this.employeeParams()[paramType] as any[];
      const item = items.find(item => item.code === code);
      return item?.name || code;
    });
  }

  // Specific methods using the generic approach
  getEmploymentTypeDisplayName(typeCode: string): string {
    return this.getDisplayName(typeCode, 'employmentTypes');
  }

  getEmployeeStatusDisplayName(statusCode: string): string {
    return this.getDisplayName(statusCode, 'employeeStatuses');
  }

  getJobLevelDisplayName(levelCode: string): string {
    return this.getDisplayName(levelCode, 'jobLevels');
  }

  getSalaryTypeDisplayName(typeCode: string): string {
    return this.getDisplayName(typeCode, 'salaryTypes');
  }

  getCurrencyDisplayName(currencyCode: string): string {
    return this.getDisplayName(currencyCode, 'currencies');
  }

  getWorkShiftDisplayName(shiftCode: string): string {
    return this.getDisplayName(shiftCode, 'workShifts');
  }

  getDepartmentDisplayName(departmentCode: string): string {
    return this.getDisplayName(departmentCode, 'departments');
  }

  getCurrencySymbol(currencyCode: string): string {
    const cacheKey = `currencySymbol_${currencyCode}`;
    return this.getCachedValue(cacheKey, () => {
      const currency = this.employeeParams().currencies.find(c => c.code === currencyCode);
      return currency?.symbol || '$';
    });
  }

  // Private helper methods
  private getCachedValue<T>(cacheKey: string, valueFactory: () => T): T {
    if (!this.paramCache.has(cacheKey)) {
      this.paramCache.set(cacheKey, valueFactory());
    }
    return this.paramCache.get(cacheKey);
  }
}
