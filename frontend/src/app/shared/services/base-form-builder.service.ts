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

    const fieldsToInclude = this.getFieldsToInclude(includeOptionalFields);
    const formConfig = this.buildFormConfig(fieldsToInclude, isReadOnly);
    const form = this.fb.nonNullable.group(formConfig);

    this.addCustomValidators(form, customValidators);

    // Set disabled state for all controls if form is read-only
    if (isReadOnly) {
      this.setFormReadOnly(form, true);
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

  setFormReadOnly(form: FormGroup, isReadOnly: boolean): void {
    if (isReadOnly) {
      form.disable();
    } else {
      form.enable();
    }
  }

  setFieldReadOnly(form: FormGroup, fieldName: string, isReadOnly: boolean): void {
    const control = form.get(fieldName);
    if (control) {
      if (isReadOnly) {
        control.disable();
      } else {
        control.enable();
      }
    }
  }

  resetForm(form: FormGroup, fieldNames?: string[]): void {
    if (fieldNames) {
      this.resetSpecificFields(form, fieldNames);
    } else {
      this.resetAllFields(form);
    }
  }

  detectChanges(formValue: any, originalData: any, options: {
    includeEmptyStrings?: boolean;
    trimStrings?: boolean;
    toLowerCase?: string[];
  } = {}): any {
    const { includeEmptyStrings = false, trimStrings = true, toLowerCase = [] } = options;
    const changes: any = {};

    Object.keys(formValue).forEach(fieldName => {
      const originalValue = originalData[fieldName];
      const newValue = formValue[fieldName];

      if (originalValue === newValue) return;

      const processedValue = this.processFieldValue(newValue, originalValue, fieldName, {
        includeEmptyStrings,
        trimStrings,
        toLowerCase
      });

      if (processedValue !== undefined) {
        changes[fieldName] = processedValue;
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
      const value = this.processFormValue(formValue[fieldName], fieldName, {
        includeEmptyStrings,
        trimStrings,
        toLowerCase
      });

      if (value !== undefined) {
        formData[fieldName] = value;
      }
    });

    return formData;
  }

  private getFieldsToInclude(includeOptionalFields: boolean): string[] {
    return includeOptionalFields
      ? [...this.requiredFields, ...this.optionalFields]
      : this.requiredFields;
  }

  private buildFormConfig(fieldsToInclude: string[], isReadOnly: boolean): { [key: string]: any[] } {
    const formConfig: { [key: string]: any[] } = {};

    fieldsToInclude.forEach(fieldName => {
      const fieldConfig = this.fieldConfigs[fieldName];
      if (fieldConfig) {
        const validators = isReadOnly ? [] : fieldConfig.validators;
        formConfig[fieldName] = [fieldConfig.defaultValue, validators];
      }
    });

    return formConfig;
  }

  private addCustomValidators(form: FormGroup, customValidators: any[]): void {
    if (customValidators.length > 0) {
      customValidators.forEach(validator => {
        form.addValidators(validator);
      });
    }
  }

  private resetSpecificFields(form: FormGroup, fieldNames: string[]): void {
    fieldNames.forEach(fieldName => {
      const defaultValue = this.getFieldDefaultValue(fieldName);
      form.get(fieldName)?.setValue(defaultValue);
    });
  }

  private resetAllFields(form: FormGroup): void {
    const resetData: any = {};
    Object.keys(form.controls).forEach(fieldName => {
      resetData[fieldName] = this.getFieldDefaultValue(fieldName);
    });
    form.reset(resetData);
  }

  private processFieldValue(newValue: any, originalValue: any, fieldName: string, options: {
    includeEmptyStrings: boolean;
    trimStrings: boolean;
    toLowerCase: string[];
  }): any {
    const { includeEmptyStrings, trimStrings, toLowerCase } = options;

    if (typeof newValue === 'string' && typeof originalValue === 'string') {
      return this.processStringValue(newValue, originalValue, fieldName, options);
    } else if (typeof newValue === 'boolean' && typeof originalValue === 'boolean') {
      return newValue !== originalValue ? newValue : undefined;
    } else if (newValue !== originalValue) {
      return newValue || includeEmptyStrings ? newValue : undefined;
    }

    return undefined;
  }

  private processStringValue(newValue: string, originalValue: string, fieldName: string, options: {
    includeEmptyStrings: boolean;
    trimStrings: boolean;
    toLowerCase: string[];
  }): any {
    const { includeEmptyStrings, trimStrings, toLowerCase } = options;

    const compareStrings = (original: string | undefined, newVal: string | undefined) => {
      const orig = trimStrings ? original?.trim() : original;
      const newValTrimmed = trimStrings ? newVal?.trim() : newVal;
      return (orig || '') !== (newValTrimmed || '');
    };

    if (compareStrings(originalValue, newValue)) {
      let processedValue = newValue;
      if (trimStrings) processedValue = processedValue.trim();
      if (toLowerCase.includes(fieldName)) processedValue = processedValue.toLowerCase();
      if (processedValue || includeEmptyStrings) {
        return processedValue;
      }
    }

    return undefined;
  }

  private processFormValue(value: any, fieldName: string, options: {
    includeEmptyStrings: boolean;
    trimStrings: boolean;
    toLowerCase: string[];
  }): any {
    const { includeEmptyStrings, trimStrings, toLowerCase } = options;

    if (typeof value === 'string') {
      if (trimStrings) value = value.trim();
      if (toLowerCase.includes(fieldName)) value = value.toLowerCase();
      if (value || includeEmptyStrings) {
        return value;
      }
    } else if (value !== null && value !== undefined) {
      return value;
    }

    return undefined;
  }
}
