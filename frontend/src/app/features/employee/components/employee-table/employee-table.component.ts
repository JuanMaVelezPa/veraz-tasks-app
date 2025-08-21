import { DatePipe, CurrencyPipe } from '@angular/common';
import { Component, inject, signal, computed, OnInit, input, output } from '@angular/core';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';
import { EmployeeService } from '@employee/services/employee.service';
import { EmployeeParamsService } from '@employee/services/employee-params.service';
import { SalaryDisplayPipe } from '@employee/pipes/salary-display.pipe';

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
import { EmployeeAssociationService } from '@employee/services/employee-association.service';
import { IconComponent } from '@shared/components/icon/icon.component';
import { IconType } from '@shared/constants/icons.constant';
import { Employee } from '@employee/interfaces/employee.interface';

@Component({
  selector: 'app-employee-table',
  imports: [RouterLink, DatePipe, SalaryDisplayPipe,
    PaginationComponent, SearchBarComponent, LoadingComponent,
    FeedbackMessageComponent, IconComponent, FeedbackMessageComponent],
  templateUrl: './employee-table.component.html',
})
export class EmployeeTableComponent implements OnInit {

  selectionMode = input<boolean>(false);
  selectedPersonId = input<string>('');

  // Output for employee selection
  employeeSelected = output<any>();

  // Internal signals for selection mode
  isSelectionMode = signal(false);
  currentSelectedPersonId = signal('');

  private employeeService = inject(EmployeeService);
  private employeeParamsService = inject(EmployeeParamsService);
  private paginationService = inject(PaginationService);
  private sortService = inject(SortService);
  private searchService = inject(SearchService);
  private preferencesService = inject(PreferencesService);
  private feedbackService = inject(FeedbackMessageService);
  private route = inject(ActivatedRoute);
  private employeeAssociationService = inject(EmployeeAssociationService);

  employeesPerPage = signal(10);
  currentPage = this.paginationService.currentPage;
  sortState: SortState;
  searchState: SearchState;

  // Table columns configuration
  columns: SortableColumn[] = [
    { key: 'employeeCode', label: 'Employee Code', sortable: true },
    { key: 'position', label: 'Position', sortable: true },
    { key: 'department', label: 'Department', sortable: true },
    { key: 'employmentType', label: 'Employment Type', sortable: true },
    { key: 'status', label: 'Status', sortable: true },
    { key: 'hireDate', label: 'Hire Date', sortable: true },
    { key: 'salary', label: 'Salary', sortable: false },
    { key: 'actions', label: 'Actions', sortable: false }
  ];

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
    const personId = this.route.snapshot.queryParamMap.get('personId');

    if (mode === 'select' && personId) {
      this.isSelectionMode.set(true);
      this.currentSelectedPersonId.set(personId);
    }
  }

  private queryParams = computed(() => {
    const sortOptions = this.sortState.getSortOptions();
    const params: SearchOptions = {
      page: this.currentPage() - 1,
      size: this.employeesPerPage(),
      sort: sortOptions.sort,
      order: sortOptions.order
    };

    if (this.searchState.hasSearchTerm()) {
      params.search = this.searchState.getSearchValue();
    }

    return params;
  });

  // Use rxResource for reactive data loading
  employeesResource = rxResource({
    params: this.queryParams,
    stream: ({ params }) => this.employeeService.getEmployees(params)
  });

  // Employee params for display
  employeeParams = computed(() => this.employeeParamsService.getEmployeeParams());

  // Computed values for template
  employees = computed(() => this.employeesResource.value()?.data || []);
  paginationData = computed(() => this.employeesResource.value()?.pagination);

  // Methods
  onSearchChange(searchTerm: string) {
    this.searchState.setSearchTerm(searchTerm);
    this.paginationService.goToPage(1);
  }

  onSort(column: string) {
    this.sortState.handleSort(column, this.columns);
    this.paginationService.goToPage(1);
  }

  onPageChange(page: number) {
    this.paginationService.goToPage(page);
  }

  onEmployeeSelect(employee: Employee) {
    if (this.isSelectionMode()) {
      this.employeeSelected.emit(employee);
    }
  }

  getSortClass(column: string): string {
    return this.sortState.getSortClass(column, this.columns);
  }

  getSortIcon(column: string): IconType {
    return this.sortState.getSortIcon(column, this.columns);
  }

  getEmploymentTypeName(code: string): string {
    const type = this.employeeParams().employmentTypes.find(t => t.code === code);
    return type?.name || code;
  }

  getStatusName(code: string): string {
    const status = this.employeeParams().employeeStatuses.find(s => s.code === code);
    return status?.name || code;
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

  getDepartmentName(code: string): string {
    if (!code) return 'N/A';
    const dept = this.employeeParams().departments.find(d => d.code === code);
    return dept?.name || code;
  }

  // Optimización: trackBy function para mejorar performance
  trackByEmployeeId(index: number, employee: Employee): string {
    return employee.id;
  }

  private loadPreferences() {
    // Load user preferences for table display
    const preferences = this.preferencesService.getPreferences();
    // Note: employeesPerPage preference might not exist yet, so we'll use default
  }
}
