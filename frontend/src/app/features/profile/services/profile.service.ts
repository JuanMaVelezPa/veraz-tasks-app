import { Injectable, inject } from '@angular/core';
import { Observable, of, tap, map, catchError, throwError, switchMap } from 'rxjs';
import { User } from '@users/interfaces/user.interface';
import { Person, PersonCreateRequest, PersonUpdateRequest } from '@person/interfaces/person.interface';
import { Employee, EmployeeCreateRequest, EmployeeUpdateRequest } from '@employee/interfaces/employee.interface';
import { Client, ClientCreateRequest, ClientUpdateRequest } from '@client/interfaces/client.interface';
import { ApiResponse } from '@shared/interfaces/api-response.interface';
import { CacheService } from '@shared/services/cache.service';
import { AuthService } from '@auth/services/auth.service';
import { UserApiService } from '@users/services/user-api.service';
import { PersonApiService } from '@person/services/person-api.service';
import { EmployeeApiService } from '@employee/services/employee-api.service';
import { ClientApiService } from '@client/services/client-api.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private userApiService = inject(UserApiService);
  private personApiService = inject(PersonApiService);
  private employeeApiService = inject(EmployeeApiService);
  private clientApiService = inject(ClientApiService);
  private cacheService = inject(CacheService);
  private authService = inject(AuthService);

  getMyProfile(): Observable<Person | null> {
    const currentUser = this.authService.user();
    if (!currentUser) {
      return throwError(() => new Error('User not authenticated'));
    }

    return this.personApiService.getPersonByUserId(currentUser.id).pipe(
      map((apiResponse) => this.handleSuccess(apiResponse, 'person')),
      catchError((error) => {
        if (error.status === 404) {
          return of(null);
        }
        return this.handleError(error, 'getting profile');
      })
    );
  }

  checkProfileExists(): Observable<boolean> {
    return this.getMyProfile().pipe(
      map((person) => person !== null),
      catchError(() => of(false))
    );
  }

  createMyProfile(personData: PersonCreateRequest): Observable<Person> {
    const currentUser = this.authService.user();
    if (!currentUser) {
      return throwError(() => new Error('User not authenticated'));
    }

    const personDataWithUser = { ...personData, userId: currentUser.id };
    return this.personApiService.createPerson(personDataWithUser).pipe(
      map((apiResponse) => this.handleSuccess(apiResponse, 'person')),
      tap((person) => this.cacheService.clearPattern('persons:')),
      catchError((error) => this.handleError(error, 'creating profile'))
    );
  }

  updateMyProfile(personData: PersonUpdateRequest): Observable<Person> {
    return this.getMyProfile().pipe(
      switchMap((person) => {
        if (!person) {
          return throwError(() => new Error('No person profile found to update'));
        }
        return this.personApiService.updatePerson(person.id, personData).pipe(
          map((apiResponse) => this.handleSuccess(apiResponse, 'person')),
          tap((person) => this.cacheService.clearPattern('persons:')),
          catchError((error) => this.handleError(error, 'updating profile'))
        );
      }),
      catchError((error) => this.handleError(error, 'getting profile'))
    );
  }

  deleteMyProfile(): Observable<boolean> {
    return this.getMyProfile().pipe(
      switchMap((person) => {
        if (!person) {
          return throwError(() => new Error('No person profile found to delete'));
        }
        return this.personApiService.deletePerson(person.id).pipe(
          map((apiResponse) => this.handleSuccess(apiResponse, 'void')),
          tap(() => this.cacheService.clearPattern('persons:')),
          catchError((error) => this.handleError(error, 'deleting profile'))
        );
      }),
      catchError((error) => this.handleError(error, 'getting profile'))
    );
  }

  getMyUserAccount(): Observable<User> {
    const currentUser = this.authService.user();
    if (!currentUser) {
      return throwError(() => new Error('User not authenticated'));
    }

    return this.userApiService.getUserById(currentUser.id).pipe(
      map((apiResponse) => this.handleSuccess(apiResponse, 'user')),
      catchError((error) => this.handleError(error, 'getting user account'))
    );
  }

  updateMyUserAccount(userData: any): Observable<User> {
    const currentUser = this.authService.user();
    if (!currentUser) {
      return throwError(() => new Error('User not authenticated'));
    }

    return this.userApiService.updateUser(currentUser.id, userData).pipe(
      map((apiResponse) => this.handleSuccess(apiResponse, 'user')),
      tap((user) => {
        this.cacheService.delete(`user:${user.id}`);
        this.cacheService.clearPattern('users:');
        this.authService.updateCurrentUser(user);
      }),
      catchError((error) => this.handleError(error, 'updating user account'))
    );
  }

  getMyEmployee(): Observable<Employee | null> {
    return this.getMyProfile().pipe(
      switchMap((person) => {
        if (!person) {
          return of(null);
        }
        return this.employeeApiService.getEmployeeByPersonId(person.id).pipe(
          map((apiResponse) => this.handleSuccess(apiResponse, 'employee')),
          catchError((error) => {
            if (error.status === 404) {
              return of(null);
            }
            return this.handleError(error, 'getting employee');
          })
        );
      }),
      catchError((error) => this.handleError(error, 'getting profile'))
    );
  }

  checkEmployeeExists(): Observable<boolean> {
    return this.getMyEmployee().pipe(
      map((employee) => employee !== null),
      catchError(() => of(false))
    );
  }

  createMyEmployee(employeeData: EmployeeCreateRequest): Observable<Employee> {
    return this.getMyProfile().pipe(
      switchMap((person) => {
        if (!person) {
          return throwError(() => new Error('No person profile found to create employee for'));
        }
        const employeeDataWithPerson = { ...employeeData, personId: person.id };
        return this.employeeApiService.createEmployee(employeeDataWithPerson).pipe(
          map((apiResponse) => this.handleSuccess(apiResponse, 'employee')),
          tap((employee) => this.cacheService.clearPattern('employees:')),
          catchError((error) => this.handleError(error, 'creating employee'))
        );
      }),
      catchError((error) => this.handleError(error, 'getting profile'))
    );
  }

  updateMyEmployee(employeeData: EmployeeUpdateRequest): Observable<Employee> {
    return this.getMyEmployee().pipe(
      switchMap((employee) => {
        if (!employee) {
          return throwError(() => new Error('No employee profile found to update'));
        }
        return this.employeeApiService.updateEmployee(employee.id, employeeData).pipe(
          map((apiResponse) => this.handleSuccess(apiResponse, 'employee')),
          tap((employee) => this.cacheService.clearPattern('employees:')),
          catchError((error) => this.handleError(error, 'updating employee'))
        );
      }),
      catchError((error) => this.handleError(error, 'getting employee'))
    );
  }

  deleteMyEmployee(): Observable<void> {
    return this.getMyEmployee().pipe(
      switchMap((employee) => {
        if (!employee) {
          return throwError(() => new Error('No employee profile found to delete'));
        }
        return this.employeeApiService.deleteEmployee(employee.id).pipe(
          map(() => {}),
          tap(() => this.cacheService.clearPattern('employees:')),
          catchError((error) => this.handleError(error, 'deleting employee'))
        );
      }),
      catchError((error) => this.handleError(error, 'getting employee'))
    );
  }

  getMyClient(): Observable<Client | null> {
    return this.getMyProfile().pipe(
      switchMap((person) => {
        if (!person) {
          return of(null);
        }
        return this.clientApiService.getClientByPersonId(person.id).pipe(
          map((apiResponse) => this.handleSuccess(apiResponse, 'client')),
          catchError((error) => {
            if (error.status === 404) {
              return of(null);
            }
            return this.handleError(error, 'getting client');
          })
        );
      }),
      catchError((error) => this.handleError(error, 'getting profile'))
    );
  }

  checkClientExists(): Observable<boolean> {
    return this.getMyClient().pipe(
      map((client) => client !== null),
      catchError(() => of(false))
    );
  }

  createMyClient(clientData: ClientCreateRequest): Observable<Client> {
    return this.getMyProfile().pipe(
      switchMap((person) => {
        if (!person) {
          return throwError(() => new Error('No person profile found to create client for'));
        }
        const clientDataWithPerson = { ...clientData, personId: person.id };
        return this.clientApiService.createClient(clientDataWithPerson).pipe(
          map((apiResponse) => this.handleSuccess(apiResponse, 'client')),
          tap((client) => this.cacheService.clearPattern('clients:')),
          catchError((error) => this.handleError(error, 'creating client'))
        );
      }),
      catchError((error) => this.handleError(error, 'getting profile'))
    );
  }

  updateMyClient(clientData: ClientUpdateRequest): Observable<Client> {
    return this.getMyClient().pipe(
      switchMap((client) => {
        if (!client) {
          return throwError(() => new Error('No client profile found to update'));
        }
        return this.clientApiService.updateClient(client.id, clientData).pipe(
          map((apiResponse) => this.handleSuccess(apiResponse, 'client')),
          tap((client) => this.cacheService.clearPattern('clients:')),
          catchError((error) => this.handleError(error, 'updating client'))
        );
      }),
      catchError((error) => this.handleError(error, 'getting client'))
    );
  }

  deleteMyClient(): Observable<void> {
    return this.getMyClient().pipe(
      switchMap((client) => {
        if (!client) {
          return throwError(() => new Error('No client profile found to delete'));
        }
        return this.clientApiService.deleteClient(client.id).pipe(
          map(() => {}),
          tap(() => this.cacheService.clearPattern('clients:')),
          catchError((error) => this.handleError(error, 'deleting client'))
        );
      }),
      catchError((error) => this.handleError(error, 'getting client'))
    );
  }

  private handleSuccess(apiResponse: ApiResponse<any>, type: string): any {
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

  private handleError(error: any, operation: string): Observable<never> {
    return throwError(() => error);
  }
}
