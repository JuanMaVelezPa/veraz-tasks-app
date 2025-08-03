import { Injectable, signal } from '@angular/core';
import { SortableColumn, SortOptions } from '@shared/interfaces/sort.interface';

export class SortState {
  private sortField = signal<string>('');
  private sortOrder = signal<'asc' | 'desc'>('asc');

  setInitialSort(field: string): void {
    this.sortField.set(field);
    this.sortOrder.set('asc');
  }

  getSortField(): string {
    return this.sortField();
  }

  getSortOrder(): 'asc' | 'desc' {
    return this.sortOrder();
  }

  handleSort(field: string, columns: SortableColumn[]): void {
    if (!this.isSortable(field, columns)) return;

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
  }

  isSortable(field: string, columns: SortableColumn[]): boolean {
    const column = columns.find(col => col.key === field);
    return column?.sortable ?? false;
  }

  getSortIcon(field: string, columns: SortableColumn[]): string {
    if (!this.isSortable(field, columns)) return '';

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
  }

  getSortClass(field: string, columns: SortableColumn[]): string {
    if (!this.isSortable(field, columns)) return '';

    const currentField = this.sortField();
    return currentField === field ? 'text-primary font-semibold text-center' : '';
  }

  getSortLabel(field: string, columns: SortableColumn[]): string {
    if (!this.isSortable(field, columns)) return '';

    const currentField = this.sortField();
    const currentOrder = this.sortOrder();

    if (currentField !== field) return '';

    return currentOrder === 'asc' ? ' ↓' : ' ↑';
  }

  getSortOptions(): SortOptions {
    return {
      sort: this.sortField(),
      order: this.sortOrder()
    };
  }

  resetSort(): void {
    this.sortField.set('');
    this.sortOrder.set('asc');
  }

  getCurrentSortField(): string {
    return this.sortField();
  }

  getCurrentSortOrder(): 'asc' | 'desc' {
    return this.sortOrder();
  }
}

@Injectable({
  providedIn: 'root'
})
export class SortService {
  createSortState(): SortState {
    return new SortState();
  }
}
