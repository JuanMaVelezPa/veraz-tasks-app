import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { IconComponent } from '../icon/icon.component';
import { IconType } from '../../constants/icons.constant';

@Component({
  selector: 'shared-feedback-message',
  standalone: true,
  imports: [CommonModule, IconComponent],
  templateUrl: './feedback-message.component.html'
})
export class FeedbackMessageComponent {

  private feedbackService = inject(FeedbackMessageService);

  message = this.feedbackService.message;

  getIconType(): IconType {
    const type = this.message()?.type;
    switch (type) {
      case 'success':
        return 'check';
      case 'warning':
        return 'warning';
      case 'error':
        return 'error';
      default:
        return 'info';
    }
  }
}
