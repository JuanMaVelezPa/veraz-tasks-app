import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LayoutService } from '@shared/services/layout.service';
import { AuthService } from '@auth/services/auth.service';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'shared-sidebar',
  standalone: true,
  imports: [RouterLink, IconComponent],
  templateUrl: './sidebar.component.html',
})
export class SidebarComponent {
  layoutService = inject(LayoutService);
  authService = inject(AuthService);
}
