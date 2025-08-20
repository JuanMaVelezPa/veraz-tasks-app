import { Timestamped } from '@shared/interfaces/timestamped.interface';

export interface User extends Timestamped {
  id: string;
  username: string;
  email: string;
  isActive: boolean;
  roles: string[];
  perms: string[];
}
