import { Timestamped } from '@shared/interfaces/timestamped.interface';

export interface PersonFields {
  identType: string;
  identNumber: string;
  firstName: string;
  lastName: string;
  birthDate?: string;
  gender?: string;
  nationality?: string;
  mobile?: string;
  email?: string;
  address?: string;
  city?: string;
  country?: string;
  postalCode?: string;
  notes?: string;
  isActive: boolean;
  isEmployee: boolean;
  isClient: boolean;
}

export interface PersonWithUser extends PersonFields {
  userId?: string | null;
}

export interface Person extends PersonWithUser, Timestamped {
  id: string;
}

export type PersonCreateRequest = PersonWithUser;
export type PersonUpdateRequest = Partial<PersonFields>;
export type PersonFormData = PersonFields;

export interface PersonUserAssociation {
  userId: string;
}

export interface PersonManagementOptions {
  context: 'admin' | 'profile';
  userId?: string;
  onSuccess?: (person: Person) => void;
  onError?: (error: any) => void;
}
