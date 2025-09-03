import { Injectable, inject } from '@angular/core';
import { FormGroup, Validators } from '@angular/forms';
import { FormUtilsService } from '@shared/services/form-utils.service';
import { BaseFormBuilderService, FormConfig, FieldConfig } from '@shared/services/base-form-builder.service';

export interface PersonFormConfig extends FormConfig { }

@Injectable({
  providedIn: 'root'
})
export class PersonFormBuilderService extends BaseFormBuilderService {
  private formUtils = inject(FormUtilsService);

  protected readonly fieldConfigs: { [key: string]: FieldConfig } = {
    identType: {
      name: 'identType',
      validators: [Validators.required],
      defaultValue: '',
      isRequired: true
    },
    identNumber: {
      name: 'identNumber',
      validators: [Validators.required, Validators.minLength(3)],
      defaultValue: '',
      isRequired: true
    },
    firstName: {
      name: 'firstName',
      validators: [Validators.required, Validators.minLength(2)],
      defaultValue: '',
      isRequired: true
    },
    lastName: {
      name: 'lastName',
      validators: [Validators.required, Validators.minLength(2)],
      defaultValue: '',
      isRequired: true
    },
    birthDate: {
      name: 'birthDate',
      validators: [],
      defaultValue: ''
    },
    gender: {
      name: 'gender',
      validators: [],
      defaultValue: ''
    },
    nationality: {
      name: 'nationality',
      validators: [],
      defaultValue: ''
    },
    mobile: {
      name: 'mobile',
      validators: [],
      defaultValue: ''
    },
    email: {
      name: 'email',
      validators: [Validators.pattern(FormUtilsService.EMAIL_PATTERN)],
      defaultValue: '',
      isRequired: true
    },
    address: {
      name: 'address',
      validators: [],
      defaultValue: ''
    },
    city: {
      name: 'city',
      validators: [],
      defaultValue: ''
    },
    country: {
      name: 'country',
      validators: [],
      defaultValue: ''
    },
    postalCode: {
      name: 'postalCode',
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

  protected readonly requiredFields = ['identType', 'identNumber', 'firstName', 'lastName'];
  protected readonly optionalFields = [
    'birthDate', 'gender', 'nationality', 'mobile', 'email',
    'address', 'city', 'country', 'postalCode', 'notes', 'isActive'
  ];

  buildPersonForm(config: PersonFormConfig = {}): FormGroup {
    return this.buildForm(config);
  }

  detectPersonChanges(formValue: any, originalPerson: any): any {
    // Clean up form values for better comparison
    const cleanedFormValue = this.cleanFormValues(formValue);

    const changes = this.detectChanges(cleanedFormValue, originalPerson, {
      trimStrings: true
    });

    return changes;
  }

  private cleanFormValues(formValue: any): any {
    const cleaned = { ...formValue };

    // Convert empty strings to null for better comparison
    Object.keys(cleaned).forEach(key => {
      if (typeof cleaned[key] === 'string' && cleaned[key].trim() === '') {
        cleaned[key] = null;
      }
    });

    return cleaned;
  }

  preparePersonFormData(formValue: any): any {
    const processedData = this.prepareFormData(formValue, {
      trimStrings: true
    });

    // Apply title case to firstName and lastName
    if (processedData.firstName) {
      processedData.firstName = FormUtilsService.toTitleCase(processedData.firstName);
    }
    if (processedData.lastName) {
      processedData.lastName = FormUtilsService.toTitleCase(processedData.lastName);
    }

    return processedData;
  }

  validateRequiredPersonFields(formData: any): { isValid: boolean; missingFields: string[] } {
    const requiredFields = ['identType', 'identNumber', 'firstName', 'lastName'];
    const missingFields = requiredFields.filter(field => !formData[field] || !formData[field].trim());

    return {
      isValid: missingFields.length === 0,
      missingFields
    };
  }

  override patchForm(form: FormGroup, data: any): void {
    if (data) {
      const formattedData = { ...data };
      if (formattedData.birthDate && typeof formattedData.birthDate === 'string') {
        try {
          const date = new Date(formattedData.birthDate);
          formattedData.birthDate = date.toISOString().split('T')[0];
        } catch (error) {
          console.warn('Invalid date format for birthDate:', formattedData.birthDate);
        }
      }
      form.patchValue(formattedData);
    }
  }
}
