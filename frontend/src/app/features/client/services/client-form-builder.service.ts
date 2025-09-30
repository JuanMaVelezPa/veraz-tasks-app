import { Injectable } from '@angular/core';
import { FormGroup, Validators } from '@angular/forms';
import { BaseFormBuilderService, FormConfig, FieldConfig } from '@shared/services/base-form-builder.service';
import { ClientStatus } from '@client/interfaces/client.interface';

export interface ClientFormConfig extends FormConfig { }

@Injectable({
  providedIn: 'root'
})
export class ClientFormBuilderService extends BaseFormBuilderService {

  protected readonly requiredFields: string[] = [
    'personId',
    'type',
    'category',
    'status',
    'creditLimit',
    'currency'
  ];

  protected readonly optionalFields: string[] = [
    'source',
    'companyName',
    'companyWebsite',
    'companyIndustry',
    'contactPerson',
    'contactPosition',
    'address',
    'city',
    'country',
    'postalCode',
    'taxId',
    'paymentTerms',
    'paymentMethod',
    'notes',
    'preferences',
    'tags',
    'rating',
    'isActive'
  ];

  protected readonly fieldConfigs: { [key: string]: FieldConfig } = {
    personId: {
      name: 'personId',
      validators: [],
      defaultValue: '',
      isRequired: false
    },
    type: {
      name: 'type',
      validators: [Validators.required, Validators.maxLength(20)],
      defaultValue: '',
      isRequired: true
    },
    category: {
      name: 'category',
      validators: [Validators.required, Validators.maxLength(50)],
      defaultValue: '',
      isRequired: true
    },
    source: {
      name: 'source',
      validators: [Validators.maxLength(50)],
      defaultValue: ''
    },
    companyName: {
      name: 'companyName',
      validators: [Validators.maxLength(200)],
      defaultValue: ''
    },
    companyWebsite: {
      name: 'companyWebsite',
      validators: [Validators.maxLength(255)],
      defaultValue: ''
    },
    companyIndustry: {
      name: 'companyIndustry',
      validators: [Validators.maxLength(100)],
      defaultValue: ''
    },
    contactPerson: {
      name: 'contactPerson',
      validators: [Validators.maxLength(200)],
      defaultValue: ''
    },
    contactPosition: {
      name: 'contactPosition',
      validators: [Validators.maxLength(100)],
      defaultValue: ''
    },
    address: {
      name: 'address',
      validators: [Validators.maxLength(255)],
      defaultValue: ''
    },
    city: {
      name: 'city',
      validators: [Validators.maxLength(100)],
      defaultValue: ''
    },
    country: {
      name: 'country',
      validators: [Validators.maxLength(100)],
      defaultValue: ''
    },
    postalCode: {
      name: 'postalCode',
      validators: [Validators.maxLength(20)],
      defaultValue: ''
    },
    taxId: {
      name: 'taxId',
      validators: [Validators.maxLength(50)],
      defaultValue: ''
    },
    creditLimit: {
      name: 'creditLimit',
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
    paymentTerms: {
      name: 'paymentTerms',
      validators: [Validators.maxLength(50)],
      defaultValue: ''
    },
    paymentMethod: {
      name: 'paymentMethod',
      validators: [Validators.maxLength(50)],
      defaultValue: ''
    },
    notes: {
      name: 'notes',
      validators: [Validators.maxLength(500)],
      defaultValue: ''
    },
    preferences: {
      name: 'preferences',
      validators: [Validators.maxLength(500)],
      defaultValue: ''
    },
    tags: {
      name: 'tags',
      validators: [Validators.maxLength(500)],
      defaultValue: ''
    },
    rating: {
      name: 'rating',
      validators: [Validators.min(1), Validators.max(5)],
      defaultValue: null
    },
    status: {
      name: 'status',
      validators: [Validators.required, Validators.maxLength(20)],
      defaultValue: ClientStatus.ACTIVE,
      isRequired: true
    },
    isActive: {
      name: 'isActive',
      validators: [],
      defaultValue: true
    }
  };

  buildClientForm(config: ClientFormConfig = {}): FormGroup {
    return this.buildForm(config);
  }

  buildClientFormWithDefaults(defaults: Partial<ClientFormConfig>): FormGroup {
    return this.buildForm(defaults);
  }

  prepareClientFormData(formValue: any): any {
    // Clean empty values and convert types
    const cleanedData: any = {};

    Object.keys(formValue).forEach(key => {
      const value = formValue[key];
      if (value !== null && value !== undefined && value !== '') {
        if (key === 'creditLimit' && value !== null) {
          cleanedData[key] = Number(value);
        } else if (key === 'rating' && value !== null) {
          cleanedData[key] = Number(value);
        } else if (key === 'isActive') {
          cleanedData[key] = Boolean(value);
        } else {
          cleanedData[key] = value;
        }
      }
    });

    return cleanedData;
  }

}

