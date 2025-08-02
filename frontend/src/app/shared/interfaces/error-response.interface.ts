export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  errorId: string;
  path: string;
  fieldErrors: Record<string, string>;
}
