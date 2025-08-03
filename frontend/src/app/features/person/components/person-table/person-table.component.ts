import { DatePipe } from '@angular/common';
import { Component, inject, signal, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';
import { PersonService } from '@person/services/person.service';
import { PersonParamsService } from '@person/services/person-params.service';
import { SearchOptions } from '@shared/interfaces/search.interface';
import { SortableColumn } from '@shared/interfaces/sort.interface';
import { PaginationService } from '@shared/services/pagination.service';
import { SortService, SortState } from '@shared/services/sort.service';
import { SearchService, SearchState } from '@shared/services/search.service';
import { PaginationComponent } from '@shared/components/pagination/pagination.component';
import { SearchBarComponent } from '@shared/components/search-bar/search-bar.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';

@Component({
  selector: 'app-person-table',
  imports: [RouterLink, DatePipe, PaginationComponent, SearchBarComponent, LoadingComponent],
  templateUrl: './person-table.component.html',
})
export class PersonTableComponent {
  private personService = inject(PersonService);
  private personParamsService = inject(PersonParamsService);
  private pagination = inject(PaginationService);
  private sortService = inject(SortService);
  private searchService = inject(SearchService);

  personsPerPage = signal(10);
  currentPage = this.pagination.currentPage;
  showLastNameFirst = signal(false);
  sortState: SortState;
  searchState: SearchState;

  constructor() {
    this.sortState = this.sortService.createSortState();
    this.searchState = this.searchService.createSearchState();
    this.sortState.setInitialSort('firstName');
  }

  private queryParams = computed(() => {
    const sortOptions = this.sortState.getSortOptions();
    let sort = sortOptions.sort;
    let order = sortOptions.order;

    if (sort === 'firstName' && this.showLastNameFirst()) {
      sort = 'lastName,firstName';
    } else if (sort === 'lastName' && !this.showLastNameFirst()) {
      sort = 'firstName,lastName';
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
    if (this.showLastNameFirst()) {
      return `${person.lastName} ${person.firstName}`.trim();
    }
    return `${person.firstName} ${person.lastName}`.trim();
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
    this.resetToFirstPage();
  };

  handleSearch = (term: string): void => {
    this.searchState.setSearchTerm(term);
    this.resetToFirstPage();
  };

  toggleNameOrder = (): void => {
    this.showLastNameFirst.set(!this.showLastNameFirst());
  };

  private resetToFirstPage = (): void => {
    if (this.currentPage() !== 1) {
      this.pagination.goToFirst();
    }
  };

  isSortable = (field: string): boolean => this.sortState.isSortable(field, this.columns);
  getSortIcon = (field: string): string => this.sortState.getSortIcon(field, this.columns);
  getSortClass = (field: string): string => this.sortState.getSortClass(field, this.columns);
  getSortLabel = (field: string): string => this.sortState.getSortLabel(field, this.columns);

  get searchTerm(): string {
    return this.searchState.getSearchTerm();
  }
}
