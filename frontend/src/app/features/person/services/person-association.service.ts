import { Injectable, inject } from '@angular/core';
import { PersonService } from '@person/services/person.service';
import { PersonUpdateRequest } from '@person/interfaces/person.interface';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { NavigationHistoryService } from '@shared/services/navigation-history.service';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PersonAssociationService {
  private personService = inject(PersonService);
  private feedbackService = inject(FeedbackMessageService);
  private navigationHistory = inject(NavigationHistoryService);

  async associatePersonWithUser(personId: string, userId: string, personName: string): Promise<void> {
    try {
      const updateData: PersonUpdateRequest = { userId };
      await firstValueFrom(this.personService.updatePerson(personId, updateData));
      this.feedbackService.showSuccess(`Person ${personName} linked successfully`);
      this.navigationHistory.goBackToUser(userId);
    } catch (error) {
      this.feedbackService.showError('Failed to link person with user');
    }
  }

  async disassociatePerson(personId: string, personName: string): Promise<void> {
    try {
      const updateData: PersonUpdateRequest = { userId: null };
      await firstValueFrom(this.personService.updatePerson(personId, updateData));
      this.feedbackService.showSuccess('Person disassociated successfully');
    } catch (error) {
      this.feedbackService.showError('Failed to disassociate person');
    }
  }

  async changePersonAssociation(personId: string, userId: string): Promise<void> {
    try {
      const updateData: PersonUpdateRequest = { userId: null };
      await firstValueFrom(this.personService.updatePerson(personId, updateData));
      this.feedbackService.showSuccess('Person disassociated. You can now associate a different person.');
    } catch (error) {
      this.feedbackService.showError('Failed to change person association');
    }
  }
}
