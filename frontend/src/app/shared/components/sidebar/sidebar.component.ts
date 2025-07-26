import { Component, inject } from '@angular/core';
import { LayoutService } from '@shared/services/layout.service';

@Component({
  selector: 'app-sidebar',
  imports: [],
  templateUrl: './sidebar.component.html',
})
export class SidebarComponent {

  layoutService = inject(LayoutService);
}
