import { Injectable, inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

export interface FieldConfig {
  name: string;
  validators: any[];
  defaultValue: any;
  isRequired?: boolean;
}

export interface FormConfig {
  includeOptionalFields?: boolean;
  isReadOnly?: boolean;
  customValidators?: any[];
}

@Injectable()
export abstract class BaseFormBuilderService {
  protected fb = inject(FormBuilder);

  protected abstract readonly fieldConfigs: { [key: string]: FieldConfig };
  protected abstract readonly requiredFields: string[];
  protected abstract readonly optionalFields: string[];

  protected buildForm(config: FormConfig = {}): FormGroup {
    const { includeOptionalFields = true, isReadOnly = false, customValidators = [] } = config;

    const fieldsToInclude = includeOptionalFields
      ? [...this.requiredFields, ...this.optionalFields]
      : this.requiredFields;

    const formConfig: { [key: string]: any[] } = {};

    fieldsToInclude.forEach(fieldName => {
      const fieldConfig = this.fieldConfigs[fieldName];
      if (fieldConfig) {
        const validators = isReadOnly ? [] : fieldConfig.validators;
        formConfig[fieldName] = [fieldConfig.defaultValue, validators];
      }
    });

    const form = this.fb.nonNullable.group(formConfig);

    if (customValidators.length > 0) {
      customValidators.forEach(validator => {
        form.addValidators(validator);
      });
    }

    return form;
  }

  getFieldConfig(fieldName: string): FieldConfig | null {
    return this.fieldConfigs[fieldName] || null;
  }

  isFieldRequired(fieldName: string): boolean {
    const config = this.fieldConfigs[fieldName];
    return config?.isRequired || false;
  }

  getFieldValidators(fieldName: string): any[] {
    const config = this.fieldConfigs[fieldName];
    return config?.validators || [];
  }

  getFieldDefaultValue(fieldName: string): any {
    const config = this.fieldConfigs[fieldName];
    return config?.defaultValue;
  }

  patchForm(form: FormGroup, data: any): void {
    if (data) {
      form.patchValue(data);
    }
  }

  resetForm(form: FormGroup, fieldNames?: string[]): void {
    if (fieldNames) {
      fieldNames.forEach(fieldName => {
        const defaultValue = this.getFieldDefaultValue(fieldName);
        form.get(fieldName)?.setValue(defaultValue);
      });
    } else {
      const resetData: any = {};
      Object.keys(form.controls).forEach(fieldName => {
        resetData[fieldName] = this.getFieldDefaultValue(fieldName);
      });
      form.reset(resetData);
    }
  }

  detectChanges(formValue: any, originalData: any, options: {
    includeEmptyStrings?: boolean;
    trimStrings?: boolean;
    toLowerCase?: string[];
  } = {}): any {
    const { includeEmptyStrings = false, trimStrings = true, toLowerCase = [] } = options;
    const changes: any = {};

    const compareStrings = (original: string | undefined, newValue: string | undefined) => {
      const orig = trimStrings ? original?.trim() : original;
      const newVal = trimStrings ? newValue?.trim() : newValue;
      return (orig || '') !== (newVal || '');
    };

    Object.keys(formValue).forEach(fieldName => {
      const originalValue = originalData[fieldName];
      const newValue = formValue[fieldName];

      // Skip if values are the same
      if (originalValue === newValue) return;

      // Handle string comparisons
      if (typeof newValue === 'string' && typeof originalValue === 'string') {
        if (compareStrings(originalValue, newValue)) {
          let processedValue = newValue;
          if (trimStrings) processedValue = processedValue.trim();
          if (toLowerCase.includes(fieldName)) processedValue = processedValue.toLowerCase();
          if (processedValue || includeEmptyStrings) {
            changes[fieldName] = processedValue;
          }
        }
      }
      // Handle boolean comparisons
      else if (typeof newValue === 'boolean' && typeof originalValue === 'boolean') {
        if (newValue !== originalValue) {
          changes[fieldName] = newValue;
        }
      }
      // Handle other types
      else if (newValue !== originalValue) {
        if (newValue || includeEmptyStrings) {
          changes[fieldName] = newValue;
        }
      }
    });

    return changes;
  }

  prepareFormData(formValue: any, options: {
    includeEmptyStrings?: boolean;
    trimStrings?: boolean;
    toLowerCase?: string[];
  } = {}): any {
    const { includeEmptyStrings = false, trimStrings = true, toLowerCase = [] } = options;
    const formData: any = {};

    Object.keys(formValue).forEach(fieldName => {
      let value = formValue[fieldName];

      if (typeof value === 'string') {
        if (trimStrings) value = value.trim();
        if (toLowerCase.includes(fieldName)) value = value.toLowerCase();
        if (value || includeEmptyStrings) {
          formData[fieldName] = value;
        }
      } else if (value !== null && value !== undefined) {
        formData[fieldName] = value;
      }
    });

    return formData;
  }
}
