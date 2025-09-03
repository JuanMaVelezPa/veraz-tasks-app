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

  hasFieldError(form: FormGroup, fieldName: string): boolean | null {
    return form.controls[fieldName].errors && form.controls[fieldName].touched;
  }

  getFieldError(form: FormGroup, fieldName: string): string[] | null {
    if (!form.controls[fieldName]) return null;
    const errors = form.controls[fieldName].errors ?? {};
    return PasswordUtilsService.getTextErrorArray(errors);
  }

  passwordValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    const errors: ValidationErrors = {};

    if (value.length < PasswordUtilsService.MIN_LENGTH) {
      errors['minLength'] = true;
    }
    if (!/[a-z]/.test(value)) {
      errors['hasLowerCase'] = true;
    }
    if (!/[A-Z]/.test(value)) {
      errors['hasUpperCase'] = true;
    }
    if (!/\d/.test(value)) {
      errors['hasNumber'] = true;
    }
    if (!PasswordUtilsService.SPECIAL_CHARS.test(value)) {
      errors['hasSpecialChar'] = true;
    }

    return Object.keys(errors).length > 0 ? errors : null;
  }

  static getTextErrorArray(errors: ValidationErrors): string[] | null {
    const errorMessages: string[] = [];

    // No incluir 'required' aquí ya que se maneja por separado con Validators.required
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
