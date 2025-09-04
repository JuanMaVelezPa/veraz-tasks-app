import { Injectable, inject } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { PersonFormBuilderService, PersonFormConfig } from '@person/services/person-form-builder.service';
import { EmployeeFormBuilderService, EmployeeFormConfig } from '@employee/services/employee-form-builder.service';
import { ClientFormBuilderService, ClientFormConfig } from '@client/services/client-form-builder.service';
import { UserFormBuilderService, UserFormConfig } from '@users/services/user-form-builder.service';

@Injectable({
  providedIn: 'root'
})
export class FormBuildersManagerService {
  private personFormBuilder = inject(PersonFormBuilderService);
  private employeeFormBuilder = inject(EmployeeFormBuilderService);
  private clientFormBuilder = inject(ClientFormBuilderService);
  private userFormBuilder = inject(UserFormBuilderService);

  buildPersonForm(config: PersonFormConfig = {}): FormGroup {
    return this.personFormBuilder.buildPersonForm(config);
  }

  buildEmployeeForm(config: EmployeeFormConfig = {}): FormGroup {
    return this.employeeFormBuilder.buildEmployeeForm(config);
  }

  buildClientForm(config: ClientFormConfig = {}): FormGroup {
    return this.clientFormBuilder.buildClientForm(config);
  }

  buildUserForm(config: UserFormConfig = {}): FormGroup {
    return this.userFormBuilder.buildUserForm(config);
  }

  patchForm(form: FormGroup, data: any): void {
    if (!form || !data) return;

    const formKeys = Object.keys(form.controls);

    if (this.isPersonForm(formKeys)) {
      this.personFormBuilder.patchForm(form, data);
    } else if (this.isEmployeeForm(formKeys)) {
      this.employeeFormBuilder.patchForm(form, data);
    } else if (this.isUserForm(formKeys)) {
      this.userFormBuilder.patchForm(form, data);
    }
  }

  resetForm(form: FormGroup, fieldNames?: string[]): void {
    if (!form) return;

    const formKeys = Object.keys(form.controls);

    if (this.isPersonForm(formKeys)) {
      this.personFormBuilder.resetForm(form, fieldNames);
    } else if (this.isEmployeeForm(formKeys)) {
      this.employeeFormBuilder.resetForm(form, fieldNames);
    } else if (this.isClientForm(formKeys)) {
      this.clientFormBuilder.resetForm(form, fieldNames);
    } else if (this.isUserForm(formKeys)) {
      this.userFormBuilder.resetForm(form, fieldNames);
    }
  }

  detectUserChanges(formValue: any, originalUser: any, options?: any): any {
    return this.userFormBuilder.detectUserChanges(formValue, originalUser, options);
  }

  detectPersonChanges(formValue: any, originalPerson: any): any {
    return this.personFormBuilder.detectPersonChanges(formValue, originalPerson);
  }

  prepareUserFormData(formValue: any): any {
    return this.userFormBuilder.prepareUserFormData(formValue);
  }

  preparePersonFormData(formValue: any): any {
    return this.personFormBuilder.preparePersonFormData(formValue);
  }

  prepareEmployeeFormData(formValue: any): any {
    return this.employeeFormBuilder.prepareEmployeeFormData(formValue);
  }

  prepareClientFormData(formValue: any): any {
    return this.clientFormBuilder.prepareClientFormData(formValue);
  }

  validateRequiredPersonFields(formData: any): { isValid: boolean; missingFields: string[] } {
    return this.personFormBuilder.validateRequiredPersonFields(formData);
  }

  isFieldRequired(form: FormGroup, fieldName: string): boolean {
    if (!form) return false;

    const formKeys = Object.keys(form.controls);

    if (this.isPersonForm(formKeys)) {
      return this.personFormBuilder.isFieldRequired(fieldName);
    } else if (this.isEmployeeForm(formKeys)) {
      return this.employeeFormBuilder.isFieldRequired(fieldName);
    } else if (this.isUserForm(formKeys)) {
      return this.userFormBuilder.isFieldRequired(fieldName);
    }

    return false;
  }

  getFieldValidators(form: FormGroup, fieldName: string): any[] {
    if (!form) return [];

    const formKeys = Object.keys(form.controls);

    if (this.isPersonForm(formKeys)) {
      return this.personFormBuilder.getFieldValidators(fieldName);
    } else if (this.isEmployeeForm(formKeys)) {
      return this.employeeFormBuilder.getFieldValidators(fieldName);
    } else if (this.isUserForm(formKeys)) {
      return this.userFormBuilder.getFieldValidators(fieldName);
    }

    return [];
  }

  getFieldDefaultValue(form: FormGroup, fieldName: string): any {
    if (!form) return null;

    const formKeys = Object.keys(form.controls);

    if (this.isPersonForm(formKeys)) {
      return this.personFormBuilder.getFieldDefaultValue(fieldName);
    } else if (this.isEmployeeForm(formKeys)) {
      return this.employeeFormBuilder.getFieldDefaultValue(fieldName);
    } else if (this.isUserForm(formKeys)) {
      return this.userFormBuilder.getFieldDefaultValue(fieldName);
    }

    return null;
  }

  private isPersonForm(formKeys: string[]): boolean {
    const personFields = ['identType', 'identNumber', 'firstName', 'lastName'];
    return personFields.some(field => formKeys.includes(field));
  }

  private isEmployeeForm(formKeys: string[]): boolean {
    const employeeFields = ['position', 'employmentType', 'hireDate'];
    return employeeFields.some(field => formKeys.includes(field));
  }

  private isClientForm(formKeys: string[]): boolean {
    const clientFields = ['type', 'status'];
    return clientFields.some(field => formKeys.includes(field));
  }

  private isUserForm(formKeys: string[]): boolean {
    const userFields = ['username', 'email', 'password', 'selectedRole'];
    return userFields.some(field => formKeys.includes(field));
  }

}
