import { Injectable } from '@angular/core';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { ICON_MAP, IconType } from '../constants/icons.constant';

@Injectable({
  providedIn: 'root'
})
export class IconService {

  getIcon(type: IconType): IconDefinition {
    return ICON_MAP[type];
  }
}
