import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { User } from '@auth/interfaces/user';
import { environment } from '@env/environment';
import { Observable, of, tap } from 'rxjs';
import { UserSearchOptions } from '../../../shared/interfaces/search.interface';
import { UsersResponse } from '../interfaces/userResponse.interface';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private http = inject(HttpClient);

  private usersCache = new Map<string, UsersResponse>();
  private userCache = new Map<string, User[]>();

  getUsers(options: UserSearchOptions): Observable<UsersResponse> {
    const { page, size, sort, order, search } = options;
    const cacheKey = `users-page-${page}-size-${size}-sort-${sort}-order-${order}-search-${search}`;

    if (this.usersCache.has(cacheKey)) {
      return of(this.usersCache.get(cacheKey)!);
    }

    const params: any = {
      page,
      size,
      sortBy: sort,
      sortDirection: order
    };

    if (search && search.trim()) {
      params.search = search.trim();
    }

    return this.http
      .get<UsersResponse>(`${environment.apiUrl}/users`, { params })
      .pipe(
        tap(response => {
          this.usersCache.set(cacheKey, response);
        })
      );
  }

}
