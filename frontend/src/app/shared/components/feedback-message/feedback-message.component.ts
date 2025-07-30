import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';

@Component({
  selector: 'app-feedback-message',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './feedback-message.component.html'
})
export class FeedbackMessageComponent {

  private feedbackService = inject(FeedbackMessageService);

  message = this.feedbackService.message;

  getIconPath(): string {
    const type = this.message()?.type;
    switch (type) {
      case 'success':
        return 'M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z';
      case 'warning':
        return 'M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z';
      case 'error':
        return 'M12 9v3.75m9-.75a9 9 0 11-18 0 9 9 0 0118 0zm-9 3.75h.008v.008H12v-.008z';
      default:
        return 'M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z';
    }
  }
}
