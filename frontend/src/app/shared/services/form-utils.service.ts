import { Injectable } from '@angular/core';
import { AbstractControl, FormArray, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { delay } from 'rxjs/operators';

async function sleep(seconds: number = 1): Promise<boolean> {
  return new Promise(resolve => setTimeout(() => {
    resolve(true);
  }, seconds * 1000));
}

@Injectable({
  providedIn: 'root'
})
export class FormUtilsService {

  static namePattern = '([a-zA-Z]+) ([a-zA-Z]+)';
  static emailPattern = '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$';
  static notOnlySpacesPattern = '^[a-zA-Z0-9]+$';
  static slugPattern = '^[a-z0-9_]+(?:-[a-z0-9_]+)*$';
  static usernamePattern = '^[a-zA-Z0-9_-]+$';

  constructor() { }

  isValidField(form: FormGroup, fieldName: string): boolean | null {
    const control = form.controls[fieldName];
    return control.errors && control.touched;
  }

  getFieldError(form: FormGroup, fieldName: string): string | null {
    if (!form.controls[fieldName]) return null;
    const errors = form.controls[fieldName].errors ?? {};
    return FormUtilsService.getTextError(errors);
  }

  isValidFieldInArray(formArray: FormArray, index: number): boolean | null {
    return formArray.controls[index].errors && formArray.controls[index].touched;
  }

  getFieldInArrayError(formArray: FormArray, index: number): string | null {
    if (!formArray.controls[index]) return null;
    const errors = formArray.controls[index].errors ?? {};
    return FormUtilsService.getTextError(errors);
  }

  usernameOrEmailValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    const errors: ValidationErrors = {};

    if (!value) { return null; }

    if (value.includes('@')) {
      const emailPatternError = Validators.pattern(FormUtilsService.emailPattern)(control);
      if (emailPatternError) {
        errors['pattern'] = emailPatternError['pattern'];
      }
    } else {

      const minLengthRequired = 3;
      const minLengthError = Validators.minLength(minLengthRequired)(control);
      if (minLengthError) {
        errors['minlength'] = minLengthError['minlength'];
      }

      const maxLengthRequired = 20;
      const maxLengthError = Validators.maxLength(maxLengthRequired)(control);
      if (maxLengthError) {
        errors['maxlength'] = maxLengthError['maxlength'];
      }
    }

    return Object.keys(errors).length ? errors : null;
  }

  usernameValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    const errors: ValidationErrors = {};

    if (!value) { return null; }

    const minLengthRequired = 3;
    const minLengthError = Validators.minLength(minLengthRequired)(control);
    if (minLengthError) {
      errors['minlength'] = minLengthError['minlength'];
    }

    const maxLengthRequired = 20;
    const maxLengthError = Validators.maxLength(maxLengthRequired)(control);
    if (maxLengthError) {
      errors['maxlength'] = maxLengthError['maxlength'];
    }

    const patternError = Validators.pattern(FormUtilsService.usernamePattern)(control);
    if (patternError) {
      errors['pattern'] = patternError['pattern'];
    }

    const reservedWordsError = FormUtilsService.notReservedWords(control);
    if (reservedWordsError) {
      errors['reservedWordUsed'] = reservedWordsError['reservedWordUsed'];
    }

    return Object.keys(errors).length ? errors : null;
  }

  confirmPasswordValidator(passwordField: string) {
    return (control: AbstractControl): ValidationErrors | null => {
      const password = control.parent?.get(passwordField)?.value;
      const confirmPassword = control.value;

      return password === confirmPassword ? null : { passwordMismatch: true };
    };
  }

  static isFieldOneEqualFieldTwo(field1: string, field2: string) {
    return (formGroup: AbstractControl) => {
      const field1Value = formGroup.get(field1)?.value;
      const field2Value = formGroup.get(field2)?.value;
      if (!field1Value || !field2Value) return null;
      return field1Value === field2Value ? null : { fieldNotMatch: true };
    }
  }

  static notReservedWords(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;
    const reservedWords = ['admin', 'user', 'strider', 'root', 'test'
      , 'guest', 'superadmin', 'super', 'manager', 'superuser', 'super-admin'
      , 'super-user', 'super-manager', 'veraz', 'employee', 'abc123', '123abc'
      , 'asdf', 'asdf123']
    const lowerCaseValue = value.toLowerCase();

    for (const word of reservedWords) {
      if (lowerCaseValue.includes(word.toLowerCase())) {
        return { 'reservedWordUsed': { word: word } };
      }
    }
    return null;
  }

  static async validateCredentials(control: AbstractControl): Promise<ValidationErrors | null> {
    console.log('Validating credentials against server...');
    await sleep(1);

    const formValue = control.value;
    // Simulación de validación - aquí iría la llamada real al servidor
    if (formValue === 'test@test.com') {
      return { invalidCredentials: true };
    }
    return null;
  }

  markFormGroupTouched(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
    });
  }

  resetForm(formGroup: FormGroup) {
    formGroup.reset();
  }

  hasFormErrors(formGroup: FormGroup): boolean {
    return formGroup.invalid && formGroup.touched;
  }

  getFormErrors(formGroup: FormGroup): { [key: string]: string } {
    const errors: { [key: string]: string } = {};
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      if (control?.errors) {
        errors[key] = FormUtilsService.getTextError(control.errors) || '';
      }
    });
    return errors;
  }

  validateUsernameAvailability(username: string): Observable<boolean> {
    return of(true).pipe(delay(500));
  }

  static getTextError(errors: ValidationErrors): string | null {
    for (const key of Object.keys(errors)) {
      switch (key) {
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
          if (errors['pattern']?.requiredPattern === FormUtilsService.namePattern) {
            return 'Name must be in the format of first and last name.';
          }
          if (errors['pattern']?.requiredPattern === FormUtilsService.emailPattern) {
            return 'Invalid email.';
          }
          if (errors['pattern']?.requiredPattern === FormUtilsService.notOnlySpacesPattern) {
            return 'Name cannot contain spaces.';
          }
          if (errors['pattern']?.requiredPattern === FormUtilsService.slugPattern) {
            return 'Slug cannot contain spaces.';
          }
          if (errors['pattern']?.requiredPattern === FormUtilsService.usernamePattern) {
            return 'Username can only contain letters, numbers, hyphens and underscores.';
          }
          return `The format is not correct`;
        default:
          return `Uncontrolled validation error: ${key}`;
      }
    }
    return null;
  }
}
