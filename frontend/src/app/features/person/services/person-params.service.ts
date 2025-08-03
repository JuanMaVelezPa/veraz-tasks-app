import { Injectable, signal } from '@angular/core';
import { IdentificationType, Gender, Nationality, PersonParams } from '@person/interfaces/person-params.interface';

@Injectable({
  providedIn: 'root'
})
export class PersonParamsService {
  private identificationTypes = signal<IdentificationType[]>([
    { id: '1', code: 'CC', name: 'Citizenship Card', isActive: true },
    { id: '2', code: 'CE', name: 'Foreigner ID', isActive: true },
    { id: '3', code: 'TI', name: 'Identity Card', isActive: true },
    { id: '4', code: 'PP', name: 'Passport', isActive: true },
    { id: '5', code: 'NIT', name: 'Tax ID Number', isActive: true }
  ]);

  private genders = signal<Gender[]>([
    { id: '1', code: 'M', name: 'Male', isActive: true },
    { id: '2', code: 'F', name: 'Female', isActive: true },
    { id: '3', code: 'O', name: 'Other', isActive: true }
  ]);

  private nationalities = signal<Nationality[]>([
    { id: '1', code: 'CO', name: 'Colombian', isActive: true },
    { id: '2', code: 'US', name: 'American', isActive: true },
    { id: '3', code: 'ES', name: 'Spanish', isActive: true },
    { id: '4', code: 'MX', name: 'Mexican', isActive: true },
    { id: '5', code: 'AR', name: 'Argentine', isActive: true },
    { id: '6', code: 'BR', name: 'Brazilian', isActive: true },
    { id: '7', code: 'PE', name: 'Peruvian', isActive: true },
    { id: '8', code: 'CL', name: 'Chilean', isActive: true },
    { id: '9', code: 'VE', name: 'Venezuelan', isActive: true },
    { id: '10', code: 'EC', name: 'Ecuadorian', isActive: true }
  ]);

  getIdentificationTypes() {
    return this.identificationTypes;
  }

  getGenders() {
    return this.genders;
  }

  getNationalities() {
    return this.nationalities;
  }

  getPersonParams(): PersonParams {
    return {
      identificationTypes: this.identificationTypes(),
      genders: this.genders(),
      nationalities: this.nationalities()
    };
  }

  getIdentificationTypeById(id: string): IdentificationType | undefined {
    return this.identificationTypes().find(type => type.id === id);
  }

  getGenderById(id: string): Gender | undefined {
    return this.genders().find(gender => gender.id === id);
  }

  getNationalityById(id: string): Nationality | undefined {
    return this.nationalities().find(nationality => nationality.id === id);
  }

  getIdentificationTypeByCode(code: string): IdentificationType | undefined {
    return this.identificationTypes().find(type => type.code === code);
  }

  getGenderByCode(code: string): Gender | undefined {
    return this.genders().find(gender => gender.code === code);
  }

  getNationalityByCode(code: string): Nationality | undefined {
    return this.nationalities().find(nationality => nationality.code === code);
  }
}
