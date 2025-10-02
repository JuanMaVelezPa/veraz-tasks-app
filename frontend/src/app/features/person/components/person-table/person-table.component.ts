import { Component, inject, signal, computed, OnInit, input, output } from '@angular/core';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';
import { PersonService } from '@person/services/person.service';
import { PersonParamsService } from '@person/services/person-params.service';
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

  selectionMode = input<boolean>(false);
  selectedUserId = input<string>('');

  personSelected = output<Person>();

  isSelectionMode = signal(false);
  currentSelectedUserId = signal('');

  private personService = inject(PersonService);
  private personParamsService = inject(PersonParamsService);
  private pagination = inject(PaginationService);
  private sortService = inject(SortService);
  private searchService = inject(SearchService);
  private preferencesService = inject(PreferencesService);
  private feedbackService = inject(FeedbackMessageService);
  private route = inject(ActivatedRoute);
  private personAssociationService = inject(PersonAssociationService);

  currentPage = this.pagination.currentPage;
  personsPerPage = signal(10);
  showLastNameFirst = signal(false);
  personFilter = signal<'all' | 'users' | 'employees' | 'clients' | 'complete'>('all');

  sortState: SortState;
  searchState: SearchState;

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
    const filter = this.personFilter();

    switch (filter) {
      case 'users':
        return persons.filter(person => person.userId);
      case 'employees':
        return persons.filter(person => person.isEmployee);
      case 'clients':
        return persons.filter(person => person.isClient);
      case 'complete':
        return persons.filter(person => person.userId && person.isEmployee && person.isClient);
      case 'all':
      default:
        return persons;
    }
  });

  totalPersons = computed(() => this.personsResource.value()?.data?.length ?? 0);
  filteredCount = computed(() => this.filteredPersons().length);
  hasActiveFilters = computed(() => this.personFilter() !== 'all');

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

  private readonly personTypeConfig = {
    astronaut: {
      icon: 'user-astronaut' as IconType,
      tooltip: 'Astronaut Profile (Complete)',
      cssClass: 'text-primary',
      condition: (person: Person) => person.userId && person.isEmployee && person.isClient
    },
    employee: {
      icon: 'helmet-safety' as IconType,
      tooltip: 'Employee Profile',
      cssClass: 'text-success',
      condition: (person: Person) => person.isEmployee
    },
    client: {
      icon: 'building' as IconType,
      tooltip: 'Client Profile',
      cssClass: 'text-warning',
      condition: (person: Person) => person.isClient
    },
    user: {
      icon: 'user-check' as IconType,
      tooltip: 'User Profile',
      cssClass: 'text-info',
      condition: (person: Person) => person.userId
    },
    basic: {
      icon: 'user' as IconType,
      tooltip: 'Basic Person Profile',
      cssClass: 'text-base-content/60',
      condition: () => true
    }
  } as const;

  getPersonType(person: Person): keyof typeof this.personTypeConfig {
    for (const [type, config] of Object.entries(this.personTypeConfig)) {
      if (config.condition(person)) {
        return type as keyof typeof this.personTypeConfig;
      }
    }
    return 'basic';
  }

  getTypeIcon(person: Person): IconType {
    return this.personTypeConfig[this.getPersonType(person)].icon;
  }

  getTypeTooltip(person: Person): string {
    return this.personTypeConfig[this.getPersonType(person)].tooltip;
  }

  getTypeCssClass(person: Person): string {
    return this.personTypeConfig[this.getPersonType(person)].cssClass;
  }

  handleSort(field: string): void {
    this.sortState.handleSort(field, this.columns);
    this.saveSortPreferences();
    this.resetToFirstPageIfNeeded();
  }

  handleSearch(term: string): void {
    this.searchState.setSearchTerm(term);
    this.resetToFirstPageIfNeeded();
  }

  toggleNameOrder(): void {
    const newValue = this.preferencesService.toggleLastNameFirst();
    this.showLastNameFirst.set(newValue);
  }

  setFilter(filter: 'all' | 'users' | 'employees' | 'clients' | 'complete'): void {
    this.personFilter.set(filter);
    this.saveFilterPreferences(filter);
  }

  clearAllFilters(): void {
    this.personFilter.set('all');
    this.saveFilterPreferences('all');
  }

  getFilterInfo() {
    const filter = this.personFilter();
    const filterInfo = {
      'all': { label: 'All Persons', icon: 'users' as IconType, color: 'btn-primary' },
      'users': { label: 'Users Only', icon: 'user-check' as IconType, color: 'btn-info' },
      'employees': { label: 'Employees Only', icon: 'helmet-safety' as IconType, color: 'btn-success' },
      'clients': { label: 'Clients Only', icon: 'building' as IconType, color: 'btn-warning' },
      'complete': { label: 'Complete Profiles', icon: 'user-astronaut' as IconType, color: 'btn-primary' }
    };
    return filterInfo[filter] || filterInfo['all'];
  }

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
    const validFilter = this.isValidFilter(preferences.personFilter) ? preferences.personFilter : 'all';
    this.personFilter.set(validFilter);
    this.initializeSortState(preferences);
  }

  private isValidFilter(filter: string | undefined): filter is 'all' | 'users' | 'employees' | 'clients' | 'complete' {
    const validFilters = ['all', 'users', 'employees', 'clients', 'complete'];
    return validFilters.includes(filter || '');
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

  private saveFilterPreferences(filter: string): void {
    this.preferencesService.updatePersonFilterPreferences(filter);
  }

  private resetToFirstPageIfNeeded(): void {
    if (this.currentPage() !== 1) {
      this.pagination.goToFirst();
    }
  }
}
