export interface Person {
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
  createdAt: string;
  updatedAt: string;
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
