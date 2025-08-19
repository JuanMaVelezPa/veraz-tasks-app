import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AppInfoComponent } from '../../components/app-info/app-info.component';
import { MobileAppInfoComponent } from '../../components/mobile-app-info/mobile-app-info.component';

@Component({
  selector: 'app-auth-layout',
  imports: [RouterOutlet, AppInfoComponent, MobileAppInfoComponent],
  templateUrl: './auth-layout.component.html',
})
export class AuthLayoutComponent {
}
