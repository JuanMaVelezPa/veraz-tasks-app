import { ClientType, ClientCategory, ClientStatus, ClientRating } from '@client/interfaces/client.interface';
import { SelectOption, CurrencyOption } from '@client/interfaces/client-params.interface';

export const CLIENT_TYPES: SelectOption<ClientType>[] = [
  { id: '1', code: ClientType.INDIVIDUAL, name: 'Individual', description: 'Individual client or natural person' },
  { id: '2', code: ClientType.CORPORATE, name: 'Corporate', description: 'Corporate client or company' },
  { id: '3', code: ClientType.GOVERNMENT, name: 'Government', description: 'Public sector client' }
];

export const CLIENT_CATEGORIES: SelectOption<ClientCategory>[] = [
  { id: '1', code: ClientCategory.STANDARD, name: 'Standard', description: 'Regular client' },
  { id: '2', code: ClientCategory.PREMIUM, name: 'Premium', description: 'High-value client' },
];

export const CLIENT_STATUSES: SelectOption<ClientStatus>[] = [
  { id: '1', code: ClientStatus.ACTIVE, name: 'Active', description: 'Active client' },
  { id: '2', code: ClientStatus.INACTIVE, name: 'Inactive', description: 'Inactive client' },
  { id: '3', code: ClientStatus.SUSPENDED, name: 'Suspended', description: 'Suspended client' },
  { id: '4', code: ClientStatus.BLACKLISTED, name: 'Blacklisted', description: 'Blacklisted client' }
];

export const CLIENT_RATINGS: SelectOption<ClientRating>[] = [
  { id: '1', code: ClientRating.ONE, name: '1 - Very Poor', description: 'Very low rating' },
  { id: '2', code: ClientRating.TWO, name: '2 - Poor', description: 'Low rating' },
  { id: '3', code: ClientRating.THREE, name: '3 - Fair', description: 'Fair rating' },
  { id: '4', code: ClientRating.FOUR, name: '4 - Good', description: 'Good rating' },
  { id: '5', code: ClientRating.FIVE, name: '5 - Excellent', description: 'Excellent rating' }
];

export const CURRENCIES: CurrencyOption[] = [
  { id: '1', code: 'CAD', name: 'Canadian Dollar', symbol: '$' },
  { id: '2', code: 'USD', name: 'US Dollar', symbol: '$' },
  { id: '3', code: 'EUR', name: 'Euro', symbol: '€' },
  { id: '4', code: 'COP', name: 'Colombian Peso', symbol: '$' },
];

export const PAYMENT_TERMS: SelectOption<string>[] = [
  { id: '1', code: 'IMMEDIATE', name: 'Immediate', description: 'Immediate payment' },
  { id: '2', code: 'NET_30', name: 'Net 30', description: 'Payment in 30 days' },
  { id: '3', code: 'NET_60', name: 'Net 60', description: 'Payment in 60 days' }
];

export const PAYMENT_METHODS: SelectOption<string>[] = [
  { id: '1', code: 'CASH', name: 'Cash', description: 'Cash payment' },
  { id: '2', code: 'BANK_TRANSFER', name: 'Bank Transfer', description: 'Bank transfer' },
  { id: '3', code: 'CREDIT_CARD', name: 'Credit Card', description: 'Credit card payment' },
  { id: '4', code: 'DEBIT_CARD', name: 'Debit Card', description: 'Debit card payment' },
];
