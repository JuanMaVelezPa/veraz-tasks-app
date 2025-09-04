import { Injectable } from '@angular/core';
import { ClientParams, ClientTypeOption, ClientCategoryOption, ClientStatusOption, ClientRatingOption, CurrencyOption, PaymentTermOption, PaymentMethodOption } from '@client/interfaces/client-params.interface';
import {
  CLIENT_TYPES,
  CLIENT_CATEGORIES,
  CLIENT_STATUSES,
  CLIENT_RATINGS,
  CURRENCIES,
  PAYMENT_TERMS,
  PAYMENT_METHODS
} from '@client/constants/client-params.constants';

@Injectable({
  providedIn: 'root'
})
export class ClientParamsService {

  getClientParams(): ClientParams {
    return {
      clientTypes: CLIENT_TYPES,
      clientCategories: CLIENT_CATEGORIES,
      clientStatuses: CLIENT_STATUSES,
      clientRatings: CLIENT_RATINGS,
      currencies: CURRENCIES,
      paymentTerms: PAYMENT_TERMS,
      paymentMethods: PAYMENT_METHODS
    };
  }
}
