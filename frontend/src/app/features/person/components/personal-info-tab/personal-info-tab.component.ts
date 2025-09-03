import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { Person } from '@person/interfaces/person.interface';
import { PersonFormComponent } from '@person/components/person-form/person-form.component';
import { IconComponent } from '@shared/components/icon/icon.component';

@Component({
  selector: 'app-personal-info-tab',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, PersonFormComponent, IconComponent],
  templateUrl: './personal-info-tab.component.html'
})
export class PersonalInfoTabComponent {
  personForm = input.required<FormGroup>();
  person = input.required<Person | null>();
  isEditMode = input.required<boolean>();
  isLoading = input.required<boolean>();
  isReadOnly = input<boolean>(false);

  formSubmitted = output<void>();
  personUpdated = output<Person>();
  personCreated = output<Person>();
}
