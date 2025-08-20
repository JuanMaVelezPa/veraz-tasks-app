import { Injectable, inject } from '@angular/core';
import { Observable, firstValueFrom, catchError } from 'rxjs';
import { Person, PersonFormData, PersonManagementOptions } from '@person/interfaces/person.interface';
import { PersonService } from '@person/services/person.service';
import { ProfileService } from '@profile/services/profile.service';
import { HttpErrorService } from '@shared/services/http-error.service';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';

@Injectable({
  providedIn: 'root'
})
export class PersonManagementService {
  private personService = inject(PersonService);
  private profileService = inject(ProfileService);
  private httpErrorService = inject(HttpErrorService);
  private feedbackService = inject(FeedbackMessageService);

  async createPerson(formData: PersonFormData, options: PersonManagementOptions): Promise<Person> {
    try {
      let response: Observable<any>;

      if (options.context === 'profile') {
        response = this.profileService.createMyProfile(formData);
      } else {
        const personData = { ...formData, ...(options.userId && { userId: options.userId }) };
        response = this.personService.createPerson(personData);
      }

      const result = await firstValueFrom(
        response.pipe(
          catchError(error => this.httpErrorService.handleError(error, 'creating person'))
        )
      );

      const person = result.data || result;

      this.feedbackService.showSuccess(
        options.context === 'profile' ? 'Personal information created successfully!' : 'Person created successfully'
      );

      options.onSuccess?.(person);
      return person;

    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while creating the person.');
      options.onError?.(error);
      throw error;
    }
  }

  async updatePerson(originalPerson: Person, formData: PersonFormData, options: PersonManagementOptions): Promise<Person> {
    try {
      const changes = this.detectChanges(formData, originalPerson);

      if (Object.keys(changes).length === 0) {
        this.feedbackService.showWarning('No changes detected. Nothing to save.');
        return originalPerson;
      }

      let response: Observable<any>;

      if (options.context === 'profile') {
        response = this.profileService.updateMyProfile(changes);
      } else {
        response = this.personService.updatePerson(originalPerson.id, changes);
      }

      const result = await firstValueFrom(
        response.pipe(
          catchError(error => this.httpErrorService.handleError(error, 'updating person'))
        )
      );

      // Extract person from result (can be Person or ApiResponse<Person>)
      const person = result.data || result;

      this.feedbackService.showSuccess(
        options.context === 'profile' ? 'Personal information updated successfully!' : 'Person updated successfully'
      );

      options.onSuccess?.(person);
      return person;

    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while updating the person.');
      options.onError?.(error);
      throw error;
    }
  }

  validateRequiredFields(formData: PersonFormData): boolean {
    if (!formData.identType?.trim() || !formData.identNumber?.trim() ||
      !formData.firstName?.trim() || !formData.lastName?.trim()) {
      this.feedbackService.showError('Identification, first name and last name are required.');
      return false;
    }
    return true;
  }

  private detectChanges(formData: PersonFormData, originalPerson: Person): any {
    const changes: any = {};
    const compareStrings = (original: string | undefined, newValue: string | undefined) => {
      return (original?.trim() || '') !== (newValue?.trim() || '');
    };

    if (compareStrings(originalPerson.identType, formData.identType)) changes.identType = formData.identType?.trim();
    if (compareStrings(originalPerson.identNumber, formData.identNumber)) changes.identNumber = formData.identNumber?.trim();
    if (compareStrings(originalPerson.firstName, formData.firstName)) changes.firstName = formData.firstName?.trim();
    if (compareStrings(originalPerson.lastName, formData.lastName)) changes.lastName = formData.lastName?.trim();
    if (formData.birthDate !== originalPerson.birthDate) changes.birthDate = formData.birthDate || undefined;
    if (formData.gender !== originalPerson.gender) changes.gender = formData.gender || undefined;
    if (compareStrings(originalPerson.nationality, formData.nationality)) changes.nationality = formData.nationality?.trim() || undefined;
    if (compareStrings(originalPerson.mobile, formData.mobile)) changes.mobile = formData.mobile?.trim() || undefined;
    if (compareStrings(originalPerson.email, formData.email)) changes.email = formData.email?.trim().toLowerCase() || undefined;
    if (compareStrings(originalPerson.address, formData.address)) changes.address = formData.address?.trim() || undefined;
    if (compareStrings(originalPerson.city, formData.city)) changes.city = formData.city?.trim() || undefined;
    if (compareStrings(originalPerson.country, formData.country)) changes.country = formData.country?.trim() || undefined;
    if (compareStrings(originalPerson.postalCode, formData.postalCode)) changes.postalCode = formData.postalCode?.trim() || undefined;
    if (compareStrings(originalPerson.notes, formData.notes)) changes.notes = formData.notes?.trim() || undefined;
    if (formData.isActive !== originalPerson.isActive) changes.isActive = formData.isActive;

    return changes;
  }

  prepareFormData(formValue: any): PersonFormData {
    return {
      identType: formValue.identType?.trim() || '',
      identNumber: formValue.identNumber?.trim() || '',
      firstName: formValue.firstName?.trim() || '',
      lastName: formValue.lastName?.trim() || '',
      birthDate: formValue.birthDate || undefined,
      gender: formValue.gender || undefined,
      nationality: formValue.nationality?.trim() || undefined,
      mobile: formValue.mobile?.trim() || undefined,
      email: formValue.email?.trim().toLowerCase() || undefined,
      address: formValue.address?.trim() || undefined,
      city: formValue.city?.trim() || undefined,
      country: formValue.country?.trim() || undefined,
      postalCode: formValue.postalCode?.trim() || undefined,
      notes: formValue.notes?.trim() || undefined,
      isActive: formValue.isActive ?? true
    };
  }
}
