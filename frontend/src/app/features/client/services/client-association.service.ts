import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { ClientService } from './client.service';
import { Client } from '@client/interfaces/client.interface';
import { Person } from '@person/interfaces/person.interface';

@Injectable({
  providedIn: 'root'
})
export class ClientAssociationService {
  private readonly clientService = inject(ClientService);

  getClientByPersonId(personId: string): Observable<Client | null> {
    if (!personId) {
      return new Observable(subscriber => subscriber.next(null));
    }

    // This would typically call a specific endpoint, but for now we'll use the general service
    // In a real implementation, you might have a findByPersonId method
    return this.clientService.getClientById(personId)
      .pipe(
        map(client => client.id !== 'new' ? client : null)
      );
  }

  isPersonClient(personId: string): Observable<boolean> {
    return this.getClientByPersonId(personId)
      .pipe(
        map(client => client !== null)
      );
  }

  getClientPersonInfo(client: Client): Partial<Person> {
    // This would typically fetch person details from the person service
    // For now, return basic info if available
    return {
      id: client.personId
    };
  }

  validateClientPersonAssociation(personId: string, clientId?: string): Observable<boolean> {
    if (!personId) {
      return new Observable(subscriber => subscriber.next(false));
    }

    return this.isPersonClient(personId)
      .pipe(
        map(isClient => {
          // If we're updating an existing client, allow it
          if (clientId && clientId !== 'new') {
            return true;
          }
          // If creating new, ensure person is not already a client
          return !isClient;
        })
      );
  }
}
