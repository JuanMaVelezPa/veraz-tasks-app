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
    let errorMessage: string;

    if (error.error?.message) {
      errorMessage = error.error.message;
    } else {
      errorMessage = this.getGeneralErrorMessageByStatus(error.status);
    }

    return this.createCustomError(errorMessage, error.status, error, context, error.error);
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

  private getGeneralErrorMessageByStatus(status: number): string {
    switch (status) {
      case 400:
        return 'Invalid request data.';
      case 401:
        return 'Unauthorized. Please sign in again.';
      case 403:
        return 'Access denied. You do not have permission for this action.';
      case 404:
        return 'Resource not found.';
      case 409:
        return 'Conflict with the provided data.';
      case 422:
        return 'Validation error. Please check the data format.';
      case 429:
        return 'Too many requests. Please wait a moment before trying again.';
      case 500:
        return 'Server error. Please try again later.';
      case 502:
      case 503:
      case 504:
        return 'Service temporarily unavailable. Please try again later.';
      case 0:
        return 'Connection error. Please check your internet connection.';
      default:
        if (status >= 500) {
          return 'Server error. Please try again later.';
        } else if (status >= 400) {
          return 'Request error. Please verify the provided data.';
        } else {
          return `Error ${status}: Unexpected error occurred.`;
        }
    }
  }
}
