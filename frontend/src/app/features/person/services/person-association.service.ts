import { Injectable, inject } from '@angular/core';
import { PersonService } from '@person/services/person.service';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { NavigationHistoryService } from '@shared/services/navigation-history.service';
import { firstValueFrom } from 'rxjs';

/**
 * Service for managing user-person associations
 * Provides methods for linking and unlinking users with persons
 * Uses dedicated endpoints to ensure clear separation of concerns
 */
@Injectable({
  providedIn: 'root'
})
export class PersonAssociationService {
  private personService = inject(PersonService);
  private feedbackService = inject(FeedbackMessageService);
  private navigationHistory = inject(NavigationHistoryService);

  /**
   * Links a person with a user using the dedicated association endpoint
   *
   * @param personId The person ID to link
   * @param userId The user ID to link with
   * @param personName The person name for success message
   */
  async linkPersonWithUser(personId: string, userId: string, personName: string): Promise<void> {
    try {
      await firstValueFrom(this.personService.associateUser(personId, userId));
      this.feedbackService.showSuccess(`Person ${personName} linked successfully`);
      this.navigationHistory.goBackToUser(userId);
    } catch (error) {
      this.feedbackService.showError('Failed to link person with user');
    }
  }

  /**
   * Removes user association from a person using the dedicated endpoint
   *
   * @param personId The person ID to remove association from
   * @param personName The person name for success message
   */
  async removePersonalProfile(personId: string, personName: string): Promise<void> {
    try {
      await firstValueFrom(this.personService.removeUserAssociation(personId));
      this.feedbackService.showSuccess('Personal profile removed successfully');
    } catch (error) {
      this.feedbackService.showError('Failed to remove personal profile');
    }
  }

  /**
   * Changes person association by removing current association
   * This allows linking to a different person
   *
   * @param personId The person ID to remove association from
   * @param userId The user ID (unused, kept for interface consistency)
   */
  async changePersonAssociation(personId: string, userId: string): Promise<void> {
    try {
      await firstValueFrom(this.personService.removeUserAssociation(personId));
      this.feedbackService.showSuccess('Personal profile removed. You can now link a different person.');
    } catch (error) {
      this.feedbackService.showError('Failed to change personal profile');
    }
  }
}
