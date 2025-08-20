import { Component, input, computed, inject } from '@angular/core';
import { PaginationService } from '@shared/services/pagination.service';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'shared-pagination',
  standalone: true,
  imports: [IconComponent],
  templateUrl: './pagination.component.html'
})
export class PaginationComponent {
  paginationService = inject(PaginationService);

  totalPages = input<number>(0);
  totalItems = input<number>(0);
  itemsPerPage = input<number>(10);
  currentPage = this.paginationService.currentPage;

  startItem = computed(() => (this.currentPage() - 1) * this.itemsPerPage() + 1);
  endItem = computed(() => Math.min(this.currentPage() * this.itemsPerPage(), this.totalItems()));

  goToPage = (page: number) => this.paginationService.goToPage(page);
  goToPrev = () => this.paginationService.goToPrev();
  goToNext = () => this.paginationService.goToNext(this.totalPages());
}
