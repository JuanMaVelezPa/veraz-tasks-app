import { Injectable, inject } from '@angular/core';
import { Observable, map, of, tap, catchError, throwError } from 'rxjs';
import { ClientApiService } from './client-api.service';
import { Client, ClientCreateRequest, ClientUpdateRequest, ClientType, ClientStatus, ClientCategory } from '@client/interfaces/client.interface';
import { SearchOptions } from '@shared/interfaces/search.interface';
import { PaginatedResponseDTO } from '@shared/interfaces/pagination.interface';
import { ApiResponse } from '@shared/interfaces/api-response.interface';
import { CacheService } from '@shared/services/cache.service';

const emptyClient: Client = {
  id: 'new',
  personId: '',
  type: ClientType.INDIVIDUAL,
  category: ClientCategory.STANDARD,
  status: ClientStatus.ACTIVE,
  creditLimit: 0,
  currency: 'USD',
  isActive: true,
  createdAt: new Date().toISOString(),
  updatedAt: new Date().toISOString()
};

const emptyPagination: PaginatedResponseDTO<Client> = {
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
export class ClientService {
  private readonly clientApiService = inject(ClientApiService);
  private readonly cacheService = inject(CacheService);

  getClients(options: SearchOptions): Observable<PaginatedResponseDTO<Client>> {
    const cacheKey = this.buildCacheKey(options);
    const cached = this.cacheService.get<PaginatedResponseDTO<Client>>(cacheKey);

    if (cached) {
      return of(cached);
    }

    return this.clientApiService.getClients(options)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'clients')),
        tap(response => this.cacheService.set(cacheKey, response)),
        catchError((error) => {
          console.error('ClientService - getClients - Error:', error);
          return of(emptyPagination);
        })
      );
  }

  getClientById(id: string): Observable<Client> {
    if (id === 'new') return of(emptyClient);

    const cacheKey = `client:${id}`;
    const cached = this.cacheService.get<Client>(cacheKey);

    if (cached) {
      return of(cached);
    }

    return this.clientApiService.getClientById(id)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'client')),
        tap((client) => this.cacheService.set(cacheKey, client)),
        catchError((error) => {
          console.error('ClientService - getClientById - Error:', error);
          return of(emptyClient);
        })
      );
  }

  createClient(clientData: ClientCreateRequest): Observable<Client> {
    // Validar campos obligatorios antes de enviar
    this.validateRequiredFields(clientData);

    return this.clientApiService.createClient(clientData)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'client')),
        tap(() => {
          this.clearClientsCache();
          this.clearPersonsCache();
        }),
        catchError((error) => this.propagateError(error, 'creating client'))
      );
  }

  updateClient(id: string, clientData: ClientUpdateRequest): Observable<Client> {
    return this.clientApiService.updateClient(id, clientData)
      .pipe(
        map((apiResponse) => this.extractDataFromResponse(apiResponse, 'client')),
        tap((client: Client) => {
          this.updateClientCache(client);
          this.clearClientsCache();
        }),
        catchError((error) => this.propagateError(error, 'updating client'))
      );
  }

  deleteClient(id: string): Observable<void> {
    return this.clientApiService.deleteClient(id)
      .pipe(
        map(() => { }),
        tap(() => {
          this.clearClientCache(id);
          this.clearClientsCache();
        }),
        catchError((error) => this.propagateError(error, 'deleting client'))
      );
  }

  getClientByPersonId(personId: string): Observable<Client | null> {
    if (!this.isValidPersonId(personId)) {
      return of(null);
    }

    const cacheKey = `client:person:${personId}`;
    const cached = this.cacheService.get<Client | null>(cacheKey);

    if (cached !== undefined && cached !== null) {
      return of(cached);
    }

    if (cached === null) {
      this.cacheService.delete(cacheKey);
    }

    return this.clientApiService.getClientByPersonId(personId)
      .pipe(
        map((apiResponse) => this.extractClientFromResponse(apiResponse)),
        tap((client: Client | null) => this.cacheService.set(cacheKey, client)),
        catchError((error) => {
          // Si no se encuentra el cliente (404), devolver null en lugar de propagar error
          if (error.status === 404) {
            this.cacheService.set(cacheKey, null);
            return of(null);
          }
          return this.propagateError(error, 'loading client by person ID');
        })
      );
  }

  private extractDataFromResponse<T>(apiResponse: ApiResponse<T>, dataKey: string): T {
    if (!apiResponse.success) {
      throw new Error(apiResponse.message || 'API request failed');
    }
    return apiResponse.data as T;
  }

  private propagateError(error: any, action: string): Observable<never> {
    console.error(`ClientService - Error ${action}:`, error);
    return throwError(() => error);
  }

  private buildCacheKey(options: SearchOptions): string {
    return `clients:${JSON.stringify(options)}`;
  }

  private clearClientsCache(): void {
    this.cacheService.clearPattern('clients:');
  }

  private clearClientCache(id: string): void {
    this.cacheService.delete(`client:${id}`);
    this.clearClientsCache();
  }

  private clearPersonsCache(): void {
    this.cacheService.clearPattern('persons:');
  }

  private updateClientCache(client: Client): void {
    this.cacheService.set(`client:${client.id}`, client);
    this.clearClientsCache();
    this.clearPersonsCache();
  }

  private extractClientFromResponse(apiResponse: ApiResponse<any>): Client | null {
    if (!apiResponse.success) {
      return null;
    }
    return apiResponse.data;
  }

  private isValidPersonId(personId: string): boolean {
    return Boolean(personId && personId !== 'undefined' && personId !== 'null' && personId.trim() !== '');
  }

  private validateRequiredFields(clientData: ClientCreateRequest): void {
    const errors: string[] = [];

    if (!clientData.personId || clientData.personId.trim() === '') {
      errors.push('Person ID is required');
    }

    if (!clientData.type) {
      errors.push('Client type is required');
    }

    if (!clientData.category) {
      errors.push('Client category is required');
    }

    if (!clientData.status) {
      errors.push('Client status is required');
    }

    if (!clientData.creditLimit || clientData.creditLimit <= 0) {
      errors.push('Credit limit is required and must be greater than 0');
    }

    if (!clientData.currency || clientData.currency.trim() === '') {
      errors.push('Currency is required');
    }

    if (errors.length > 0) {
      throw new Error(`Validation failed: ${errors.join(', ')}`);
    }
  }
}
