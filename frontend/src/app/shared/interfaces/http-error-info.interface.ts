import { HttpErrorResponse } from "@angular/common/http";
import { ErrorResponse } from "./error-response.interface";

export interface HttpErrorInfo {
  message: string;
  status: number;
  originalError: HttpErrorResponse;
  context: string;
  errorResponse?: ErrorResponse;
}
