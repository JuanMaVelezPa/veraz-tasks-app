import { Injectable, inject } from '@angular/core';
import { Observable, firstValueFrom, catchError } from 'rxjs';
import { Person, PersonFormData, PersonManagementOptions } from '@person/interfaces/person.interface';
import { PersonService } from '@person/services/person.service';
import { ProfileService } from '@profile/services/profile.service';
import { HttpErrorService } from '@shared/services/http-error.service';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { PersonFormBuilderService } from '@person/services/person-form-builder.service';

@Injectable({
  providedIn: 'root'
})
export class PersonManagementService {
  private personService = inject(PersonService);
  private profileService = inject(ProfileService);
  private httpErrorService = inject(HttpErrorService);
  private feedbackService = inject(FeedbackMessageService);
  private personFormBuilder = inject(PersonFormBuilderService);

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
      const changes = this.personFormBuilder.detectPersonChanges(formData, originalPerson);

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

}
