import { User } from "./user.interface";

export interface AuthResponse {
  user: User;
  token: string | null;
  message: string;
}

export type AuthStatus = 'checking' | 'authenticated' | 'not-authenticated';

export interface CacheAuthResponse {
  authStatus: AuthStatus;
  user?: User;
  token?: string;
  lastAuthCheck: number;
}
