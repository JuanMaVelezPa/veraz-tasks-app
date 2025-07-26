import { Component, inject } from '@angular/core';
import { FooterComponent } from '@shared/components/footer/footer.component';
import { NavbarComponent } from '@shared/components/navbar/navbar.component';
import { SidebarComponent } from '@shared/components/sidebar/sidebar.component';
import { RouterOutlet } from '@angular/router';
import { LayoutService } from '@shared/services/layout.service';

@Component({
  selector: 'app-main-layout',
  imports: [NavbarComponent, FooterComponent, SidebarComponent, RouterOutlet],
  templateUrl: './main-layout.component.html',
})
export class MainLayoutComponent {

  layoutService = inject(LayoutService);

}
