import { Component, input, output, signal, computed, OnInit, inject, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { ReactiveFormsModule, FormGroup } from '@angular/forms';
import { Client, ClientType, ClientCategory, ClientStatus, ClientRating } from '@client/interfaces/client.interface';
import { ClientParamsService } from '@client/services/client-params.service';
import { IconComponent } from '@shared/components/icon/icon.component';
import { ScrollService } from '@shared/services/scroll.service';
import { LoadingComponent } from '@shared/components/loading/loading.component';

@Component({
  selector: 'app-client-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, IconComponent,
    CurrencyPipe, LoadingComponent],
  templateUrl: './client-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClientFormComponent implements OnInit {

  clientForm = input.required<FormGroup>();
  client = input<Client | null>(null);
  isEditMode = input<boolean>(false);
  isLoading = input<boolean>(false);
  isReadOnly = input<boolean>(false);

  formSubmitted = output<void>();
  formCancelled = output<void>();

  submitButtonText = computed(() => this.isEditMode() ? 'Update Client' : 'Create Client');
  submitButtonIcon = computed(() => this.isEditMode() ? 'save' : 'add');

  private clientParamsService = inject(ClientParamsService);
  private scrollService = inject(ScrollService);

  clientParams = signal(this.clientParamsService.getClientParams());

  clientTypes = computed(() => this.clientParams().clientTypes);
  clientCategories = computed(() => this.clientParams().clientCategories);
  clientStatuses = computed(() => this.clientParams().clientStatuses);
  clientRatings = computed(() => this.clientParams().clientRatings);
  currencies = computed(() => this.clientParams().currencies);
  paymentTerms = computed(() => this.clientParams().paymentTerms);
  paymentMethods = computed(() => this.clientParams().paymentMethods);

  ngOnInit() {
    this.setFormValues();
  }

  submitForm() {
    if (this.clientForm().valid) {
      this.formSubmitted.emit();
    } else {
      this.clientForm().markAllAsTouched();
      this.scrollService.scrollToFirstError();
    }
  }

  cancelForm() {
    this.formCancelled.emit();
  }

  private setFormValues(): void {
    const client = this.client();
    if (!client || !this.isEditMode()) return;

    const formData = {
      personId: client.personId,
      type: client.type,
      category: client.category,
      source: client.source,
      companyName: client.companyName,
      companyWebsite: client.companyWebsite,
      companyIndustry: client.companyIndustry,
      contactPerson: client.contactPerson,
      contactPosition: client.contactPosition,
      address: client.address,
      city: client.city,
      country: client.country,
      postalCode: client.postalCode,
      taxId: client.taxId,
      creditLimit: client.creditLimit,
      currency: client.currency,
      paymentTerms: client.paymentTerms,
      paymentMethod: client.paymentMethod,
      notes: client.notes,
      preferences: client.preferences,
      tags: client.tags,
      rating: client.rating,
      status: client.status,
      isActive: client.isActive
    };

    this.clientForm().patchValue(formData);
  }

  private getDisplayName<T>(value: T | null | undefined, options: Array<{ code: T; name: string }>): string {
    if (value == null) return '-';
    const option = options.find(opt => opt.code === value);
    return option?.name ?? String(value);
  }

  private getCurrencyDisplayNameInternal(code: string | null | undefined): string {
    if (code == null) return '-';
    const currency = this.currencies().find(c => c.code === code);
    return currency ? `${currency.name} (${currency.symbol})` : code;
  }

  getClientTypeDisplayName(code: ClientType): string {
    return this.getDisplayName(code, this.clientTypes());
  }

  getClientCategoryDisplayName(code: ClientCategory): string {
    return this.getDisplayName(code, this.clientCategories());
  }

  getClientStatusDisplayName(code: ClientStatus): string {
    return this.getDisplayName(code, this.clientStatuses());
  }

  getClientRatingDisplayName(rating: ClientRating): string {
    return this.getDisplayName(rating, this.clientRatings());
  }

  getCurrencyDisplayName(code: string): string {
    return this.getCurrencyDisplayNameInternal(code);
  }

  getPaymentTermDisplayName(code: string): string {
    return this.getDisplayName(code, this.paymentTerms());
  }

  getPaymentMethodDisplayName(code: string): string {
    return this.getDisplayName(code, this.paymentMethods());
  }

  getCurrencySymbol(currencyCode: string): string {
    const currency = this.currencies().find(c => c.code === currencyCode);
    return currency?.symbol || '$';
  }
}
