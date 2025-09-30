import { Injectable, inject } from '@angular/core';
import { FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { FormUtilsService } from '@shared/services/form-utils.service';
import { PasswordUtilsService } from '@shared/services/password-utils.service';
import { BaseFormBuilderService, FormConfig, FieldConfig } from '@shared/services/base-form-builder.service';

export interface UserFormConfig extends FormConfig {
  isEditMode?: boolean;
  includePasswordValidation?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class UserFormBuilderService extends BaseFormBuilderService {
  private formUtils = inject(FormUtilsService);
  private passwordUtils = inject(PasswordUtilsService);

  // Custom validator for roles - at least one role must be selected
  private rolesRequiredValidator(control: AbstractControl): ValidationErrors | null {
    const roles = control.value;
    if (!roles || !Array.isArray(roles) || roles.length === 0) {
      return { rolesRequired: true };
    }
    return null;
  }

  protected readonly fieldConfigs: { [key: string]: FieldConfig } = {
    username: {
      name: 'username',
      validators: [Validators.required, this.formUtils.validateUsername],
      defaultValue: '',
      isRequired: true
    },
    email: {
      name: 'email',
      validators: [Validators.required, Validators.pattern(FormUtilsService.EMAIL_PATTERN)],
      defaultValue: '',
      isRequired: true
    },
    password: {
      name: 'password',
      validators: [this.passwordUtils.passwordValidator],
      defaultValue: ''
    },
    confirmPassword: {
      name: 'confirmPassword',
      validators: [this.passwordUtils.passwordValidator],
      defaultValue: ''
    },
    selectedRoles: {
      name: 'selectedRoles',
      validators: [this.rolesRequiredValidator.bind(this)],
      defaultValue: [],
      isRequired: true
    },
    isActive: {
      name: 'isActive',
      validators: [],
      defaultValue: true
    }
  };

  protected readonly requiredFields = ['username', 'email', 'selectedRoles'];
  protected readonly optionalFields = ['password', 'confirmPassword', 'isActive'];

  buildUserForm(config: UserFormConfig = {}): FormGroup {
    const {
      isEditMode = false,
      includePasswordValidation = true,
      isReadOnly = false,
      customValidators = []
    } = config;

    const formConfig = this.buildUserFormConfig(isEditMode, isReadOnly);
    const form = this.fb.nonNullable.group(formConfig);

    this.addPasswordValidation(form, includePasswordValidation, isEditMode);
    this.addUserCustomValidators(form, customValidators);

    return form;
  }

  private buildUserFormConfig(isEditMode: boolean, isReadOnly: boolean): { [key: string]: any[] } {
    const formConfig: { [key: string]: any[] } = {};

    Object.keys(this.fieldConfigs).forEach(fieldName => {
      const fieldConfig = this.fieldConfigs[fieldName];
      if (fieldConfig) {
        let validators = [...fieldConfig.validators];

        if (this.isPasswordField(fieldName)) {
          validators = this.getPasswordValidators(fieldName, isEditMode);
        }

        if (isReadOnly) {
          validators = [];
        }

        formConfig[fieldName] = [fieldConfig.defaultValue, validators];
      }
    });

    return formConfig;
  }

  private isPasswordField(fieldName: string): boolean {
    return fieldName === 'password' || fieldName === 'confirmPassword';
  }

  private getPasswordValidators(fieldName: string, isEditMode: boolean): any[] {
    if (isEditMode) {
      // In edit mode, passwords are optional
      return [this.passwordUtils.passwordValidator];
    }
    // In creation mode, passwords are required
    return [Validators.required, this.passwordUtils.passwordValidator];
  }

  private addPasswordValidation(form: FormGroup, includePasswordValidation: boolean, isEditMode: boolean): void {
    if (includePasswordValidation) {
      if (!isEditMode) {
        // In creation mode, always validate password equality
        form.addValidators(FormUtilsService.validateFieldEquality('password', 'confirmPassword'));
      } else {
        // In edit mode, validate equality only if both fields have values
        const passwordControl = form.get('password');
        const confirmPasswordControl = form.get('confirmPassword');

        if (passwordControl && confirmPasswordControl) {
          const validatePasswordMatch = () => {
            const password = passwordControl.value;
            const confirmPassword = confirmPasswordControl.value;

            if (password && confirmPassword && password !== confirmPassword) {
              form.setErrors({ ...form.errors, ['fieldNotMatch']: true });
            } else {
              const errors = { ...form.errors };
              delete errors['fieldNotMatch'];
              form.setErrors(Object.keys(errors).length > 0 ? errors : null);
            }
          };

          passwordControl.valueChanges.subscribe(validatePasswordMatch);
          confirmPasswordControl.valueChanges.subscribe(validatePasswordMatch);
        }
      }
    }
  }

  private addUserCustomValidators(form: FormGroup, customValidators: any[]): void {
    if (customValidators.length > 0) {
      customValidators.forEach(validator => {
        form.addValidators(validator);
      });
    }
  }

  detectUserChanges(formValue: any, originalUser: any, options: {
    includeRoles?: boolean;
    includePassword?: boolean;
  } = {}): any {
    const { includeRoles = true, includePassword = true } = options;

    // Create a clean comparison object without role-related fields
    const comparisonValue = { ...formValue };
    delete comparisonValue.selectedRoles; // Remove roles field from comparison

    const changes = this.detectChanges(comparisonValue, originalUser, {
      trimStrings: true,
      toLowerCase: ['username', 'email']
    });

    this.addRoleChanges(changes, formValue, originalUser, includeRoles);
    this.addPasswordChanges(changes, formValue, includePassword);

    return changes;
  }

  private addRoleChanges(changes: any, formValue: any, originalUser: any, includeRoles: boolean): void {
    if (includeRoles) {
      const formRoles = formValue.selectedRoles || [];
      const originalRoles = originalUser.roles || [];

      // Convert to arrays and sort for comparison
      const formRolesSorted = [...formRoles].sort();
      const originalRolesSorted = [...originalRoles].sort();

      // Check if roles are different
      const rolesChanged = JSON.stringify(formRolesSorted) !== JSON.stringify(originalRolesSorted);

      if (rolesChanged) {
        changes.roles = formRoles;
      } else {
        // Remove roles from changes if they haven't changed
        if (changes.roles) {
          delete changes.roles;
        }
      }
    }
  }

  private addPasswordChanges(changes: any, formValue: any, includePassword: boolean): void {
    if (includePassword && formValue.password?.trim()) {
      changes.password = formValue.password.trim();
    }
  }

  prepareUserFormData(formValue: any): any {
    return super.prepareFormData(formValue, {
      trimStrings: true,
      toLowerCase: ['username', 'email']
    });
  }

  override patchForm(form: FormGroup, data: any): void {
    if (!data) return;

    const patchData = this.preparePatchData(data);
    super.patchForm(form, patchData);
  }

  private preparePatchData(data: any): any {
    const patchData = { ...data };

    // Handle multiple roles
    if (data.roles && Array.isArray(data.roles)) {
      patchData.selectedRoles = data.roles;
    } else if (data.selectedRoles && Array.isArray(data.selectedRoles)) {
      patchData.selectedRoles = data.selectedRoles;
    } else {
      patchData.selectedRoles = [];
    }

    patchData.password = '';
    patchData.confirmPassword = '';

    return patchData;
  }
}
