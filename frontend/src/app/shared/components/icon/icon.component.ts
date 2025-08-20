import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition, SizeProp } from '@fortawesome/fontawesome-svg-core';
import { IconService } from '../../services/icon.service';
import { IconType } from '../../constants/icons.constant';

export type IconSize = SizeProp;

@Component({
  selector: 'shared-icon',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './icon.component.html'
})
export class IconComponent {
  @Input() type!: IconType;
  @Input() size: IconSize = 'sm';
  @Input() color?: string = 'currentColor';

  private iconService = inject(IconService);

  get iconDefinition(): IconDefinition {
    return this.iconService.getIcon(this.type);
  }
}
