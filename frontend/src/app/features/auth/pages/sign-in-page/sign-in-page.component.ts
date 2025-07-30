import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormUtilsService } from '@shared/services/form-utils.service';
import { PasswordUtilsService } from '@shared/services/password-utils.service';
import { Router } from '@angular/router';
import { AuthService } from '@auth/services/auth.service';
import { AppInitService } from '@core/services/app-init.service';
import { LoadingComponent } from '@shared/components/loading/loading.component';

@Component({
  selector: 'app-sign-in-page',
  imports: [ReactiveFormsModule, LoadingComponent],
  templateUrl: './sign-in-page.component.html',
})
export class SignInPageComponent {

  fb = inject(FormBuilder);
  formUtils = inject(FormUtilsService);
  passwordUtils = inject(PasswordUtilsService);
  router = inject(Router);

  authService = inject(AuthService);
  appInitService = inject(AppInitService);

  isLoading = signal(false);
  errorMessage = signal<string | null>(null);

  signInForm = this.fb.nonNullable.group({
    usernameOrEmail: ['', [
      Validators.required,
      this.formUtils.usernameOrEmailValidator
    ]],
    password: ['', [
      Validators.required,
      this.passwordUtils.passwordValidator
    ]],
  });

  onSubmit() {

    if (this.signInForm.invalid) {
      this.signInForm.markAllAsTouched();
      return;
    }

    const { usernameOrEmail, password } = this.signInForm.value;
    if (!usernameOrEmail || !password) { return; }

    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.authService.signIn(usernameOrEmail.toLowerCase(), password!)
      .subscribe((isAuthenticated) => {
        if (isAuthenticated) {
          this.resetForm();
          this.isLoading.set(false);
          this.appInitService.startTokenRefresh();
          this.router.navigateByUrl('/');
        } else {
          this.errorMessage.set('Invalid credentials. Please verify your username and password.');
          this.isLoading.set(false);
        }
      },
        (error) => {
          this.isLoading.set(false);
          if (error && error.status === 401) {
            this.errorMessage.set('Invalid credentials. Please verify your username and password.');
          } else if (error && error.error && error.error.message) {
            this.errorMessage.set(error.error.message);
          } else {
            this.errorMessage.set('An unexpected error occurred while signing in. Please try again.');
          }
        }
      );
  }

  resetForm() {
    this.formUtils.resetForm(this.signInForm);
  }

  // navigateToSignUp() {
  //   this.router.navigate(['/auth/sign-up']);
  // }
}
