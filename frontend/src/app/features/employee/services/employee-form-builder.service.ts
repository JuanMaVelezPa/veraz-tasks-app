import { Injectable } from '@angular/core';
import { FormGroup, Validators } from '@angular/forms';
import { FormUtilsService } from '@shared/services/form-utils.service';
import { BaseFormBuilderService, FormConfig, FieldConfig } from '@shared/services/base-form-builder.service';

export interface EmployeeFormConfig extends FormConfig { }

@Injectable({
  providedIn: 'root'
})
export class EmployeeFormBuilderService extends BaseFormBuilderService {

  protected readonly fieldConfigs: { [key: string]: FieldConfig } = {
    position: {
      name: 'position',
      validators: [Validators.required, Validators.minLength(3), Validators.maxLength(100)],
      defaultValue: '',
      isRequired: true
    },
    hireDate: {
      name: 'hireDate',
      validators: [Validators.required],
      defaultValue: '',
      isRequired: true
    },
    status: {
      name: 'status',
      validators: [Validators.required, Validators.maxLength(20)],
      defaultValue: 'ACTIVE',
      isRequired: true
    },
    salary: {
      name: 'salary',
      validators: [Validators.required, Validators.min(0.01)],
      defaultValue: null,
      isRequired: true
    },
    currency: {
      name: 'currency',
      validators: [Validators.required, Validators.maxLength(3)],
      defaultValue: 'CAD',
      isRequired: true
    },
    salaryType: {
      name: 'salaryType',
      validators: [Validators.required, Validators.maxLength(20)],
      defaultValue: 'HOURLY',
      isRequired: true
    },

    department: {
      name: 'department',
      validators: [Validators.maxLength(100)],
      defaultValue: ''
    },
    employmentType: {
      name: 'employmentType',
      validators: [Validators.maxLength(20)],
      defaultValue: ''
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
    workEmail: {
      name: 'workEmail',
      validators: [Validators.pattern(FormUtilsService.EMAIL_PATTERN), Validators.maxLength(100)],
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

  protected readonly requiredFields = ['position', 'hireDate', 'status', 'salary', 'currency', 'salaryType'];
  protected readonly optionalFields = [
    'department', 'employmentType', 'terminationDate', 'probationEndDate',
    'workEmail', 'workPhone', 'workLocation', 'workSchedule', 'jobLevel',
    'costCenter', 'workShift', 'skills', 'certifications', 'education',
    'benefits', 'notes', 'isActive'
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
