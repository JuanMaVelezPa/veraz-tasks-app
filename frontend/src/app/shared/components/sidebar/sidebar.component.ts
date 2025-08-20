import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LayoutService } from '@shared/services/layout.service';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'shared-sidebar',
  standalone: true,
  imports: [RouterLink, IconComponent],
  templateUrl: './sidebar.component.html',
})
export class SidebarComponent {
  layoutService = inject(LayoutService);
}
