import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { Employee } from '@employee/interfaces/employee.interface';
import { EmployeeFormComponent } from '@employee/components/employee-form/employee-form.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { IconComponent } from '@shared/components/icon/icon.component';

@Component({
  selector: 'app-employee-tab',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, EmployeeFormComponent, LoadingComponent, IconComponent],
  templateUrl: './employee-tab.component.html'
})
export class EmployeeTabComponent {
  employeeForm = input.required<FormGroup>();
  employee = input.required<Employee | null>();
  isEditMode = input.required<boolean>();
  isLoading = input.required<boolean>();
  isLoadingEmployee = input.required<boolean>();
  isReadOnly = input<boolean>(false);

  formSubmitted = output<void>();
}
