import { Component, inject, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup } from '@angular/forms';
import { PersonParamsService } from '@person/services/person-params.service';

@Component({
  selector: 'app-person-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './person-form.component.html',
})
export class PersonFormComponent {
  personForm = input.required<FormGroup>();
  isEditMode = input<boolean>(false);
  isLoading = input<boolean>(false);
  person = input.required<any>();

  formSubmitted = output<void>();
  formReset = output<void>();

  private personParamsService = inject(PersonParamsService);

  identificationTypes = this.personParamsService.getIdentificationTypes();
  genders = this.personParamsService.getGenders();
  nationalities = this.personParamsService.getNationalities();

  onSubmit(): void {
    if (this.personForm().invalid) {
      this.personForm().markAllAsTouched();
      return;
    }
    this.formSubmitted.emit();
  }

  onReset(): void {
    this.formReset.emit();
  }
}
