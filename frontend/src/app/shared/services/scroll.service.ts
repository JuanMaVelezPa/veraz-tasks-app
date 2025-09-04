import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ScrollService {

  scrollToTop(): void {
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  }

  scrollToFirstError(): void {
    // Find first form control with error
    const firstErrorElement = document.querySelector('.input-error, .select-error, .textarea-error');
    if (firstErrorElement) {
      firstErrorElement.scrollIntoView({
        behavior: 'smooth',
        block: 'center'
      });
    } else {
      // Fallback to scroll to top
      this.scrollToTop();
    }
  }

}
