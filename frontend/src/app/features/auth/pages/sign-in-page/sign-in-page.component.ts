import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormUtilsService } from '@shared/services/form-utils.service';
import { PasswordUtilsService } from '@shared/services/password-utils.service';
import { Router } from '@angular/router';
import { AuthService } from '@auth/services/auth.service';
import { AppInitService } from '@core/services/app-init.service';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { FeedbackMessageComponent } from '@shared/components/feedback-message/feedback-message.component';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-sign-in-page',
  imports: [ReactiveFormsModule, LoadingComponent, FeedbackMessageComponent],
  templateUrl: './sign-in-page.component.html',
})
export class SignInPageComponent {

  fb = inject(FormBuilder);
  formUtils = inject(FormUtilsService);
  passwordUtils = inject(PasswordUtilsService);
  router = inject(Router);

  authService = inject(AuthService);
  appInitService = inject(AppInitService);
  feedbackService = inject(FeedbackMessageService);

  isLoading = signal(false);
  hasError = signal(false);

  signInForm = this.fb.nonNullable.group({
    usernameOrEmail: ['', [Validators.required]],
    password: ['', [Validators.required]],
  });

  async onSubmit() {
    if (this.signInForm.invalid) {
      this.signInForm.markAllAsTouched();
      return;
    }

    const { usernameOrEmail, password } = this.signInForm.value;
    if (!usernameOrEmail || !password) {
      this.showError('Invalid credentials, please check your username/email and password');
      return;
    }

    if (this.formUtils.usernameValidator(this.signInForm.get('usernameOrEmail')!)) {
      this.showError('Invalid credentials, please check your username/email and password');
      return;
    }
    if (this.passwordUtils.passwordValidator(this.signInForm.get('password')!)) {
      this.showError('Invalid credentials, please check your username/email and password');
      return;
    }

    this.isLoading.set(true);
    this.hasError.set(false);
    this.feedbackService.clearMessage();

    try {
      const signInResponse = await firstValueFrom(
        this.authService.signIn(usernameOrEmail.toLowerCase(), password!)
      );

      if (signInResponse.authStatus === 'authenticated') {
        this.resetForm();
        this.appInitService.startTokenRefresh();
        this.router.navigateByUrl('/');
      } else {
        this.showError(signInResponse.message, 10000);
      }
    } catch (error) {
      this.showError('Unexpected error signing in. Please try again.');
    } finally {
      this.isLoading.set(false);
    }
  }

  private showError(message: string, timeout: number = 2000) {
    this.hasError.set(true);
    this.feedbackService.showError(message, timeout);
  }

  resetForm() {
    this.hasError.set(false);
    this.feedbackService.clearMessage();
    this.formUtils.resetForm(this.signInForm);
  }

  // navigateToSignUp() {
  //   this.router.navigate(['/auth/sign-up']);
  // }
}
