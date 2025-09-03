import { Injectable } from '@angular/core';
import { AbstractControl, FormArray, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { delay } from 'rxjs/operators';

const DELAY_SECONDS = 1;
const MIN_USERNAME_LENGTH = 3;
const MAX_USERNAME_LENGTH = 20;
const RESERVED_WORDS = [
  'admin', 'user', 'strider', 'root', 'test', 'guest', 'superadmin',
  'super', 'manager', 'superuser', 'super-admin', 'super-user',
  'super-manager', 'veraz', 'employee', 'abc123', '123abc', 'asdf', 'asdf123'
];

@Injectable({
  providedIn: 'root'
})
export class FormUtilsService {

  static readonly NAME_PATTERN = '([a-zA-Z]+) ([a-zA-Z]+)';
  static readonly EMAIL_PATTERN = '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$';
  static readonly NO_SPACES_PATTERN = '^[a-zA-Z0-9]+$';
  static readonly SLUG_PATTERN = '^[a-z0-9_]+(?:-[a-z0-9_]+)*$';
  static readonly USERNAME_PATTERN = '^[a-zA-Z0-9_-]+$';

  hasFieldError(form: FormGroup, fieldName: string): boolean | null {
    const field = form.controls[fieldName];
    return field.errors && field.touched;
  }

  getFieldErrorMessage(form: FormGroup, fieldName: string): string | null {
    const field = form.controls[fieldName];
    if (!field) return null;

    const errors = field.errors ?? {};
    return FormUtilsService.getErrorMessage(errors);
  }

  hasArrayFieldError(formArray: FormArray, index: number): boolean | null {
    const field = formArray.controls[index];
    return field.errors && field.touched;
  }

  getArrayFieldErrorMessage(formArray: FormArray, index: number): string | null {
    const field = formArray.controls[index];
    if (!field) return null;

    const errors = field.errors ?? {};
    return FormUtilsService.getErrorMessage(errors);
  }

  validateUsernameOrEmail(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    const errors: ValidationErrors = {};

    if (value.includes('@')) {
      const emailError = Validators.pattern(FormUtilsService.EMAIL_PATTERN)(control);
      if (emailError) {
        errors['pattern'] = emailError['pattern'];
      }
    } else {
      const minLengthError = Validators.minLength(MIN_USERNAME_LENGTH)(control);
      if (minLengthError) {
        errors['minlength'] = minLengthError['minlength'];
      }

      const maxLengthError = Validators.maxLength(MAX_USERNAME_LENGTH)(control);
      if (maxLengthError) {
        errors['maxlength'] = maxLengthError['maxlength'];
      }
    }

    return Object.keys(errors).length ? errors : null;
  }

  validateUsername(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    const errors: ValidationErrors = {};

    const minLengthError = Validators.minLength(MIN_USERNAME_LENGTH)(control);
    if (minLengthError) {
      errors['minlength'] = minLengthError['minlength'];
    }

    const maxLengthError = Validators.maxLength(MAX_USERNAME_LENGTH)(control);
    if (maxLengthError) {
      errors['maxlength'] = maxLengthError['maxlength'];
    }

    const patternError = Validators.pattern(FormUtilsService.USERNAME_PATTERN)(control);
    if (patternError) {
      errors['pattern'] = patternError['pattern'];
    }

    const reservedWordError = FormUtilsService.checkReservedWords(control);
    if (reservedWordError) {
      errors['reservedWordUsed'] = reservedWordError['reservedWordUsed'];
    }

    return Object.keys(errors).length ? errors : null;
  }

  validatePasswordConfirmation(passwordFieldName: string) {
    return (control: AbstractControl): ValidationErrors | null => {
      const password = control.parent?.get(passwordFieldName)?.value;
      const confirmPassword = control.value;

      return password === confirmPassword ? null : { passwordMismatch: true };
    };
  }

  static validateFieldEquality(field1Name: string, field2Name: string) {
    return (formGroup: AbstractControl) => {
      const field1Value = formGroup.get(field1Name)?.value;
      const field2Value = formGroup.get(field2Name)?.value;

      if (!field1Value || !field2Value) return null;
      return field1Value === field2Value ? null : { fieldNotMatch: true };
    }
  }

  static checkReservedWords(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    const lowerCaseValue = value.toLowerCase();

    for (const reservedWord of RESERVED_WORDS) {
      if (lowerCaseValue.includes(reservedWord.toLowerCase())) {
        return { 'reservedWordUsed': { word: reservedWord } };
      }
    }
    return null;
  }

  static async validateCredentials(control: AbstractControl): Promise<ValidationErrors | null> {
    await this.delay(DELAY_SECONDS);

    const value = control.value;
    if (value === 'test@test.com') {
      return { invalidCredentials: true };
    }
    return null;
  }

  markFormAsTouched(form: FormGroup) {
    Object.keys(form.controls).forEach(key => {
      const control = form.get(key);
      control?.markAsTouched();
    });
  }

  resetForm(form: FormGroup) {
    form.reset();
  }

  hasFormErrors(form: FormGroup): boolean {
    return form.invalid && form.touched;
  }

  getFormErrorMessages(form: FormGroup): { [key: string]: string } {
    const errorMessages: { [key: string]: string } = {};

    Object.keys(form.controls).forEach(key => {
      const control = form.get(key);
      if (control?.errors) {
        errorMessages[key] = FormUtilsService.getErrorMessage(control.errors) || '';
      }
    });

    return errorMessages;
  }

  checkUsernameAvailability(username: string): Observable<boolean> {
    return of(true).pipe(delay(500));
  }

  static toTitleCase(text: string): string {
    if (!text) return text;
    return text.toLowerCase().replace(/\b\w/g, (char) => char.toUpperCase());
  }

  static getErrorMessage(errors: ValidationErrors): string | null {
    for (const errorType of Object.keys(errors)) {
      switch (errorType) {
        case 'required':
          return 'This field is required.';
        case 'minlength':
          return `Minimum ${errors['minlength']?.requiredLength} characters.`;
        case 'min':
          return `Minimum ${errors['min']?.min}`;
        case 'maxlength':
          return `Maximum ${errors['maxlength']?.requiredLength} characters`;
        case 'max':
          return `Maximum ${errors['max']?.max}`;
        case 'reservedWordUsed':
          return `The word ${errors['reservedWordUsed']?.word} is reserved.`;
        case 'fieldNotMatch':
          return 'Passwords do not match';
        case 'pattern':
          return this.getPatternErrorMessage(errors['pattern']?.requiredPattern);
        default:
          return `Uncontrolled validation error: ${errorType}`;
      }
    }
    return null;
  }

  private static getPatternErrorMessage(pattern: string): string {
    switch (pattern) {
      case FormUtilsService.NAME_PATTERN:
        return 'Name must be in the format of first and last name.';
      case FormUtilsService.EMAIL_PATTERN:
        return 'Invalid email.';
      case FormUtilsService.NO_SPACES_PATTERN:
        return 'Name cannot contain spaces.';
      case FormUtilsService.SLUG_PATTERN:
        return 'Slug cannot contain spaces.';
      case FormUtilsService.USERNAME_PATTERN:
        return 'Username can only contain letters, numbers, hyphens and underscores.';
      default:
        return 'The format is not correct';
    }
  }

  private static delay(seconds: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, seconds * 1000));
  }
}
