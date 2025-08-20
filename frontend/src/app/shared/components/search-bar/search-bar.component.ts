import { Component, input, output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'shared-search-bar',
  standalone: true,
  imports: [CommonModule, FormsModule, IconComponent],
  templateUrl: './search-bar.component.html'
})
export class SearchBarComponent {
  placeholder = input('Search...');
  searchChange = output<string>();

  onSearch(searchTerm: string) {
    this.searchChange.emit(searchTerm);
  }
}
