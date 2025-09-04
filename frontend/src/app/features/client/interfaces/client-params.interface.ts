import { ClientType, ClientCategory, ClientStatus, ClientRating } from './client.interface';

export interface ClientParams {
  clientTypes: SelectOption<ClientType>[];
  clientCategories: SelectOption<ClientCategory>[];
  clientStatuses: SelectOption<ClientStatus>[];
  clientRatings: SelectOption<ClientRating>[];
  currencies: CurrencyOption[];
  paymentTerms: SelectOption<string>[];
  paymentMethods: SelectOption<string>[];
}

// Generic select option interface
export interface SelectOption<T = string> {
  id: string;
  code: T;
  name: string;
  description?: string;
}

// Specific option interfaces extending the generic one
export interface ClientTypeOption extends SelectOption<ClientType> {}
export interface ClientCategoryOption extends SelectOption<ClientCategory> {}
export interface ClientStatusOption extends SelectOption<ClientStatus> {}
export interface ClientRatingOption extends SelectOption<ClientRating> {}

export interface CurrencyOption {
  id: string;
  code: string;
  name: string;
  symbol: string;
}

export interface PaymentTermOption extends SelectOption<string> {}
export interface PaymentMethodOption extends SelectOption<string> {}
