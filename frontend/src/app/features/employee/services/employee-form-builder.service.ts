import { Injectable, inject } from '@angular/core';
import { FormGroup, Validators } from '@angular/forms';
import { FormUtilsService } from '@shared/services/form-utils.service';
import { BaseFormBuilderService, FormConfig, FieldConfig } from '@shared/services/base-form-builder.service';

export interface EmployeeFormConfig extends FormConfig { }

@Injectable({
  providedIn: 'root'
})
export class EmployeeFormBuilderService extends BaseFormBuilderService {
  private formUtils = inject(FormUtilsService);

  protected readonly fieldConfigs: { [key: string]: FieldConfig } = {
    position: {
      name: 'position',
      validators: [Validators.required, Validators.minLength(3), Validators.maxLength(100)],
      defaultValue: '',
      isRequired: true
    },
    department: {
      name: 'department',
      validators: [Validators.maxLength(100)],
      defaultValue: ''
    },
    employmentType: {
      name: 'employmentType',
      validators: [Validators.required, Validators.maxLength(20)],
      defaultValue: '',
      isRequired: true
    },
    status: {
      name: 'status',
      validators: [Validators.maxLength(20)],
      defaultValue: 'ACTIVE'
    },
    hireDate: {
      name: 'hireDate',
      validators: [Validators.required],
      defaultValue: '',
      isRequired: true
    },
    terminationDate: {
      name: 'terminationDate',
      validators: [],
      defaultValue: ''
    },
    probationEndDate: {
      name: 'probationEndDate',
      validators: [],
      defaultValue: ''
    },
    salary: {
      name: 'salary',
      validators: [],
      defaultValue: null
    },
    currency: {
      name: 'currency',
      validators: [Validators.maxLength(3)],
      defaultValue: 'USD'
    },
    salaryType: {
      name: 'salaryType',
      validators: [Validators.maxLength(20)],
      defaultValue: ''
    },
    workEmail: {
      name: 'workEmail',
      validators: [Validators.pattern(FormUtilsService.emailPattern), Validators.maxLength(100)],
      defaultValue: ''
    },
    workPhone: {
      name: 'workPhone',
      validators: [Validators.maxLength(20)],
      defaultValue: ''
    },
    workLocation: {
      name: 'workLocation',
      validators: [Validators.maxLength(100)],
      defaultValue: ''
    },
    workSchedule: {
      name: 'workSchedule',
      validators: [Validators.maxLength(100)],
      defaultValue: ''
    },
    jobLevel: {
      name: 'jobLevel',
      validators: [Validators.maxLength(20)],
      defaultValue: ''
    },
    costCenter: {
      name: 'costCenter',
      validators: [Validators.maxLength(50)],
      defaultValue: ''
    },
    workShift: {
      name: 'workShift',
      validators: [Validators.maxLength(20)],
      defaultValue: ''
    },
    skills: {
      name: 'skills',
      validators: [],
      defaultValue: ''
    },
    certifications: {
      name: 'certifications',
      validators: [],
      defaultValue: ''
    },
    education: {
      name: 'education',
      validators: [],
      defaultValue: ''
    },
    benefits: {
      name: 'benefits',
      validators: [],
      defaultValue: ''
    },
    notes: {
      name: 'notes',
      validators: [],
      defaultValue: ''
    },
    isActive: {
      name: 'isActive',
      validators: [],
      defaultValue: true
    }
  };

  protected readonly requiredFields = ['position', 'employmentType', 'hireDate'];
  protected readonly optionalFields = [
    'department', 'status', 'terminationDate', 'probationEndDate', 'salary',
    'currency', 'salaryType', 'workEmail', 'workPhone', 'workLocation',
    'workSchedule', 'jobLevel', 'costCenter', 'workShift', 'skills',
    'certifications', 'education', 'benefits', 'notes', 'isActive'
  ];

  buildEmployeeForm(config: EmployeeFormConfig = {}): FormGroup {
    return this.buildForm(config);
  }

  prepareEmployeeFormData(formValue: any): any {
    return this.prepareFormData(formValue, {
      trimStrings: true
    });
  }
}
