import { Injectable, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { ErrorResponse, HttpErrorInfo } from '@shared/interfaces/error-response.interface';
import { FeedbackMessageService } from './feedback-message.service';

@Injectable({
  providedIn: 'root'
})
export class HttpErrorService {
  private feedbackService = inject(FeedbackMessageService);

  handleError(error: HttpErrorResponse, context: string): Observable<never> {
    const errorMessage = this.extractErrorMessage(error);
    return this.createCustomError(errorMessage, error.status, error, context, error.error);
  }

  private extractErrorMessage(error: HttpErrorResponse): string {
    return error.error?.message || this.getErrorMessageByStatus(error.status);
  }

  private createCustomError(
    message: string,
    status: number,
    originalError: HttpErrorResponse,
    context: string,
    errorResponse?: ErrorResponse
  ): Observable<never> {
    const customError: HttpErrorInfo = {
      message,
      status,
      originalError,
      context,
      errorResponse
    };

    return throwError(() => customError);
  }

  private getErrorMessageByStatus(status: number): string {
    const errorMessages: Record<number, string> = {
      400: 'Invalid request data.',
      401: 'Unauthorized. Please sign in again.',
      403: 'Access denied. You do not have permission for this action.',
      404: 'Resource not found.',
      409: 'Conflict with the provided data.',
      422: 'Validation error. Please check the data format.',
      429: 'Too many requests. Please wait a moment before trying again.',
      500: 'Server error. Please try again later.',
      502: 'Service temporarily unavailable. Please try again later.',
      503: 'Service temporarily unavailable. Please try again later.',
      504: 'Service temporarily unavailable. Please try again later.',
      0: 'Connection error. Please check your internet connection.'
    };

    if (errorMessages[status]) {
      return errorMessages[status];
    }

    return this.getGenericErrorMessage(status);
  }

  private getGenericErrorMessage(status: number): string {
    if (status >= 500) {
      return 'Server error. Please try again later.';
    } else if (status >= 400) {
      return 'Request error. Please verify the provided data.';
    } else {
      return `Error ${status}: Unexpected error occurred.`;
    }
  }
}
