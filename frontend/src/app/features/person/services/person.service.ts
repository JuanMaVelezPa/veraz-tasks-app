import { Injectable, inject } from '@angular/core';
import { Observable, map, of, tap, catchError } from 'rxjs';
import { PersonApiService } from './person-api.service';
import { Person, PersonCreateRequest, PersonUpdateRequest } from '@person/interfaces/person.interface';
import { SearchOptions } from '@shared/interfaces/search.interface';
import { PaginatedResponseDTO } from '@shared/interfaces/pagination.interface';
import { ApiResponse } from '@shared/interfaces/api-response.interface';
import { CacheService } from '@shared/services/cache.service';

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

  getPersons(options: SearchOptions): Observable<PaginatedResponseDTO<Person>> {
    const cacheKey = this.generateCacheKey(options);
    const cached = this.cacheService.get<PaginatedResponseDTO<Person>>(cacheKey);

    if (cached) {
      return of(cached);
    }

    return this.personApiService.getPersons(options)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'persons')),
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
        map((apiResponse) => this.handleSuccess(apiResponse, 'person')),
        tap((person) => this.cacheService.set(cacheKey, person)),
        catchError(() => of(emptyPerson))
      );
  }

  createPerson(personData: PersonCreateRequest): Observable<Person> {
    return this.personApiService.createPerson(personData)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'person')),
        tap((person: Person) => this.cacheService.clearPattern('persons:')),
        catchError((error) => this.handleError(error, 'creating person'))
      );
  }

  updatePerson(id: string, personData: PersonUpdateRequest): Observable<Person> {
    return this.personApiService.updatePerson(id, personData)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'person')),
        tap((person: Person) => {
          this.cacheService.set(`person:${person.id}`, person);
          this.cacheService.clearPattern('persons:');
        }),
        catchError((error) => this.handleError(error, 'updating person'))
      );
  }

  deletePerson(id: string): Observable<boolean> {
    return this.personApiService.deletePerson(id)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'boolean')),
        tap(() => {
          this.cacheService.delete(`person:${id}`);
          this.cacheService.clearPattern('persons:');
        }),
        catchError((error) => this.handleError(error, 'deleting person'))
      );
  }

  removeUserAssociation(personId: string): Observable<boolean> {
    return this.personApiService.removeUserAssociation(personId)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'boolean')),
        tap(() => {
          this.cacheService.delete(`person:${personId}`);
          this.cacheService.clearPattern('persons:');
        }),
        catchError((error) => this.handleError(error, 'removing user association'))
      );
  }

  associateUser(personId: string, userId: string): Observable<Person> {
    return this.personApiService.associateUser(personId, userId)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'person')),
        tap((person: Person) => {
          this.cacheService.set(`person:${person.id}`, person);
          this.cacheService.clearPattern('persons:');
        }),
        catchError((error) => this.handleError(error, 'associating user'))
      );
  }

  private generateCacheKey(options: SearchOptions): string {
    const { page, size, search, sort, order } = options;
    return `persons:${page}:${size}:${search || ''}:${sort || ''}:${order || ''}`;
  }

  private handleSuccess(apiResponse: ApiResponse<any>, type: string): any {
    if (!apiResponse.success || !apiResponse.data) {
      throw new Error(apiResponse.message || `Error ${type}`);
    }
    return apiResponse.data;
  }

  private handleError(error: any, operation: string): Observable<never> {
    const errorMessage = error?.errorResponse?.message || error?.message || `Error ${operation}`;
    throw new Error(errorMessage);
  }
}
