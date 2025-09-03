import { Injectable } from '@angular/core';
import { TabItem } from '@shared/interfaces/tab.interface';

export interface TabsContext {
  type: 'admin' | 'profile';
  hasPersonalProfile?: boolean;
  hasEmploymentProfile?: boolean;
  hasUserAccount?: boolean;
  isEditMode?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class TabsService {

  initializeTabs(context: TabsContext): TabItem[] {
    if (context.type === 'admin') {
      return this.getAdminTabs(context);
    } else {
      return this.getProfileTabs(context);
    }
  }

  private getAdminTabs(context: TabsContext): TabItem[] {
    const { hasUserAccount, hasEmploymentProfile, isEditMode } = context;

    return [
      {
        id: 'user',
        label: 'User Management',
        shortLabel: 'User',
        icon: 'user-secret',
        disabled: !isEditMode,
        badge: this.getBadge('info', 'After Save', !isEditMode) ||
          this.getBadge('warning', 'Optional', !hasUserAccount) ||
          this.getBadge('success', '', !!hasUserAccount, 'check')
      },
      {
        id: 'person',
        label: 'Personal Information',
        shortLabel: 'Personal',
        icon: 'user',
        badge: isEditMode ? { type: 'primary', text: 'Required' } : undefined
      },
      {
        id: 'employee',
        label: 'Employment Information',
        shortLabel: 'Employee',
        icon: 'user-tie',
        disabled: !isEditMode,
        badge: this.getBadge('info', 'After Save', !isEditMode) ||
          this.getBadge('warning', 'Optional', !hasEmploymentProfile) ||
          this.getBadge('success', '', !!hasEmploymentProfile, 'check')
      }
    ];
  }

  private getProfileTabs(context: TabsContext): TabItem[] {
    const { hasPersonalProfile, hasEmploymentProfile } = context;

    return [
      {
        id: 'user',
        label: 'Account & Security',
        shortLabel: 'Account',
        icon: 'user'
      },
      {
        id: 'person',
        label: 'Personal Information',
        shortLabel: 'Personal',
        icon: 'user',
        disabled: !hasPersonalProfile,
        badge: hasPersonalProfile ? { type: 'success', text: '', icon: 'check' } : undefined
      },
      {
        id: 'employment',
        label: 'Employment Information',
        shortLabel: 'Employment',
        icon: 'user-tie',
        disabled: !hasEmploymentProfile,
        badge: hasEmploymentProfile ? { type: 'success', text: '', icon: 'check' } : undefined
      }
    ];
  }

  private getBadge(type: 'info' | 'warning' | 'success', text: string, condition: boolean, icon?: 'check'): TabItem['badge'] {
    return condition ? { type, text, icon } : undefined;
  }
}
