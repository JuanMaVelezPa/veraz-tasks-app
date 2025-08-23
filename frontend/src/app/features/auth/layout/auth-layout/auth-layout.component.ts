import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AppInfoComponent } from '../../components/app-info/app-info.component';
import { MobileAppInfoComponent } from '../../components/mobile-app-info/mobile-app-info.component';
import { FooterComponent } from '@shared/components/footer/footer.component';

@Component({
  selector: 'app-auth-layout',
  imports: [RouterOutlet, AppInfoComponent, MobileAppInfoComponent, FooterComponent],
  templateUrl: './auth-layout.component.html',
})
export class AuthLayoutComponent {
}
