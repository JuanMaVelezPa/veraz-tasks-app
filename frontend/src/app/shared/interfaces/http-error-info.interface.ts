import { HttpErrorResponse } from "@angular/common/http";

export interface HttpErrorInfo {
  message: string;
  status: number;
  originalError: HttpErrorResponse;
  context: string;
}
