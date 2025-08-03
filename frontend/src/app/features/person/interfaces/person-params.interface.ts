export interface IdentificationType {
  id: string;
  code: string;
  name: string;
  description?: string;
  isActive: boolean;
}

export interface Gender {
  id: string;
  code: string;
  name: string;
  description?: string;
  isActive: boolean;
}

export interface Nationality {
  id: string;
  code: string;
  name: string;
  description?: string;
  isActive: boolean;
}

export interface PersonParams {
  identificationTypes: IdentificationType[];
  genders: Gender[];
  nationalities: Nationality[];
} 