import { Component, inject, signal, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { PersonTableComponent } from '@person/components/person-table/person-table.component';
import { ActivatedRoute, Router } from '@angular/router';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { NavigationHistoryService } from '@shared/services/navigation-history.service';
import { IconComponent } from '@shared/components/icon/icon.component';

@Component({
  selector: 'app-persons-admin-page',
  imports: [PersonTableComponent, IconComponent],
  templateUrl: './persons-admin-page.component.html',
})
export class PersonsAdminPageComponent implements OnInit, OnDestroy {
  @ViewChild('personTable') personTable!: PersonTableComponent;

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private feedbackService = inject(FeedbackMessageService);
  private navigationHistory = inject(NavigationHistoryService);

  isSelectionMode = signal(false);
  selectedUserId = signal<string | null>(null);

  ngOnInit() {
    this.feedbackService.clearMessage();
    this.checkSelectionMode();

    // Listen for navigation events to refresh table when returning from person/employee forms
    this.route.queryParams.subscribe(() => {
      // Refresh table when query params change (e.g., returning from forms)
      setTimeout(() => {
        this.refreshPersonTable();
      }, 100);
    });
  }

  ngOnDestroy() {
    this.feedbackService.clearMessage();
  }

  private checkSelectionMode() {
    const mode = this.route.snapshot.queryParamMap.get('mode');
    const userId = this.route.snapshot.queryParamMap.get('userId');

    if (mode === 'select' && userId) {
      this.isSelectionMode.set(true);
      this.selectedUserId.set(userId);
    } else {
      this.isSelectionMode.set(false);
      this.selectedUserId.set(null);
    }
  }

  goBack() {
    const userId = this.selectedUserId();
    if (userId) {
      this.navigationHistory.navigateTo(`/admin/users/${userId}/person`);
    } else {
      this.navigationHistory.goBack('/admin/users');
    }
  }

  createNewPerson() {
    const userId = this.selectedUserId();
    if (userId) {
      this.router.navigate(['/admin/persons/new'], {
        queryParams: { userId }
      });
    } else {
      this.router.navigate(['/admin/persons/new']);
    }
  }

  refreshPersonTable(): void {
    if (this.personTable) {
      this.personTable.refreshData();
    }
  }
}
