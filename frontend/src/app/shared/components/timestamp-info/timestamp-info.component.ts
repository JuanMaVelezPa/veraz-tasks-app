import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Timestamped } from '../../interfaces/timestamped.interface';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'shared-timestamp-info',
  standalone: true,
  imports: [CommonModule, IconComponent],
  templateUrl: './timestamp-info.component.html'
})
export class TimestampInfoComponent {
  @Input() entity: Timestamped | null = null;
  @Input() title: string = 'Account Information';
  @Input() showIcons: boolean = true;

  protected get hasData(): boolean {
    return this.entity !== null &&
           (!!this.entity.createdAt || !!this.entity.updatedAt);
  }

  protected formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  protected formatTime(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
