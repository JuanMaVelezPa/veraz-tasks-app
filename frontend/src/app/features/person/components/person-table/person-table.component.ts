import { DatePipe } from '@angular/common';
import { Component, inject, signal, computed, OnInit, input, output } from '@angular/core';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';
import { PersonService } from '@person/services/person.service';
import { PersonParamsService } from '@person/services/person-params.service';
import { EmployeeService } from '@employee/services/employee.service';
import { Person } from '@person/interfaces/person.interface';

import { SearchOptions } from '@shared/interfaces/search.interface';
import { SortableColumn } from '@shared/interfaces/sort.interface';
import { PaginationService } from '@shared/services/pagination.service';
import { SortService, SortState } from '@shared/services/sort.service';
import { SearchService, SearchState } from '@shared/services/search.service';
import { PreferencesService } from '@shared/services/preferences.service';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { PaginationComponent } from '@shared/components/pagination/pagination.component';
import { SearchBarComponent } from '@shared/components/search-bar/search-bar.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { FeedbackMessageComponent } from '@shared/components/feedback-message/feedback-message.component';
import { PersonAssociationService } from '@person/services/person-association.service';
import { IconComponent } from '@shared/components/icon/icon.component';
import { IconType } from '@shared/constants/icons.constant';

@Component({
  selector: 'app-person-table',
  imports: [RouterLink, PaginationComponent, SearchBarComponent, LoadingComponent, FeedbackMessageComponent, IconComponent],
  templateUrl: './person-table.component.html',
})
export class PersonTableComponent implements OnInit {
  // Inputs
  selectionMode = input<boolean>(false);
  selectedUserId = input<string>('');

  // Outputs
  personSelected = output<Person>();

  // Internal state
  isSelectionMode = signal(false);
  currentSelectedUserId = signal('');

  // Services
  private personService = inject(PersonService);
  private personParamsService = inject(PersonParamsService);
  private employeeService = inject(EmployeeService);
  private pagination = inject(PaginationService);
  private sortService = inject(SortService);
  private searchService = inject(SearchService);
  private preferencesService = inject(PreferencesService);
  private feedbackService = inject(FeedbackMessageService);
  private route = inject(ActivatedRoute);
  private personAssociationService = inject(PersonAssociationService);

  // Pagination and display settings
  personsPerPage = signal(10);
  currentPage = this.pagination.currentPage;
  showLastNameFirst = signal(false);
  showOnlyEmployees = signal(false);
  showOnlyUsers = signal(false);

  // State management
  sortState: SortState;
  searchState: SearchState;

  // Table configuration
  readonly columns: SortableColumn[] = [
    { key: 'firstName', label: 'Full Name', sortable: true },
    { key: 'identNumber', label: 'ID Number', sortable: true },
    { key: 'email', label: 'Email', sortable: true },
    { key: 'mobile', label: 'Mobile', sortable: true },
    { key: 'city', label: 'City', sortable: true },
    { key: 'type', label: 'Type', sortable: false },
    { key: 'isActive', label: 'Active', sortable: true },
    { key: 'actions', label: 'Actions', sortable: false }
  ];

  constructor() {
    this.sortState = this.sortService.createSortState();
    this.searchState = this.searchService.createSearchState();
  }

  ngOnInit(): void {
    this.loadPreferences();
    this.checkSelectionMode();
  }

  // Data fetching and filtering
  private queryParams = computed(() => {
    const sortOptions = this.sortState.getSortOptions();
    let sort = sortOptions.sort;
    let order = sortOptions.order;

    if (sort === 'firstName' || sort === 'lastName') {
      sort = this.showLastNameFirst() ? 'lastName' : 'firstName';
    }

    const params: SearchOptions = {
      page: this.currentPage() - 1,
      size: this.personsPerPage(),
      sort: sort,
      order: order
    };

    if (this.searchState.hasSearchTerm()) {
      params.search = this.searchState.getSearchValue();
    }

    return params;
  });

  personsResource = rxResource({
    params: this.queryParams,
    stream: ({ params }) => this.personService.getPersons(params)
  });

  filteredPersons = computed(() => {
    const persons = this.personsResource.value()?.data ?? [];
    let filtered = persons;

    if (this.showOnlyEmployees()) {
      filtered = filtered.filter(person => person.isEmployee);
    }

    if (this.showOnlyUsers()) {
      filtered = filtered.filter(person => person.userId);
    }

    return filtered;
  });

  // Computed properties for filter status
  totalPersons = computed(() => this.personsResource.value()?.data?.length ?? 0);
  filteredCount = computed(() => this.filteredPersons().length);
  hasActiveFilters = computed(() => this.showOnlyEmployees() || this.showOnlyUsers());

  // Display methods
  getFullName(person: Person): string {
    const firstName = person.firstName || '';
    const lastName = person.lastName || '';
    return this.showLastNameFirst()
      ? `${lastName} ${firstName}`.trim()
      : `${firstName} ${lastName}`.trim();
  }

  getIdentificationTypeCode(code: string): string {
    const type = this.personParamsService.getIdentificationTypeByCode(code);
    return type?.code || code;
  }

  // Type configuration methods
  getTypeIcon(person: Person): IconType {
    if (person.userId && person.isEmployee) return 'user-astronaut';
    if (person.isEmployee && !person.userId) return 'user-tie';
    if (person.userId && !person.isEmployee) return 'user-check';
    return 'user';
  }

  getTypeTooltip(person: Person): string {
    if (person.userId && person.isEmployee) return 'Complete Profile (User + Employee)';
    if (person.isEmployee && !person.userId) return 'Employee Profile';
    if (person.userId && !person.isEmployee) return 'User Profile';
    return 'Person Profile';
  }

  getTypeCssClass(person: Person): string {
    if (person.userId && person.isEmployee) return 'text-primary';
    if (person.isEmployee && !person.userId) return 'text-success';
    if (person.userId && !person.isEmployee) return 'text-info';
    return 'text-base-content/60';
  }

  // Event handlers
  handleSort(field: string): void {
    this.sortState.handleSort(field, this.columns);
    this.saveSortPreferences();
    this.resetToFirstPageIfNeeded();
  }

  handleSearch(term: string): void {
    this.searchState.setSearchTerm(term);
    this.resetToFirstPageIfNeeded();
  }

  // Filter toggles
  toggleNameOrder(): void {
    const newValue = this.preferencesService.toggleLastNameFirst();
    this.showLastNameFirst.set(newValue);
  }

  toggleEmployeeFilter(): void {
    this.showOnlyEmployees.update(value => !value);
  }

  toggleUserFilter(): void {
    this.showOnlyUsers.update(value => !value);
  }

  clearAllFilters(): void {
    this.showOnlyEmployees.set(false);
    this.showOnlyUsers.set(false);
  }

  showCompleteProfiles(): void {
    this.showOnlyEmployees.set(true);
    this.showOnlyUsers.set(true);
  }

  // Sort helpers
  isSortable(field: string): boolean {
    return this.sortState.isSortable(field, this.columns);
  }

  getSortIcon(field: string): IconType {
    return this.sortState.getSortIcon(field, this.columns);
  }

  getSortClass(field: string): string {
    return this.sortState.getSortClass(field, this.columns);
  }

  get searchTerm(): string {
    return this.searchState.getSearchTerm();
  }

  // Data management
  refreshData(): void {
    this.personService.clearPersonsCache();
    this.pagination.goToFirst();
  }

  async linkPersonWithUser(person: Person): Promise<void> {
    if (!this.currentSelectedUserId()) {
      this.feedbackService.showError('No user ID provided for linking');
      return;
    }

    await this.personAssociationService.linkPersonWithUser(
      person.id,
      this.currentSelectedUserId(),
      this.getFullName(person)
    );
  }

  selectPerson(person: Person): void {
    this.personSelected.emit(person);
  }

  // Private helper methods
  private checkSelectionMode(): void {
    const mode = this.route.snapshot.queryParamMap.get('mode');
    const userId = this.route.snapshot.queryParamMap.get('userId');

    if (mode === 'select' && userId) {
      this.isSelectionMode.set(true);
      this.currentSelectedUserId.set(userId);
    }
  }

  private loadPreferences(): void {
    const preferences = this.preferencesService.getPreferences();
    this.showLastNameFirst.set(preferences.showLastNameFirst);
    this.initializeSortState(preferences);
  }

  private initializeSortState(preferences: any): void {
    this.sortState.setInitialSort(preferences.sortField);
    if (preferences.sortOrder === 'desc') {
      this.sortState.handleSort(preferences.sortField, this.columns);
    }
  }

  private saveSortPreferences(): void {
    const sortOptions = this.sortState.getSortOptions();
    if (sortOptions.sort && sortOptions.order) {
      this.preferencesService.updateSortPreferences(
        sortOptions.sort,
        sortOptions.order
      );
    }
  }

  private resetToFirstPageIfNeeded(): void {
    if (this.currentPage() !== 1) {
      this.pagination.goToFirst();
    }
  }
}
