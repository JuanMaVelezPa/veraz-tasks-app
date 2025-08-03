import { HttpErrorResponse } from "@angular/common/http";

export interface HttpErrorInfo {
  message: string;
  status: number;
  originalError: HttpErrorResponse;
  context: string;
  errorResponse?: ErrorResponse;
}
export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  errorId: string;
  path: string;
  fieldErrors: Record<string, string>;
}
