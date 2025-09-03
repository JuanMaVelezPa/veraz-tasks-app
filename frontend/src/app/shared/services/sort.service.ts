import { Injectable, signal } from '@angular/core';
import { SortableColumn } from '../interfaces/sort.interface';
import { IconType } from '../constants/icons.constant';

export interface SortOptions {
  sort: string;
  order: 'asc' | 'desc';
}

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

    const newOrder = this.calculateNewSortOrder(field, currentField, currentOrder);

    this.sortField.set(field);
    this.sortOrder.set(newOrder);
  }

  isSortable(field: string, columns: SortableColumn[]): boolean {
    const column = this.findColumnByField(field, columns);
    return column?.sortable ?? false;
  }

  getSortIcon(field: string, columns: SortableColumn[]): IconType {
    if (!this.isSortable(field, columns)) return 'sort';

    const currentField = this.sortField();
    const currentOrder = this.sortOrder();

    if (currentField !== field) {
      return 'sort';
    }

    return currentOrder === 'asc' ? 'chevron-up' : 'chevron-down';
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

  private calculateNewSortOrder(field: string, currentField: string, currentOrder: 'asc' | 'desc'): 'asc' | 'desc' {
    if (currentField === field) {
      return currentOrder === 'asc' ? 'desc' : 'asc';
    }
    return 'asc';
  }

  private findColumnByField(field: string, columns: SortableColumn[]): SortableColumn | undefined {
    return columns.find(col => col.key === field);
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
