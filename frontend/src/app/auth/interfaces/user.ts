export interface User {
  id: string;
  username: string;
  email: string;
  createdAt: string;
  updatedAt: string | null;
  roles: string[];
  perms: string[];
}
