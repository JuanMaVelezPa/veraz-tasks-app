import { inject, Injectable } from '@angular/core';
import { User } from '@users/interfaces/user.interface';
import { Observable, of, tap } from 'rxjs';
import { UserSearchOptions } from '../../../shared/interfaces/search.interface';
import { UsersResponse } from '../interfaces/users-response.interface';
import { UserResponse } from '@users/interfaces/user-response.interface';
import { UserApiService } from './user-api.service';

const emptyUser: User = {
  id: 'new',
  username: '',
  email: '',
  isActive: true,
  createdAt: new Date().toISOString(),
  updatedAt: new Date().toISOString(),
  roles: ['USER'],
  perms: []
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userApi = inject(UserApiService);

  private usersCache = new Map<string, UsersResponse>();
  private userCache = new Map<string, User>();

  getUsers(options: UserSearchOptions): Observable<UsersResponse> {
    const { page, size, sort, order, search } = options;
    const cacheKey = `users-page-${page}-size-${size}-sort-${sort}-order-${order}-search-${search}`;

    if (this.usersCache.has(cacheKey)) {
      return of(this.usersCache.get(cacheKey)!);
    }

    return this.userApi.getUsers(options)
      .pipe(
        tap(response => {
          this.usersCache.set(cacheKey, response);
        })
      );
  }

  getUserById(id: string): Observable<User> {
    if (id === 'new') {
      return of(emptyUser)
    }

    if (this.userCache.has(id)) {
      return of(this.userCache.get(id)!)
    }

    return this.userApi.getUserById(id)
      .pipe(
        tap((user) => {
          this.userCache.set(id, user);
        })
      );
  }

  createUser(userData: Partial<User>): Observable<UserResponse> {
    return this.userApi.createUser(userData)
      .pipe(
        tap(() => {
          this.usersCache.clear();
        })
      );
  }

  updateUser(id: string, updatedUser: Partial<User>): Observable<UserResponse> {
    return this.userApi.updateUser(id, updatedUser)
      .pipe(
        tap((userResponse: UserResponse) => {
          if (userResponse.user) {
            this.userCache.set(userResponse.user.id, userResponse.user);
          }
          this.usersCache.clear();
        })
      );
  }

  deleteUser(id: string): Observable<UserResponse> {
    return this.userApi.deleteUser(id)
      .pipe(
        tap(() => {
          this.userCache.delete(id);
          this.usersCache.clear();
        })
      );
  }
}
