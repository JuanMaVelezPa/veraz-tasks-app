import { Injectable, signal } from '@angular/core';

export class SearchState {
  private searchTerm = signal<string>('');

  setSearchTerm(term: string): void {
    this.searchTerm.set(term);
  }

  getSearchTerm(): string {
    return this.searchTerm();
  }

  clearSearch(): void {
    this.searchTerm.set('');
  }

  hasSearchTerm(): boolean {
    return this.searchTerm().trim().length > 0;
  }

  getSearchValue(): string {
    return this.searchTerm().trim();
  }

  isEmpty(): boolean {
    return !this.hasSearchTerm();
  }
}

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  createSearchState(): SearchState {
    return new SearchState();
  }
}
