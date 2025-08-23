import { Injectable, inject } from '@angular/core';
import { FormGroup, Validators } from '@angular/forms';
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

  protected readonly fieldConfigs: { [key: string]: FieldConfig } = {
    username: {
      name: 'username',
      validators: [Validators.required, this.formUtils.usernameValidator],
      defaultValue: '',
      isRequired: true
    },
    email: {
      name: 'email',
      validators: [Validators.required, Validators.pattern(FormUtilsService.emailPattern)],
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
    selectedRole: {
      name: 'selectedRole',
      validators: [Validators.required],
      defaultValue: '',
      isRequired: true
    },
    isActive: {
      name: 'isActive',
      validators: [],
      defaultValue: true
    }
  };

  protected readonly requiredFields = ['username', 'email', 'selectedRole'];
  protected readonly optionalFields = ['password', 'confirmPassword', 'isActive'];

  buildUserForm(config: UserFormConfig = {}): FormGroup {
    const {
      isEditMode = false,
      includePasswordValidation = true,
      isReadOnly = false,
      customValidators = []
    } = config;

    const formConfig: { [key: string]: any[] } = {};

    Object.keys(this.fieldConfigs).forEach(fieldName => {
      const fieldConfig = this.fieldConfigs[fieldName];
      if (fieldConfig) {
        let validators = [...fieldConfig.validators];

        if (fieldName === 'password' || fieldName === 'confirmPassword') {
          if (isEditMode) {
            validators = [this.passwordUtils.passwordValidator];
          } else {
            validators = [Validators.required, this.passwordUtils.passwordValidator];
          }
        }

        if (isReadOnly) {
          validators = [];
        }

        formConfig[fieldName] = [fieldConfig.defaultValue, validators];
      }
    });

    const form = this.fb.nonNullable.group(formConfig);

    if (includePasswordValidation) {
      form.addValidators(FormUtilsService.isFieldOneEqualFieldTwo('password', 'confirmPassword'));
    }

    if (customValidators.length > 0) {
      customValidators.forEach(validator => {
        form.addValidators(validator);
      });
    }

    return form;
  }

  detectUserChanges(formValue: any, originalUser: any, options: {
    includeRoles?: boolean;
    includePassword?: boolean;
  } = {}): any {
    const { includeRoles = true, includePassword = true } = options;

    const comparisonValue = { ...formValue };

    if (includeRoles && originalUser.roles) {
      const currentRole = originalUser.roles.length > 0 ? originalUser.roles[0] : '';
      comparisonValue.selectedRole = currentRole;
    }

    const changes = this.detectChanges(comparisonValue, originalUser, {
      trimStrings: true,
      toLowerCase: ['username', 'email']
    });

    if (includeRoles && formValue.selectedRole !== originalUser.roles?.[0]) {
      changes.roles = formValue.selectedRole ? [formValue.selectedRole] : [];
    }

    if (includePassword && formValue.password?.trim()) {
      changes.password = formValue.password.trim();
    }

    return changes;
  }

  prepareUserFormData(formValue: any): any {
    return this.prepareFormData(formValue, {
      trimStrings: true,
      toLowerCase: ['username', 'email']
    });
  }

    override patchForm(form: FormGroup, data: any): void {
    if (!data) return;

    const patchData = { ...data };

    // Handle roles array conversion to selectedRole
    if (data.roles && Array.isArray(data.roles)) {
      patchData.selectedRole = data.roles.length > 0 ? data.roles[0] : '';
    }

    // Ensure password fields are empty for security
    patchData.password = '';
    patchData.confirmPassword = '';

    super.patchForm(form, patchData);
  }
}
