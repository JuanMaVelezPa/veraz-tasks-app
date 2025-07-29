import { Component, input, output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './search-bar.component.html'
})
export class SearchBarComponent {
  placeholder = input('Search...');
  searchChange = output<string>();

  onSearch(searchTerm: string) {
    this.searchChange.emit(searchTerm);
  }
}
