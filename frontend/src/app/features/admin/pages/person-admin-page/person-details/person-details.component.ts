import { Component, inject, input, signal, OnDestroy, ChangeDetectorRef, OnInit } from '@angular/core';
import { Person } from '@person/interfaces/person.interface';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { PersonService } from '@person/services/person.service';
import { PersonManagementService } from '@person/services/person-management.service';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FeedbackMessageComponent } from '@shared/components/feedback-message/feedback-message.component';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { NavigationHistoryService } from '@shared/services/navigation-history.service';
import { HttpErrorService } from '@shared/services/http-error.service';
import { PersonFormComponent } from '@person/components/person-form/person-form.component';
import { TimestampInfoComponent } from '@shared/components/timestamp-info/timestamp-info.component';
import { IconComponent } from '@shared/components/icon/icon.component';
import { firstValueFrom, catchError } from 'rxjs';
import { FormUtilsService } from '@shared/services/form-utils.service';

@Component({
  selector: 'person-details',
  imports: [ReactiveFormsModule, CommonModule, FeedbackMessageComponent, PersonFormComponent, TimestampInfoComponent, IconComponent],
  templateUrl: './person-details.component.html',
})
export class PersonDetailsComponent implements OnInit, OnDestroy {
  person = input.required<Person>();

  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private personService = inject(PersonService);
  private personManagementService = inject(PersonManagementService);
  private httpErrorService = inject(HttpErrorService);
  private feedbackService = inject(FeedbackMessageService);
  private navigationHistory = inject(NavigationHistoryService);
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
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while saving the person.');
      // Reset form to original values after error
      this.setFormValues(this.currentPerson()!);
    } finally {
      this.isLoading.set(false);
    }
  }

  private async createPerson(): Promise<void> {
    const formValue = this.personForm.value;
    const formData = this.personManagementService.prepareFormData(formValue);

    if (!this.personManagementService.validateRequiredFields(formData)) {
      return;
    }

    // Check if we're creating a person for a specific user
    const userId = this.route.snapshot.queryParamMap.get('userId');

    try {
      const createdPerson = await this.personManagementService.createPerson(
        formData,
        {
          context: 'admin',
          userId: userId || undefined,
          onSuccess: (person) => {
            this.currentPerson.set(person);
            this.wasSaved.set(true);

            // Navigate back after success
            setTimeout(() => {
              if (userId) {
                this.navigationHistory.goBackToUser(userId);
              } else {
                this.navigationHistory.goBackToPersons();
              }
            }, 500);
          },
          onError: () => {
            // Reset form to original values after error
            this.setFormValues(this.currentPerson() || this.person());
          }
        }
      );
    } catch (error: any) {
      // Error handling is done in the service
      this.setFormValues(this.currentPerson() || this.person());
      throw error;
    }
  }

  private async updatePerson(): Promise<void> {
    const originalPerson = this.currentPerson();
    if (!originalPerson) return;

    const formValue = this.personForm.value;
    const formData = this.personManagementService.prepareFormData(formValue);

    try {
      await this.personManagementService.updatePerson(
        originalPerson,
        formData,
        {
          context: 'admin',
          onSuccess: (person) => {
            this.currentPerson.set(person);
            this.setFormValues(person);
            this.wasSaved.set(true);
          },
          onError: () => {
            // Reset form to original values after error
            this.setFormValues(this.currentPerson()!);
          }
        }
      );
    } catch (error: any) {
      // Error handling is done in the service
      this.setFormValues(this.currentPerson()!);
    }
  }

  async deletePerson(): Promise<void> {
    const person = this.currentPerson();
    if (!person || person.id === 'new') return;

    this.isLoading.set(true);
    this.feedbackService.clearMessage();

    try {
      const response = await firstValueFrom(
        this.personService.deletePerson(person.id).pipe(
          catchError(error => this.httpErrorService.handleError(error, 'deleting person'))
        )
      );
      // For delete operations, response will be true if successful
      this.feedbackService.showSuccess('Person deleted successfully');

      // Check if we came from a user context
      const userId = this.route.snapshot.queryParamMap.get('userId');
      if (userId) {
        this.navigationHistory.goBackToUser(userId);
      } else {
        this.navigationHistory.goBackToPersons();
      }
    } catch (error: any) {
      this.feedbackService.showError(error.message || 'An error occurred while deleting the person.');
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

  goBack(): void {
    this.feedbackService.clearMessage();
    this.navigationHistory.goBack('/admin/persons');
  }

  ngOnDestroy(): void {
    this.feedbackService.clearMessage();
  }
}
