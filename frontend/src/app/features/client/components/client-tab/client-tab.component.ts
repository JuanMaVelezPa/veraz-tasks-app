import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { Client } from '@client/interfaces/client.interface';
import { ClientFormComponent } from '@client/components/client-form/client-form.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { IconComponent } from '@shared/components/icon/icon.component';

@Component({
  selector: 'app-client-tab',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ClientFormComponent, LoadingComponent, IconComponent],
  templateUrl: './client-tab.component.html'
})
export class ClientTabComponent {
  clientForm = input.required<FormGroup>();
  client = input.required<Client | null>();
  isEditMode = input.required<boolean>();
  isLoading = input.required<boolean>();
  isLoadingClient = input.required<boolean>();
  isReadOnly = input<boolean>(false);

  formSubmitted = output<void>();
}
