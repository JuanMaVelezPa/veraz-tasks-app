import { Timestamped } from '@shared/interfaces/timestamped.interface';

export interface ClientFields {
  personId: string;
  type: ClientType;
  category?: ClientCategory;
  source?: string;
  companyName?: string;
  companyWebsite?: string;
  companyIndustry?: string;
  contactPerson?: string;
  contactPosition?: string;
  address?: string;
  city?: string;
  country?: string;
  postalCode?: string;
  taxId?: string;
  creditLimit?: number;
  currency?: string;
  paymentTerms?: string;
  paymentMethod?: string;
  notes?: string;
  preferences?: string;
  tags?: string;
  rating?: ClientRating;
  status?: ClientStatus;
  isActive: boolean;
}

export interface Client extends ClientFields, Timestamped {
  id: string;
}

export interface ClientCreateRequest extends ClientFields { }

export interface ClientUpdateRequest extends Partial<ClientFields> { }

export enum ClientType {
  INDIVIDUAL = 'INDIVIDUAL',
  CORPORATE = 'CORPORATE',
  GOVERNMENT = 'GOVERNMENT'
}

export enum ClientCategory {
  STANDARD = 'STANDARD',
  PREMIUM = 'PREMIUM'
}

export enum ClientStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  SUSPENDED = 'SUSPENDED',
  BLACKLISTED = 'BLACKLISTED'
}

export enum ClientRating {
  ONE = 1,
  TWO = 2,
  THREE = 3,
  FOUR = 4,
  FIVE = 5
}
