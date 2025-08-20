import { Component, Input, Output, EventEmitter, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Person } from '../../interfaces/person.interface';
import { IconComponent } from '@shared/components/icon/icon.component';

@Component({
  selector: 'app-person-info-card',
  standalone: true,
  imports: [CommonModule, IconComponent],
  templateUrl: './person-info-card.component.html'
})
export class PersonInfoCardComponent {
  @Input() person: Person | null = null;
  @Input() isLoading: boolean = false;
  @Input() title: string = 'Personal Information';
  @Input() buttonText: string = 'Manage';
  @Input() showIcon: boolean = true;

  @Output() actionClicked = new EventEmitter<void>();

  protected readonly associatedPerson = signal<Person | null>(null);

  ngOnChanges(): void {
    this.associatedPerson.set(this.person);
  }

  onActionClick(): void {
    this.actionClicked.emit();
  }
}
