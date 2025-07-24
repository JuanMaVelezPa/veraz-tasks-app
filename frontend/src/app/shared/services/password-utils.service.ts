import { Injectable } from '@angular/core';
import { FormGroup, ValidationErrors, AbstractControl, ValidatorFn } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class PasswordUtilsService {

  private static readonly SPECIAL_CHARS = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/;
  private static readonly MIN_LENGTH = 8;

  constructor() { }

  isValidField(form: FormGroup, fieldName: string): boolean | null {
    return form.controls[fieldName].errors && form.controls[fieldName].touched;
  }

  getFieldError(form: FormGroup, fieldName: string): string[] | null {
    if (!form.controls[fieldName]) return null;
    const errors = form.controls[fieldName].errors ?? {};
    return PasswordUtilsService.getTextErrorArray(errors);
  }

  passwordValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    const errors: ValidationErrors = {};

    if (!value) return null;
    if (PasswordUtilsService.isRequired()(control)) { errors['required'] = true; }
    if (PasswordUtilsService.isMinLength()(control)) { errors['minLength'] = true; }
    if (PasswordUtilsService.hasLowerCase()(control)) { errors['hasLowerCase'] = true; }
    if (PasswordUtilsService.hasUpperCase()(control)) { errors['hasUpperCase'] = true; }
    if (PasswordUtilsService.hasNumber()(control)) { errors['hasNumber'] = true; }
    if (PasswordUtilsService.hasSpecialChar()(control)) { errors['hasSpecialChar'] = true; }
    return Object.keys(errors).length > 0 ? errors : null;
  }

  static isRequired(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (!value) return { required: true };
      return null;
    };
  }

  static isMinLength(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (!value) return null;
      if (value.length < PasswordUtilsService.MIN_LENGTH) {
        return { minLength: true };
      }
      return null;
    };
  }

  static hasLowerCase(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (!value) return null;
      const hasLower = /[a-z]/.test(value);
      return hasLower ? null : { hasLowerCase: true };
    };
  }

  static hasUpperCase(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (!value) return null;
      const hasUpper = /[A-Z]/.test(value);
      return hasUpper ? null : { hasUpperCase: true };
    };
  }

  static hasNumber(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (!value) return null;

      const hasNumber = /\d/.test(value);
      return hasNumber ? null : { hasNumber: true };
    };
  }

  static hasSpecialChar(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (!value) return null;

      const hasSpecial = PasswordUtilsService.SPECIAL_CHARS.test(value);
      return hasSpecial ? null : { hasSpecialChar: true };
    };
  }

  static getTextErrorArray(errors: ValidationErrors): string[] | null {
    const errorMessages: string[] = [];

    if (errors['required']) {
      errorMessages.push('Password is required');
    }
    if (errors['minLength']) {
      errorMessages.push(`Minimum ${PasswordUtilsService.MIN_LENGTH} characters`);
    }
    if (errors['hasLowerCase']) {
      errorMessages.push('Missing lowercase letter');
    }
    if (errors['hasUpperCase']) {
      errorMessages.push('Missing uppercase letter');
    }
    if (errors['hasNumber']) {
      errorMessages.push('Missing number');
    }
    if (errors['hasSpecialChar']) {
      errorMessages.push('Missing special character');
    }

    return errorMessages.length > 0 ? errorMessages : null;
  }
}
