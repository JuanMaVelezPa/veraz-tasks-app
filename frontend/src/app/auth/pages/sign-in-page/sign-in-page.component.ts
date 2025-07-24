import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormUtilsService } from '@shared/services/form-utils.service';
import { PasswordUtilsService } from '@shared/services/password-utils.service';
import { Router } from '@angular/router';
import { AuthService } from '@auth/services/auth.service';

@Component({
  selector: 'app-sign-in-page',
  imports: [ReactiveFormsModule],
  templateUrl: './sign-in-page.component.html',
})
export class SignInPageComponent {

  fb = inject(FormBuilder);
  formUtils = inject(FormUtilsService);
  passwordUtils = inject(PasswordUtilsService);
  router = inject(Router);

  authService = inject(AuthService);

  isLoading = signal(false);

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

    console.log('Intentando iniciar sesión con:', { usernameOrEmail, password });
    this.authService.signIn(usernameOrEmail.toLowerCase(), password!)
      .subscribe((isAuthenticated) => {
        if (isAuthenticated) {
          console.log('Inicio de sesión exitoso');
          this.resetForm();
          this.isLoading.set(false);
        } else {
          console.error('Inicio de sesión fallido');
          this.isLoading.set(false);
        }
      },
        (error) => {
          console.error('Error en el inicio de sesión:', error);
          this.isLoading.set(false);
          if (error && error.status === 401) {
            console.log('Credenciales inválidas');
          } else if (error && error.error && error.error.message) {
            console.error('Mensaje de error del servidor:', error.error.message);
          } else {
            console.error('Ocurrió un error inesperado al iniciar sesión.');
          }
        }
      );
  }

  resetForm() {
    this.formUtils.resetForm(this.signInForm);
  }

  navigateToSignUp() {
    this.router.navigate(['/auth/sign-up']);
  }
}
