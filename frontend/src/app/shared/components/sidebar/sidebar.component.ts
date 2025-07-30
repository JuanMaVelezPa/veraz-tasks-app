import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LayoutService } from '@shared/services/layout.service';

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink],
  templateUrl: './sidebar.component.html',
})
export class SidebarComponent {

  layoutService = inject(LayoutService);

}
