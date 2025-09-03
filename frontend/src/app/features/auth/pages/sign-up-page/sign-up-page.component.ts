import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormUtilsService } from '@shared/services/form-utils.service';
import { PasswordUtilsService } from '@shared/services/password-utils.service';
import { Router } from '@angular/router';
import { AuthService } from '@auth/services/auth.service';
import { IconComponent } from '@shared/components/icon/icon.component';

@Component({
  selector: 'app-sign-up-page',
  imports: [ReactiveFormsModule, IconComponent],
  templateUrl: './sign-up-page.component.html',
})
export class SignUpPageComponent {

  fb = inject(FormBuilder);
  formUtils = inject(FormUtilsService);
  passwordUtils = inject(PasswordUtilsService);
  router = inject(Router);

  authService = inject(AuthService);

  isLoading = signal(false);

  signUpForm = this.fb.nonNullable.group({
    username: ['', [
      Validators.required,
      this.formUtils.validateUsername
    ]],
    email: ['', [
      Validators.required,
      Validators.pattern(FormUtilsService.EMAIL_PATTERN),
    ]],
    password: ['', [
      Validators.required,
      this.passwordUtils.passwordValidator,
    ]],
    confirmPassword: ['', [
      Validators.required,
      this.passwordUtils.passwordValidator,
    ]],
  }, {
    validators: [
      FormUtilsService.validateFieldEquality('password', 'confirmPassword')
    ]
  });

  onSubmit() {

    if (this.signUpForm.invalid) {
      this.signUpForm.markAllAsTouched();
      return;
    }

    const { username, email, password } = this.signUpForm.value;
    if (!username || !email || !password) {
      return;
    }

    this.isLoading.set(true);

    this.authService.signUp(username.toLowerCase(), email.toLowerCase(), password)
      .subscribe((response) => {
        if (response) {
          this.router.navigateByUrl('/');
        }
        this.resetForm();
        this.isLoading.set(false);
      });
  }

  resetForm() {
    this.formUtils.resetForm(this.signUpForm);
  }

  navigateToSignIn() {
    this.router.navigate(['/auth/sign-in']);
  }

}
