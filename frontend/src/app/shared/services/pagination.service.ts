import { inject, Injectable } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { map } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PaginationService {
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  currentPage = toSignal(
    this.route.queryParamMap.pipe(
      map(params => Number(params.get('page') ?? 1)),
      map(page => isNaN(page) ? 1 : page)
    ),
    { initialValue: 1 }
  );

  goToPage = (page: number) => {
    const params = { ...this.route.snapshot.queryParams, page };
    this.router.navigate([], { queryParams: params });
  };

  goToFirst = () => this.goToPage(1);
  goToPrev = () => this.goToPage(Math.max(1, this.currentPage() - 1));
  goToNext = (totalPages: number) => this.goToPage(Math.min(totalPages, this.currentPage() + 1));
}
