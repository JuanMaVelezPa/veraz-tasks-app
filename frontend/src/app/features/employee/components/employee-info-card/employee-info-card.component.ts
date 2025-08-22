import { Component, Input, Output, EventEmitter, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Employee } from '../../interfaces/employee.interface';
import { EmployeeParamsService } from '../../services/employee-params.service';
import { SalaryDisplayPipe } from '../../pipes/salary-display.pipe';
import { IconComponent } from '@shared/components/icon/icon.component';

@Component({
  selector: 'app-employee-info-card',
  standalone: true,
  imports: [CommonModule, IconComponent, SalaryDisplayPipe],
  templateUrl: './employee-info-card.component.html'
})
export class EmployeeInfoCardComponent {
  @Input() employee: Employee | null = null;
  @Input() isLoading: boolean = false;
  @Input() title: string = 'Employment Information';
  @Input() buttonText: string = 'Manage';
  @Input() showIcon: boolean = true;

  @Output() actionClicked = new EventEmitter<void>();

  private employeeParamsService = new EmployeeParamsService();
  protected readonly employeeProfile = signal<Employee | null>(null);

  ngOnChanges(): void {
    this.employeeProfile.set(this.employee);
  }

  onActionClick(): void {
    this.actionClicked.emit();
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'ACTIVE': return 'badge-success';
      case 'INACTIVE': return 'badge-warning';
      case 'TERMINATED': return 'badge-error';
      case 'ON_LEAVE': return 'badge-info';
      default: return 'badge-neutral';
    }
  }

  getStatusName(code: string): string {
    const status = this.employeeParamsService.getEmployeeStatuses()().find(s => s.code === code);
    return status?.name || code;
  }

  getDepartmentName(code: string): string {
    if (!code) return 'N/A';
    const dept = this.employeeParamsService.getDepartments()().find(d => d.code === code);
    return dept?.name || code;
  }
}
