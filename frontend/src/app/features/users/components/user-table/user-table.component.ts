import { DatePipe } from '@angular/common';
import { Component, inject, signal, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';
import { UserService } from '@users/services/user.service';
import { SearchOptions } from '@shared/interfaces/search.interface';
import { SortableColumn } from '@shared/interfaces/sort.interface';
import { PaginationService } from '@shared/services/pagination.service';
import { SortService, SortState } from '@shared/services/sort.service';
import { SearchService, SearchState } from '@shared/services/search.service';
import { PaginationComponent } from '@shared/components/pagination/pagination.component';
import { SearchBarComponent } from '@shared/components/search-bar/search-bar.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { IconComponent } from '@shared/components/icon/icon.component';
import { IconType } from '@shared/constants/icons.constant';

@Component({
  selector: 'app-user-table',
  imports: [RouterLink, DatePipe, PaginationComponent, SearchBarComponent, LoadingComponent, IconComponent],
  templateUrl: './user-table.component.html',
})
export class UserTableComponent {
  private userService = inject(UserService);
  private pagination = inject(PaginationService);
  private sortService = inject(SortService);
  private searchService = inject(SearchService);

  usersPerPage = signal(10);
  currentPage = this.pagination.currentPage;
  sortState: SortState;
  searchState: SearchState;

  constructor() {
    this.sortState = this.sortService.createSortState();
    this.searchState = this.searchService.createSearchState();
    this.sortState.setInitialSort('username');
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
    stream: ({ params }) => this.userService.getUsers(params)
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

  isSortable = (field: string): boolean => this.sortState.isSortable(field, this.columns);
  getSortIcon = (field: string): IconType => this.sortState.getSortIcon(field, this.columns);
  getSortClass = (field: string): string => this.sortState.getSortClass(field, this.columns);
  getSortLabel = (field: string): string => this.sortState.getSortLabel(field, this.columns);

  get searchTerm(): string {
    return this.searchState.getSearchTerm();
  }
}
