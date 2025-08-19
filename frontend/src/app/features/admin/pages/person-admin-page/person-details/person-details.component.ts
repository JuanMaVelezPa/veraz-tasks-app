import { Component, inject, input, signal, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { Person } from '@person/interfaces/person.interface';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { PersonService } from '@person/services/person.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FeedbackMessageComponent } from '@shared/components/feedback-message/feedback-message.component';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { PersonFormComponent } from '@person/components/person-form/person-form.component';
import { firstValueFrom } from 'rxjs';
import { FormUtilsService } from '@shared/services/form-utils.service';

@Component({
  selector: 'person-details',
  imports: [ReactiveFormsModule, CommonModule, FeedbackMessageComponent, PersonFormComponent],
  templateUrl: './person-details.component.html',
})
export class PersonDetailsComponent implements OnDestroy {
  person = input.required<Person>();

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private personService = inject(PersonService);
  private feedbackService = inject(FeedbackMessageService);
  private cdr = inject(ChangeDetectorRef);

  wasSaved = signal(false);
  isLoading = signal(false);
  isEditMode = signal(false);
  showDeleteModal = signal(false);
  currentPerson = signal<Person | null>(null);

  personForm = this.fb.nonNullable.group({
    identType: ['', [Validators.required]],
    identNumber: ['', [Validators.required, Validators.minLength(3)]],
    firstName: ['', [Validators.required, Validators.minLength(2)]],
    lastName: ['', [Validators.required, Validators.minLength(2)]],
    birthDate: [''],
    gender: [''],
    nationality: [''],
    mobile: [''],
    email: ['', [Validators.pattern(FormUtilsService.emailPattern)]],
    address: [''],
    city: [''],
    country: [''],
    postalCode: [''],
    notes: [''],
    isActive: [true]
  });

  ngOnInit(): void {
    this.feedbackService.clearMessage();
    const person = this.person();
    this.currentPerson.set(person);
    this.setFormValues(person);
    this.isEditMode.set(person.id !== 'new');
  }

  private setFormValues(person: Partial<Person>): void {
    this.personForm.patchValue({
      identType: person.identType || '',
      identNumber: person.identNumber || '',
      firstName: person.firstName || '',
      lastName: person.lastName || '',
      birthDate: person.birthDate ? this.formatDateForInput(person.birthDate) : '',
      gender: person.gender || '',
      nationality: person.nationality || '',
      mobile: person.mobile || '',
      email: person.email || '',
      address: person.address || '',
      city: person.city || '',
      country: person.country || '',
      postalCode: person.postalCode || '',
      notes: person.notes || '',
      isActive: person.isActive ?? true
    });
    this.cdr.detectChanges();
  }

  private formatDateForInput(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toISOString().split('T')[0];
  }

  async onSubmit(): Promise<void> {
    this.feedbackService.clearMessage();
    if (this.personForm.invalid) {
      this.personForm.markAllAsTouched();
      this.feedbackService.showError('Please fix validation errors before saving.');
      return;
    }
    this.isLoading.set(true);
    try {
      if (this.isEditMode()) {
        await this.updatePerson();
      } else {
        await this.createPerson();
      }
    } catch (error) {
      this.handlePersonError(error);
    } finally {
      this.isLoading.set(false);
    }
  }

  private async createPerson(): Promise<void> {
    const formValue = this.personForm.value;
    if (!formValue.identType?.trim() || !formValue.identNumber?.trim() ||
      !formValue.firstName?.trim() || !formValue.lastName?.trim()) {
      this.feedbackService.showError('Identification, first name and last name are required.');
      return;
    }
    const personData = {
      identType: formValue.identType.trim(),
      identNumber: formValue.identNumber.trim(),
      firstName: formValue.firstName.trim(),
      lastName: formValue.lastName.trim(),
      birthDate: formValue.birthDate || undefined,
      gender: formValue.gender || undefined,
      nationality: formValue.nationality?.trim() || undefined,
      mobile: formValue.mobile?.trim() || undefined,
      email: formValue.email?.trim().toLowerCase() || undefined,
      address: formValue.address?.trim() || undefined,
      city: formValue.city?.trim() || undefined,
      country: formValue.country?.trim() || undefined,
      postalCode: formValue.postalCode?.trim() || undefined,
      notes: formValue.notes?.trim() || undefined,
      isActive: formValue.isActive
    };
    try {
      const createdPerson = await firstValueFrom(this.personService.createPerson(personData));
      if (createdPerson?.id) {
        this.currentPerson.set(createdPerson);
        this.wasSaved.set(true);
        this.feedbackService.showSuccess('Person created successfully');
        setTimeout(() => {
          this.router.navigate(['/admin/persons']);
        }, 500);
      }
    } catch (error) {
      this.handlePersonError(error);
      throw error;
    }
  }

  private async updatePerson(): Promise<void> {
    const originalPerson = this.currentPerson();
    if (!originalPerson) return;

    const formValue = this.personForm.value;
    const changes = this.detectChanges(formValue, originalPerson);

    if (Object.keys(changes).length === 0) {
      this.feedbackService.showWarning('No changes detected. Nothing to save.');
      return;
    }

    try {
      const updatedPerson: Person = await firstValueFrom(this.personService.updatePerson(originalPerson.id, changes));
      if (updatedPerson?.id) {
        this.currentPerson.set(updatedPerson);
        this.setFormValues(updatedPerson);
        this.feedbackService.showSuccess('Person updated successfully');
        this.wasSaved.set(true);
      }
    } catch (error) {
      this.handlePersonError(error);
    }
  }

  async deletePerson(): Promise<void> {
    const person = this.currentPerson();
    if (!person || person.id === 'new') return;

    this.isLoading.set(true);
    this.feedbackService.clearMessage();

    try {
      const response = await firstValueFrom(this.personService.deletePerson(person.id));
      if (response) {
        this.feedbackService.showSuccess('Person deleted successfully');
        this.router.navigate(['/admin/persons']);
      } else {
        this.feedbackService.showError('Error deleting person');
      }
    } catch (error) {
      this.handlePersonError(error);
    } finally {
      this.isLoading.set(false);
      this.showDeleteModal.set(false);
    }
  }

  showDeleteConfirmation(): void {
    this.showDeleteModal.set(true);
  }

  cancelDelete(): void {
    this.showDeleteModal.set(false);
  }

  private handlePersonError(error: any): void {
    this.setFormValues(this.currentPerson() || this.person());
    let errorMessage = 'An error occurred while saving the person. Please try again.';
    if (error?.errorResponse?.message) {
      errorMessage = error.errorResponse.message;
    } else if (error?.message) {
      errorMessage = error.message;
    }
    this.feedbackService.showError(errorMessage);
  }

  private detectChanges(formValue: any, originalPerson: Person): any {
    const changes: any = {};
    const compareStrings = (original: string | undefined, newValue: string | undefined) => {
      return (original?.trim() || '') !== (newValue?.trim() || '');
    };

    if (compareStrings(originalPerson.identType, formValue.identType)) {
      changes.identType = formValue.identType?.trim();
    }
    if (compareStrings(originalPerson.identNumber, formValue.identNumber)) {
      changes.identNumber = formValue.identNumber?.trim();
    }
    if (compareStrings(originalPerson.firstName, formValue.firstName)) {
      changes.firstName = formValue.firstName?.trim();
    }
    if (compareStrings(originalPerson.lastName, formValue.lastName)) {
      changes.lastName = formValue.lastName?.trim();
    }
    if (formValue.birthDate !== originalPerson.birthDate) {
      changes.birthDate = formValue.birthDate || undefined;
    }
    if (formValue.gender !== originalPerson.gender) {
      changes.gender = formValue.gender || undefined;
    }
    if (compareStrings(originalPerson.nationality, formValue.nationality)) {
      changes.nationality = formValue.nationality?.trim() || undefined;
    }
    if (compareStrings(originalPerson.mobile, formValue.mobile)) {
      changes.mobile = formValue.mobile?.trim() || undefined;
    }
    if (compareStrings(originalPerson.email, formValue.email)) {
      changes.email = formValue.email?.trim().toLowerCase() || undefined;
    }
    if (compareStrings(originalPerson.address, formValue.address)) {
      changes.address = formValue.address?.trim() || undefined;
    }
    if (compareStrings(originalPerson.city, formValue.city)) {
      changes.city = formValue.city?.trim() || undefined;
    }
    if (compareStrings(originalPerson.country, formValue.country)) {
      changes.country = formValue.country?.trim() || undefined;
    }
    if (compareStrings(originalPerson.postalCode, formValue.postalCode)) {
      changes.postalCode = formValue.postalCode?.trim() || undefined;
    }
    if (compareStrings(originalPerson.notes, formValue.notes)) {
      changes.notes = formValue.notes?.trim() || undefined;
    }
    if (formValue.isActive !== originalPerson.isActive) {
      changes.isActive = formValue.isActive;
    }
    return changes;
  }

  resetForm(): void {
    this.feedbackService.clearMessage();
    const person = this.currentPerson() || this.person();
    this.setFormValues(person);
  }

  goBack(): void {
    this.feedbackService.clearMessage();
    this.router.navigate(['/admin/persons']);
  }

  ngOnDestroy(): void {
    this.feedbackService.clearMessage();
  }
}
