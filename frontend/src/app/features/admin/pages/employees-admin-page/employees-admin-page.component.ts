import { Component, inject, signal, OnInit, OnDestroy } from '@angular/core';
import { EmployeeTableComponent } from '@employee/components/employee-table/employee-table.component';
import { ActivatedRoute, Router } from '@angular/router';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { NavigationHistoryService } from '@shared/services/navigation-history.service';
import { IconComponent } from '@shared/components/icon/icon.component';

@Component({
  selector: 'app-employees-admin-page',
  standalone: true,
  imports: [EmployeeTableComponent, IconComponent],
  templateUrl: './employees-admin-page.component.html',
})
export class EmployeesAdminPageComponent implements OnInit, OnDestroy {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private feedbackService = inject(FeedbackMessageService);
  private navigationHistory = inject(NavigationHistoryService);

  isSelectionMode = signal(false);
  selectedPersonId = signal<string | null>(null);

  ngOnInit() {
    this.feedbackService.clearMessage();
    this.checkSelectionMode();
  }

  ngOnDestroy() {
    this.feedbackService.clearMessage();
  }

  private checkSelectionMode() {
    const mode = this.route.snapshot.queryParamMap.get('mode');
    const personId = this.route.snapshot.queryParamMap.get('personId');

    if (mode === 'select' && personId) {
      this.isSelectionMode.set(true);
      this.selectedPersonId.set(personId);
    } else {
      this.isSelectionMode.set(false);
      this.selectedPersonId.set(null);
    }
  }

  goBack() {
    const personId = this.selectedPersonId();
    if (personId) {
      this.navigationHistory.navigateTo(`/admin/persons/${personId}/employee`);
    } else {
      this.navigationHistory.goBack('/admin/persons');
    }
  }

}
