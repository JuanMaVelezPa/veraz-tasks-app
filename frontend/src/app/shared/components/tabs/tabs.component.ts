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

  setActiveTab(tabId: string): void {
    const tab = this.tabs().find(t => t.id === tabId);
    if (tab && !tab.disabled) {
      this.activeTabChange.emit(tabId);
    }
  }

  getTabClasses(tab: TabItem): string {
    const isActive = this.activeTab() === tab.id;
    const isDisabled = tab.disabled;

    const baseClasses = 'tab tab-sm sm:tab-md font-medium transition-all duration-200 rounded-t-lg border-2 flex-1 sm:flex-none min-w-0';
    const activeClasses = isActive ? 'tab-active bg-primary text-primary-content border-primary' : 'border-base-300 hover:border-primary/50';
    const disabledClasses = isDisabled ? 'opacity-50 cursor-not-allowed' : '';

    return `${baseClasses} ${activeClasses} ${disabledClasses}`.trim();
  }
}
