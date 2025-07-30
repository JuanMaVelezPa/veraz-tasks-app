import { DatePipe } from '@angular/common';
import { Component, inject, signal, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';
import { UserService } from '@users/services/user.service';
import { UserSearchOptions } from '@shared/interfaces/search.interface';
import { PaginationService } from '@shared/services/pagination.service';
import { PaginationComponent } from '@shared/components/pagination/pagination.component';
import { SearchBarComponent } from '@shared/components/search-bar/search-bar.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';

@Component({
  selector: 'app-user-table',
  imports: [RouterLink, DatePipe, PaginationComponent, SearchBarComponent, LoadingComponent],
  templateUrl: './user-table.component.html',
})
export class UserTableComponent {
  private userService = inject(UserService);
  private pagination = inject(PaginationService);

  usersPerPage = signal(10);
  currentPage = this.pagination.currentPage;
  sortField = signal('username');
  sortOrder = signal<'asc' | 'desc'>('asc');
  searchTerm = signal('');

  private queryParams = computed(() => {
    const params: UserSearchOptions = {
      page: this.currentPage() - 1,
      size: this.usersPerPage(),
      sort: this.sortField(),
      order: this.sortOrder()
    };

    if (this.searchTerm().trim()) {
      params.search = this.searchTerm();
    }

    return params;
  });

  usersResource = rxResource({
    params: this.queryParams,
    stream: ({ params }) => this.userService.getUsers(params)
  });

  columns = [
    { key: 'index', label: '#', sortable: false },
    { key: 'username', label: 'Username', sortable: true },
    { key: 'email', label: 'Email', sortable: true },
    { key: 'roles', label: 'Roles', sortable: false },
    { key: 'createdAt', label: 'Created At', sortable: true },
    { key: 'actions', label: 'Actions', sortable: false }
  ];

  handleSort = (field: string) => {
    if (!this.isSortable(field)) return;

    const currentField = this.sortField();
    const currentOrder = this.sortOrder();

    let newOrder: 'asc' | 'desc';
    if (currentField === field) {
      newOrder = currentOrder === 'asc' ? 'desc' : 'asc';
    } else {
      newOrder = 'asc';
    }

    this.sortField.set(field);
    this.sortOrder.set(newOrder);
    this.resetToFirstPage();
  };

  handleSearch = (term: string) => {
    this.searchTerm.set(term);
    this.resetToFirstPage();
  };

  private resetToFirstPage = () => {
    if (this.currentPage() !== 1) {
      this.pagination.goToFirst();
    }
  };

  isSortable = (field: string) => {
    const column = this.columns.find(col => col.key === field);
    return column?.sortable ?? false;
  };

  getSortIcon = (field: string) => {
    if (!this.isSortable(field)) return '';

    const currentField = this.sortField();
    const currentOrder = this.sortOrder();

    if (currentField !== field) {
      return 'fa-sort text-gray-400 hover:text-gray-600';
    }

    if (currentOrder === 'asc') {
      return 'fa-sort-up text-primary text-center font-bold text-lg';
    } else {
      return 'fa-sort-down text-primary text-center font-bold text-lg';
    }
  };

  getSortClass = (field: string) => {
    if (!this.isSortable(field)) return '';

    const currentField = this.sortField();
    return currentField === field ? 'text-primary font-semibold text-center' : '';
  };

  getSortLabel = (field: string) => {
    if (!this.isSortable(field)) return '';

    const currentField = this.sortField();
    const currentOrder = this.sortOrder();

    if (currentField !== field) return '';

    return currentOrder === 'asc' ? ' ↑' : ' ↓';
  };
}
