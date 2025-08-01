export interface ApiResponse<T> {
  success: boolean;
  status: number;
  message: string;
  data: T;
  errors: string[];
}
