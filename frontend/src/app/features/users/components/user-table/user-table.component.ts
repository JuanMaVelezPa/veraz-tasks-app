import { DatePipe } from '@angular/common';
import { Component, inject, signal, computed, OnInit } from '@angular/core';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';
import { UserService } from '@users/services/user.service';
import { PersonService } from '@person/services/person.service';
import { SearchOptions } from '@shared/interfaces/search.interface';
import { SortableColumn } from '@shared/interfaces/sort.interface';
import { PaginationService } from '@shared/services/pagination.service';
import { SortService, SortState } from '@shared/services/sort.service';
import { SearchService, SearchState } from '@shared/services/search.service';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { PaginationComponent } from '@shared/components/pagination/pagination.component';
import { SearchBarComponent } from '@shared/components/search-bar/search-bar.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { IconComponent } from '@shared/components/icon/icon.component';
import { IconType } from '@shared/constants/icons.constant';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-user-table',
  imports: [RouterLink, DatePipe, PaginationComponent, SearchBarComponent, LoadingComponent, IconComponent],
  templateUrl: './user-table.component.html',
})
export class UserTableComponent implements OnInit {
  private userService = inject(UserService);
  private personService = inject(PersonService);
  private pagination = inject(PaginationService);
  private sortService = inject(SortService);
  private searchService = inject(SearchService);
  private feedbackService = inject(FeedbackMessageService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  usersPerPage = signal(10);
  currentPage = this.pagination.currentPage;
  sortState: SortState;
  searchState: SearchState;

  isSelectionMode = signal(false);
  selectedPersonId = signal('');
  selectedPersonName = signal('');
  returnUrl = signal('');

  constructor() {
    this.sortState = this.sortService.createSortState();
    this.searchState = this.searchService.createSearchState();
    this.sortState.setInitialSort('username');
  }

  ngOnInit(): void {
    this.checkSelectionMode();
  }

  private checkSelectionMode(): void {
    const mode = this.route.snapshot.queryParamMap.get('mode');
    const personId = this.route.snapshot.queryParamMap.get('personId');
    const personName = this.route.snapshot.queryParamMap.get('personName');
    const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl');

    if (mode === 'select' && personId) {
      this.isSelectionMode.set(true);
      this.selectedPersonId.set(personId);
      this.selectedPersonName.set(personName || 'Unknown Person');
      this.returnUrl.set(returnUrl || '/admin/users');
    }
  }

  private queryParams = computed(() => {
    const sortOptions = this.sortState.getSortOptions();
    const params: SearchOptions = {
      page: this.currentPage() - 1,
      size: this.usersPerPage(),
      sort: sortOptions.sort,
      order: sortOptions.order
    };

    if (this.searchState.hasSearchTerm()) {
      params.search = this.searchState.getSearchValue();
    }

    return params;
  });

  usersResource = rxResource({
    params: this.queryParams,
    stream: ({ params }) => {
      if (this.isSelectionMode()) {
        return this.userService.getAvailableUsers(params);
      } else {
        return this.userService.getUsers(params);
      }
    }
  });

  columns: SortableColumn[] = [
    { key: 'username', label: 'Username', sortable: true },
    { key: 'email', label: 'Email', sortable: true },
    { key: 'roles', label: 'Roles', sortable: true },
    { key: 'createdAt', label: 'Created At', sortable: true },
    { key: 'isActive', label: 'Status', sortable: true },
    { key: 'actions', label: 'Actions', sortable: false }
  ];

  handleSort = (field: string): void => {
    this.sortState.handleSort(field, this.columns);
    if (this.currentPage() !== 1) {
      this.pagination.goToFirst();
    }
  };

  handleSearch = (term: string): void => {
    this.searchState.setSearchTerm(term);
    if (this.currentPage() !== 1) {
      this.pagination.goToFirst();
    }
  };

  async associateUser(userId: string): Promise<void> {
    if (!this.selectedPersonId()) {
      this.feedbackService.showError('No person ID provided for association');
      return;
    }

    try {
      await firstValueFrom(
        this.personService.associateUser(this.selectedPersonId(), userId)
      );

      this.feedbackService.showSuccess(`User associated successfully with ${this.selectedPersonName()}`);
      this.userService.clearAvailableUsersCache();

      if (this.returnUrl()) {
        this.router.navigateByUrl(this.returnUrl());
      } else {
        this.router.navigate(['/admin/persons', this.selectedPersonId()]);
      }
    } catch (error: any) {
      this.feedbackService.showError(
        error.error?.message || error.message || 'Failed to associate user. Please try again.'
      );
    }
  }

  goBack(): void {
    if (this.returnUrl()) {
      this.router.navigateByUrl(this.returnUrl());
    } else {
      this.router.navigate(['/admin/users']);
    }
  }

  isSortable = (field: string): boolean => this.sortState.isSortable(field, this.columns);
  getSortIcon = (field: string): IconType => this.sortState.getSortIcon(field, this.columns);
  getSortClass = (field: string): string => this.sortState.getSortClass(field, this.columns);
  getSortLabel = (field: string): string => this.sortState.getSortLabel(field, this.columns);

  get searchTerm(): string {
    return this.searchState.getSearchTerm();
  }
}
