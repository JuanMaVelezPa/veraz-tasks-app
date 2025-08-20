import { Component, inject, input, output, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup } from '@angular/forms';
import { PersonParamsService } from '@person/services/person-params.service';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { IconComponent } from '@shared/components/icon/icon.component';

@Component({
  selector: 'app-person-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, LoadingComponent, IconComponent],
  templateUrl: './person-form.component.html',
})
export class PersonFormComponent {
  personForm = input.required<FormGroup>();
  isEditMode = input<boolean>(false);
  isLoading = input<boolean>(false);
  person = input.required<any>();

  formSubmitted = output<void>();
  personUpdated = output<any>();
  personCreated = output<any>();

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

}
