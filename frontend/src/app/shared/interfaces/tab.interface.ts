import { IconType } from '@shared/constants/icons.constant';

export interface TabItem {
  id: string;
  label: string;
  shortLabel?: string;
  icon: IconType;
  disabled?: boolean;
  badge?: {
    type: 'info' | 'warning' | 'success' | 'primary';
    text: string;
    icon?: IconType;
  };
}
