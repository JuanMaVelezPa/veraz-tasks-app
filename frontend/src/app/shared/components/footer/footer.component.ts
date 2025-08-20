import { Component } from '@angular/core';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'shared-footer',
  imports: [IconComponent],
  templateUrl: './footer.component.html',
})
export class FooterComponent {

  currentYear = new Date().getFullYear();
 }
