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
  @Input() showIcon: boolean = true;

  protected get hasData(): boolean {
    return this.entity !== null &&
           (!!this.entity.createdAt || !!this.entity.updatedAt);
  }
}
