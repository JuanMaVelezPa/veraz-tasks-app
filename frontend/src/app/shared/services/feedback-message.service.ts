import { Injectable, signal, inject } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { FeedbackMessage } from '@shared/interfaces/feedback-message.interface';

@Injectable({
  providedIn: 'root'
})
export class FeedbackMessageService {
  message = signal<FeedbackMessage | null>(null);
  private timeoutId: number | null = null;
  private readonly DEFAULT_TIMEOUT = 3000; // 3 seconds

  constructor() {
    const router = inject(Router);
    router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.clearMessage();
      });
  }

  showMessage(type: 'success' | 'warning' | 'error' | 'info', text: string, timeout: number = this.DEFAULT_TIMEOUT) {
    this.clearTimeout();
    this.message.set(null);

    setTimeout(() => {
      this.message.set({ type, text });
      this.timeoutId = window.setTimeout(() => {
        this.clearMessage();
      }, timeout);
    }, 100);
  }

  showSuccess(text: string, timeout?: number) {
    this.showMessage('success', text, timeout);
  }

  showWarning(text: string, timeout?: number) {
    this.showMessage('warning', text, timeout);
  }

  showError(text: string, timeout?: number) {
    this.showMessage('error', text, timeout);
  }

  clearMessage() {
    this.clearTimeout();
    this.message.set(null);
  }

  private clearTimeout() {
    if (this.timeoutId) {
      clearTimeout(this.timeoutId);
      this.timeoutId = null;
    }
  }
}
