import { AuthStatus } from "./auth-response.interface";

export interface SignInRequest {
  usernameOrEmail: string;
  password: string;
}

export interface SignInResponse {
  authStatus: AuthStatus;
  message: string;
}
