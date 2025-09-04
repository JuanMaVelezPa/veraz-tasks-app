import { Injectable, inject } from '@angular/core';
import { Observable, map, of, tap, catchError, throwError } from 'rxjs';
import { PersonApiService } from './person-api.service';
import { Person, PersonCreateRequest, PersonUpdateRequest } from '@person/interfaces/person.interface';
import { SearchOptions } from '@shared/interfaces/search.interface';
import { PaginatedResponseDTO } from '@shared/interfaces/pagination.interface';
import { ApiResponse } from '@shared/interfaces/api-response.interface';
import { CacheService } from '@shared/services/cache.service';
import { UserService } from '@users/services/user.service';

const emptyPerson: Person = {
  id: 'new',
  identType: '',
  identNumber: '',
  firstName: '',
  lastName: '',
  isActive: true,
  createdAt: new Date().toISOString(),
  updatedAt: new Date().toISOString()
};

const emptyPagination: PaginatedResponseDTO<Person> = {
  data: [],
  pagination: {
    currentPage: 0,
    totalPages: 0,
    totalElements: 0,
    pageSize: 10,
    hasNext: false,
    hasPrevious: false,
    isFirst: true,
    isLast: true
  }
};

@Injectable({
  providedIn: 'root'
})
export class PersonService {
  private personApiService = inject(PersonApiService);
  private cacheService = inject(CacheService);
  private userService = inject(UserService);

  getPersons(options: SearchOptions): Observable<PaginatedResponseDTO<Person>> {
    const cacheKey = this.buildCacheKey(options);
    const cached = this.cacheService.get<PaginatedResponseDTO<Person>>(cacheKey);

    if (cached) {
      return of(cached);
    }

    return this.personApiService.getPersons(options)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'persons')),
        tap(response => this.cacheService.set(cacheKey, response)),
        catchError(() => of(emptyPagination))
      );
  }

  getPersonById(id: string): Observable<Person> {
    if (id === 'new') return of(emptyPerson);

    const cacheKey = `person:${id}`;
    const cached = this.cacheService.get<Person>(cacheKey);

    if (cached) {
      return of(cached);
    }

    return this.personApiService.getPersonById(id)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'person')),
        tap((person) => this.cacheService.set(cacheKey, person)),
        catchError((error) => {
          return of(emptyPerson);
        })
      );
  }

  createPerson(personData: PersonCreateRequest): Observable<Person> {
    return this.personApiService.createPerson(personData)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'person')),
        tap(() => this.clearPersonsCache()),
        catchError((error) => this.propagateError(error, 'creating person'))
      );
  }

  updatePerson(id: string, personData: PersonUpdateRequest): Observable<Person> {
    return this.personApiService.updatePerson(id, personData)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'person')),
        tap((person: Person) => this.updatePersonCache(person)),
        catchError((error) => this.propagateError(error, 'updating person'))
      );
  }

  deletePerson(id: string): Observable<boolean> {
    return this.personApiService.deletePerson(id)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'void')),
        tap(() => this.clearPersonCache(id)),
        catchError((error) => this.propagateError(error, 'deleting person'))
      );
  }

  removeUserAssociation(personId: string): Observable<Person> {
    return this.personApiService.removeUserAssociation(personId)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'person')),
        tap((person: Person) => {
          this.updatePersonCache(person);
          this.userService.clearAvailableUsersCache();
        }),
        catchError((error) => this.propagateError(error, 'removing user association'))
      );
  }

  associateUser(personId: string, userId: string): Observable<Person> {
    return this.personApiService.associateUser(personId, userId)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'person')),
        tap((person: Person) => {
          this.updatePersonCache(person);
          this.userService.clearAvailableUsersCache();
        }),
        catchError((error) => this.propagateError(error, 'associating user'))
      );
  }

  private buildCacheKey(options: SearchOptions): string {
    const { page, size, search, sort, order } = options;
    return `persons:${page}:${size}:${search || ''}:${sort || ''}:${order || ''}`;
  }

  private extractDataFromResponse(apiResponse: ApiResponse<any>, type: string): any {
    if (!apiResponse.success) {
      throw new Error(apiResponse.message || `Error ${type}`);
    }

    if (type === 'boolean' || type === 'void') {
      return true;
    }

    if (!apiResponse.data) {
      throw new Error(apiResponse.message || `Error ${type}`);
    }

    return apiResponse.data;
  }

  private propagateError(error: any, operation: string): Observable<never> {
    return throwError(() => error);
  }

  private updatePersonCache(person: Person): void {
    this.cacheService.set(`person:${person.id}`, person);
    this.clearPersonsCache();
  }

  private clearPersonCache(personId: string): void {
    this.cacheService.delete(`person:${personId}`);
    this.clearPersonsCache();
  }

  clearPersonsCache(): void {
    this.cacheService.clearPattern('persons:');
  }
}
