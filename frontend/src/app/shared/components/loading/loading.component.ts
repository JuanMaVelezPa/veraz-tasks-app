import { Component, input } from '@angular/core';

@Component({
  selector: 'shared-loading',
  templateUrl: './loading.component.html',
})
export class LoadingComponent {
  fullScreen = input<boolean>(false);
  overlay = input<boolean>(false);
  text = input<string>('');
}
