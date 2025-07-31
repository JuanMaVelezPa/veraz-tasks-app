import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { HttpErrorInfo } from '@shared/interfaces/http-error-info.interface';

@Injectable({
  providedIn: 'root'
})
export class HttpErrorService {

  handleError(error: HttpErrorResponse, context: string): Observable<never> {
    if (error.error?.message) {
      return this.createCustomError(error.error.message, error.status, error, context);
    }

    const errorMessage = this.getErrorMessageByStatus(error.status, context);

    return this.createCustomError(errorMessage, error.status, error, context);
  }

  private createCustomError(message: string, status: number, originalError: HttpErrorResponse, context: string): Observable<never> {
    const customError: HttpErrorInfo = {
      message,
      status,
      originalError,
      context
    };

    return throwError(() => customError);
  }

  private getErrorMessageByStatus(status: number, context: string): string {
    switch (status) {
      case 400:
        return this.get400Message(context);
      case 401:
        return 'Unauthorized. Please sign in again.';
      case 403:
        return 'Access denied. You do not have permission for this action.';
      case 404:
        return this.get404Message(context);
      case 409:
        return this.get409Message(context);
      case 422:
        return 'Validation error. Please check the data format.';
      case 429:
        return this.get429Message(context);
      case 500:
        return 'Server error. Please try again later.';
      case 502:
      case 503:
      case 504:
        return 'Service temporarily unavailable. Please try again later.';
      case 0:
        return 'Connection error. Please check your internet connection.';
      default:
        return this.getDefaultMessage(status);
    }
  }


  private get400Message(context: string): string {
    if (context.includes('sign-in')) {
      return 'Invalid login data. Please check your username and password.';
    } else if (context.includes('sign-up')) {
      return 'Invalid registration data. Please check all fields.';
    } else if (context.includes('creating')) {
      return 'Invalid user data. Please check all fields.';
    } else if (context.includes('updating')) {
      return 'Invalid update data. Please check the information provided.';
    } else {
      return 'Invalid data in the request.';
    }
  }

  private get404Message(context: string): string {
    if (context.includes('getting user')) {
      return 'User not found.';
    } else if (context.includes('updating')) {
      return 'User not found. Cannot update.';
    } else if (context.includes('deleting')) {
      return 'User not found. Cannot delete.';
    } else {
      return 'Resource not found.';
    }
  }

  private get409Message(context: string): string {
    if (context.includes('sign-up')) {
      return 'User already exists. Try with a different username or email.';
    } else if (context.includes('creating')) {
      return 'User already exists. Try with a different username or email.';
    } else if (context.includes('updating')) {
      return 'Username or email already exists. Try with different values.';
    } else {
      return 'Conflict with the provided data.';
    }
  }

  private get429Message(context: string): string {
    if (context.includes('sign-in')) {
      return 'Too many login attempts. Please wait a few minutes.';
    } else {
      return 'Too many requests. Please wait a moment before trying again.';
    }
  }

  /**
   * Mensaje por defecto para códigos de estado no manejados específicamente
   */
  private getDefaultMessage(status: number): string {
    if (status >= 500) {
      return 'Server error. Please try again later.';
    } else if (status >= 400) {
      return 'Request error. Please verify the provided data.';
    } else {
      return `Error ${status}: Unexpected error occurred.`;
    }
  }
}
