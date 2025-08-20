import { Injectable, inject } from '@angular/core';
import { Observable, firstValueFrom, catchError } from 'rxjs';
import { Person, PersonFormData, PersonManagementOptions } from '@person/interfaces/person.interface';
import { PersonService } from '@person/services/person.service';
import { ProfileService } from '@profile/services/profile.service';
import { HttpErrorService } from '@shared/services/http-error.service';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { FormUtilsService } from '@shared/services/form-utils.service';

/**
 * Unified service for person management across admin and profile contexts
 * Handles creation, updates, and validation with context-aware logic
 */
@Injectable({
  providedIn: 'root'
})
export class PersonManagementService {
  private personService = inject(PersonService);
  private profileService = inject(ProfileService);
  private httpErrorService = inject(HttpErrorService);
  private feedbackService = inject(FeedbackMessageService);

  /**
   * Creates a person with context-aware logic
   * In profile context: creates personal profile for current user
   * In admin context: creates person with optional user association
   *
   * @param formData The person data to create
   * @param options Management options including context and callbacks
   * @returns Promise<Person> The created person
   */
  async createPerson(formData: PersonFormData, options: PersonManagementOptions): Promise<Person> {
    try {
      let response: Observable<any>;

      if (options.context === 'profile') {
        // In profile context, never include userId - the person will be associated with current user
        response = this.profileService.createMyProfile(formData);
      } else {
        // In admin context, include userId if provided for association
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

  /**
   * Updates a person with context-aware logic and change detection
   * Only sends changed fields to optimize API calls
   * User association is never updated through this method
   *
   * @param originalPerson The original person data
   * @param formData The updated form data
   * @param options Management options including context and callbacks
   * @returns Promise<Person> The updated person
   */
  async updatePerson(originalPerson: Person, formData: PersonFormData, options: PersonManagementOptions): Promise<Person> {
    try {
      const changes = this.detectChanges(formData, originalPerson, options.context);

      if (Object.keys(changes).length === 0) {
        this.feedbackService.showWarning('No changes detected. Nothing to save.');
        return originalPerson;
      }

      let response: Observable<any>;

      if (options.context === 'profile') {
        // In profile context, changes object never includes userId (handled in detectChanges)
        response = this.profileService.updateMyProfile(changes);
      } else {
        // In admin context, changes may include userId for association changes
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

  /**
   * Validates required fields for person creation/update
   *
   * @param formData The form data to validate
   * @returns boolean True if validation passes, false otherwise
   */
  validateRequiredFields(formData: PersonFormData): boolean {
    if (!formData.identType?.trim() || !formData.identNumber?.trim() ||
      !formData.firstName?.trim() || !formData.lastName?.trim()) {
      this.feedbackService.showError('Identification, first name and last name are required.');
      return false;
    }
    return true;
  }

  /**
   * Detects changes between original person data and form data
   * Applies title case to names and optimizes field comparison
   * Never includes user association changes
   *
   * @param formData The form data to compare
   * @param originalPerson The original person data
   * @param context The operation context (admin or profile)
   * @returns Object containing only changed fields
   */
  private detectChanges(formData: PersonFormData, originalPerson: Person, context: 'admin' | 'profile'): any {
    const changes: any = {};

    // Improved comparison function that handles title case and null/undefined values
    const compareStrings = (original: string | undefined, newValue: string | undefined) => {
      const originalNormalized = (original?.trim() || '').toLowerCase();
      const newNormalized = (newValue?.trim() || '').toLowerCase();
      return originalNormalized !== newNormalized;
    };

    // Compare and add only changed fields
    if (compareStrings(originalPerson.identType, formData.identType)) {
      changes.identType = formData.identType?.trim();
    }

    if (compareStrings(originalPerson.identNumber, formData.identNumber)) {
      changes.identNumber = formData.identNumber?.trim();
    }

    if (compareStrings(originalPerson.firstName, formData.firstName)) {
      changes.firstName = FormUtilsService.toTitleCase(formData.firstName?.trim() || '');
    }

    if (compareStrings(originalPerson.lastName, formData.lastName)) {
      changes.lastName = FormUtilsService.toTitleCase(formData.lastName?.trim() || '');
    }

    if (formData.birthDate !== originalPerson.birthDate) {
      changes.birthDate = formData.birthDate || undefined;
    }

    if (formData.gender !== originalPerson.gender) {
      changes.gender = formData.gender || undefined;
    }

    if (compareStrings(originalPerson.nationality, formData.nationality)) {
      changes.nationality = formData.nationality?.trim() || undefined;
    }

    if (compareStrings(originalPerson.mobile, formData.mobile)) {
      changes.mobile = formData.mobile?.trim() || undefined;
    }

    if (compareStrings(originalPerson.email, formData.email)) {
      changes.email = formData.email?.trim().toLowerCase() || undefined;
    }

    if (compareStrings(originalPerson.address, formData.address)) {
      changes.address = formData.address?.trim() || undefined;
    }

    if (compareStrings(originalPerson.city, formData.city)) {
      changes.city = formData.city?.trim() || undefined;
    }

    if (compareStrings(originalPerson.country, formData.country)) {
      changes.country = formData.country?.trim() || undefined;
    }

    if (compareStrings(originalPerson.postalCode, formData.postalCode)) {
      changes.postalCode = formData.postalCode?.trim() || undefined;
    }

    if (compareStrings(originalPerson.notes, formData.notes)) {
      changes.notes = formData.notes?.trim() || undefined;
    }

    if (formData.isActive !== originalPerson.isActive) {
      changes.isActive = formData.isActive;
    }

    // IMPORTANT: User association is never updated through regular person updates
    // User association is handled through dedicated endpoints only
    // This ensures clear separation of concerns and prevents accidental disassociation

    return changes;
  }

  /**
   * Prepares form data by applying formatting and validation
   * Applies title case to names and trims all string fields
   *
   * @param formValue The raw form value
   * @returns PersonFormData The prepared form data
   */
  prepareFormData(formValue: any): PersonFormData {
    return {
      identType: formValue.identType?.trim() || '',
      identNumber: formValue.identNumber?.trim() || '',
      firstName: FormUtilsService.toTitleCase(formValue.firstName?.trim() || ''),
      lastName: FormUtilsService.toTitleCase(formValue.lastName?.trim() || ''),
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
