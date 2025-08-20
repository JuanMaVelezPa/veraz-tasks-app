import { Timestamped } from '@shared/interfaces/timestamped.interface';

export interface Person extends Timestamped {
  id: string;
  userId?: string;
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
}

export interface PersonCreateRequest {
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
  isActive?: boolean;
}

export interface PersonUpdateRequest {
  userId?: string | null;
  identType?: string;
  identNumber?: string;
  firstName?: string;
  lastName?: string;
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
  isActive?: boolean;
}
