import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IconComponent } from '@shared/components/icon/icon.component';
import { TabItem } from '@shared/interfaces/tab.interface';

@Component({
  selector: 'shared-tabs',
  standalone: true,
  imports: [CommonModule, IconComponent],
  templateUrl: './tabs.component.html'
})
export class TabsComponent {
  tabs = input.required<TabItem[]>();
  activeTab = input.required<string>();
  activeTabChange = output<string>();

  private readonly BASE_CLASSES = 'tab tab-sm sm:tab-md font-medium transition-all duration-200 rounded-t-lg border-2 flex-1 sm:flex-none min-w-0';
  private readonly ACTIVE_CLASSES = 'tab-active bg-primary text-primary-content border-primary';
  private readonly INACTIVE_CLASSES = 'border-base-300 hover:border-primary/50';
  private readonly DISABLED_CLASSES = 'opacity-50 cursor-not-allowed';

  setActiveTab(tabId: string): void {
    const tab = this.findTabById(tabId);
    if (this.canActivateTab(tab)) {
      this.activeTabChange.emit(tabId);
    }
  }

  getTabClasses(tab: TabItem): string {
    const classes = [
      this.BASE_CLASSES,
      this.isTabActive(tab) ? this.ACTIVE_CLASSES : this.INACTIVE_CLASSES,
      this.isTabDisabled(tab) ? this.DISABLED_CLASSES : ''
    ];

    return classes.filter(Boolean).join(' ');
  }

  private findTabById(tabId: string): TabItem | undefined {
    return this.tabs().find(tab => tab.id === tabId);
  }

  private canActivateTab(tab: TabItem | undefined): boolean {
    return tab != null && !tab.disabled;
  }

  private isTabActive(tab: TabItem): boolean {
    return this.activeTab() === tab.id;
  }

  private isTabDisabled(tab: TabItem): boolean {
    return Boolean(tab.disabled);
  }
}
