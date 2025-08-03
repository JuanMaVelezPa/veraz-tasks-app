import { Injectable, inject } from '@angular/core';
import { Observable, map, of, tap, catchError } from 'rxjs';
import { PersonApiService } from './person-api.service';
import { Person, PersonCreateRequest, PersonUpdateRequest } from '@person/interfaces/person.interface';
import { SearchOptions } from '@shared/interfaces/search.interface';
import { PaginatedResponseDTO } from '@shared/interfaces/pagination.interface';
import { ApiResponse } from '@shared/interfaces/api-response.interface';

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

  private personsCache = new Map<string, PaginatedResponseDTO<Person>>();
  private personCache = new Map<string, Person>();

  getPersons(options: SearchOptions): Observable<PaginatedResponseDTO<Person>> {
    const cacheKey = this.generateCacheKey(options);

    if (this.personsCache.has(cacheKey)) {
      return of(this.personsCache.get(cacheKey)!);
    }

    return this.personApiService.getPersons(options)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'persons')),
        tap(response => this.personsCache.set(cacheKey, response)),
        catchError(() => of(emptyPagination))
      );
  }

  getPersonById(id: string): Observable<Person> {
    if (id === 'new') return of(emptyPerson);
    if (this.personCache.has(id)) return of(this.personCache.get(id)!);

    return this.personApiService.getPersonById(id)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'person')),
        tap((person) => this.personCache.set(id, person)),
        catchError(() => of(emptyPerson))
      );
  }

  createPerson(personData: PersonCreateRequest): Observable<Person> {
    return this.personApiService.createPerson(personData)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'person')),
        tap(() => this.clearPersonsCache()),
        catchError((error) => this.handleError(error, 'creating person'))
      );
  }

  updatePerson(id: string, personData: PersonUpdateRequest): Observable<Person> {
    return this.personApiService.updatePerson(id, personData)
      .pipe(
        map((apiResponse) => this.handleSuccess(apiResponse, 'person')),
        tap((person: Person) => {
          this.personCache.set(person.id, person);
          this.clearPersonsCache();
        }),
        catchError((error) => this.handleError(error, 'updating person'))
      );
  }

  deletePerson(id: string): Observable<boolean> {
    return this.personApiService.deletePerson(id)
      .pipe(
        map(() => true),
        tap(() => {
          this.personCache.delete(id);
          this.clearPersonsCache();
        }),
        catchError(() => of(false))
      );
  }

  private generateCacheKey(options: SearchOptions): string {
    const { page, size, sort, order, search } = options;
    return `persons-page-${page}-size-${size}-sort-${sort}-order-${order}-search-${search}`;
  }

  private clearPersonsCache(): void {
    this.personsCache.clear();
  }

  private handleSuccess(apiResponse: ApiResponse<any>, type: string): any {
    if (!apiResponse.success || !apiResponse.data) {
      throw new Error(apiResponse.message || `Error ${type}`);
    }

    if (type === 'persons') {
      return this.mapPaginatedPersons(apiResponse.data);
    } else if (type === 'person') {
      return this.mapBackendPersonToFrontendPerson(apiResponse.data);
    }

    return apiResponse.data;
  }

  private handleError(error: any, operation: string): Observable<never> {
    const errorMessage = error?.errorResponse?.message || error?.message || `Error ${operation}`;
    throw new Error(errorMessage);
  }

  private mapPaginatedPersons(backendData: any): PaginatedResponseDTO<Person> {
    const mappedPersons = backendData.data.map((person: any) =>
      this.mapBackendPersonToFrontendPerson(person)
    );

    return {
      data: mappedPersons,
      pagination: backendData.pagination
    };
  }

  private mapBackendPersonToFrontendPerson(backendPerson: any): Person {
    return {
      id: String(backendPerson.id),
      userId: backendPerson.userId,
      identType: backendPerson.identType,
      identNumber: backendPerson.identNumber,
      firstName: backendPerson.firstName,
      lastName: backendPerson.lastName,
      birthDate: backendPerson.birthDate,
      gender: backendPerson.gender,
      nationality: backendPerson.nationality,
      mobile: backendPerson.mobile,
      email: backendPerson.email,
      address: backendPerson.address,
      city: backendPerson.city,
      country: backendPerson.country,
      postalCode: backendPerson.postalCode,
      notes: backendPerson.notes,
      isActive: backendPerson.isActive,
      createdAt: backendPerson.createdAt,
      updatedAt: backendPerson.updatedAt
    };
  }
}
