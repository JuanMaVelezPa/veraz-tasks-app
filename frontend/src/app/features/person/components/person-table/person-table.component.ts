import { DatePipe } from '@angular/common';
import { Component, inject, signal, computed, OnInit, input, output } from '@angular/core';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';
import { PersonService } from '@person/services/person.service';
import { PersonParamsService } from '@person/services/person-params.service';

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
  imports: [RouterLink, DatePipe, PaginationComponent, SearchBarComponent, LoadingComponent, FeedbackMessageComponent, IconComponent],
  templateUrl: './person-table.component.html',
})
export class PersonTableComponent implements OnInit {

  selectionMode = input<boolean>(false);
  selectedUserId = input<string>('');

  // Output for person selection
  personSelected = output<any>();

  // Internal signals for selection mode
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

  personsPerPage = signal(10);
  currentPage = this.pagination.currentPage;
  showLastNameFirst = signal(false);
  sortState: SortState;
  searchState: SearchState;

  constructor() {
    this.sortState = this.sortService.createSortState();
    this.searchState = this.searchService.createSearchState();
  }

  ngOnInit() {
    this.loadPreferences();
    this.checkSelectionMode();
  }

  private checkSelectionMode() {
    // Check if we're in selection mode from query params
    const mode = this.route.snapshot.queryParamMap.get('mode');
    const userId = this.route.snapshot.queryParamMap.get('userId');

    if (mode === 'select' && userId) {
      this.isSelectionMode.set(true);
      this.currentSelectedUserId.set(userId);
    }
  }

  private queryParams = computed(() => {
    const sortOptions = this.sortState.getSortOptions();
    let sort = sortOptions.sort;
    let order = sortOptions.order;

    if (sort === 'firstName') {
      sort = this.showLastNameFirst() ? 'lastName' : 'firstName';
    } else if (sort === 'lastName') {
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

  columns: SortableColumn[] = [
    { key: 'firstName', label: 'Full Name', sortable: true },
    { key: 'identNumber', label: 'ID Number', sortable: true },
    { key: 'email', label: 'Email', sortable: true },
    { key: 'mobile', label: 'Mobile', sortable: true },
    { key: 'city', label: 'City', sortable: true },
    { key: 'createdAt', label: 'Created At', sortable: true },
    { key: 'isActive', label: 'Status', sortable: true },
    { key: 'actions', label: 'Actions', sortable: false }
  ];

  getFullName = (person: any): string => {
    const firstName = person.firstName || '';
    const lastName = person.lastName || '';

    if (this.showLastNameFirst()) {
      return `${lastName} ${firstName}`.trim();
    }
    return `${firstName} ${lastName}`.trim();
  };

  getIdentificationTypeName = (code: string): string => {
    const type = this.personParamsService.getIdentificationTypeByCode(code);
    return type?.name || code;
  };

  getGenderName = (code: string): string => {
    const gender = this.personParamsService.getGenderByCode(code);
    return gender?.name || code;
  };

  getNationalityName = (code: string): string => {
    const nationality = this.personParamsService.getNationalityByCode(code);
    return nationality?.name || code;
  };

  handleSort = (field: string): void => {
    this.sortState.handleSort(field, this.columns);
    this.saveSortPreferences();
    this.resetToFirstPage();
  };

  handleSearch = (term: string): void => {
    this.searchState.setSearchTerm(term);
    this.resetToFirstPage();
  };

  toggleNameOrder = (): void => {
    const newValue = this.preferencesService.toggleLastNameFirst();
    this.showLastNameFirst.set(newValue);
  };

  private resetToFirstPage = (): void => {
    if (this.currentPage() !== 1) {
      this.pagination.goToFirst();
    }
  };

  private loadPreferences(): void {
    const preferences = this.preferencesService.getPreferences();

    this.showLastNameFirst.set(preferences.showLastNameFirst);

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

  isSortable = (field: string): boolean => this.sortState.isSortable(field, this.columns);
  getSortIcon = (field: string): IconType => this.sortState.getSortIcon(field, this.columns);
  getSortClass = (field: string): string => this.sortState.getSortClass(field, this.columns);
  getSortLabel = (field: string): string => this.sortState.getSortLabel(field, this.columns);

  get searchTerm(): string {
    return this.searchState.getSearchTerm();
  }

  async linkPersonWithUser(person: any) {
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

  selectPerson(person: any) {
    this.personSelected.emit(person);
  }
}
