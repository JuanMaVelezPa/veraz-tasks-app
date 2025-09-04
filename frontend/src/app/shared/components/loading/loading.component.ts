import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'shared-loading',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './loading.component.html',
})
export class LoadingComponent {
  fullScreen = input<boolean>(false);
  overlay = input<boolean>(false);
  text = input<string>('');
}
