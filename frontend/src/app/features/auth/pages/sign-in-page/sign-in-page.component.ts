import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormUtilsService } from '@shared/services/form-utils.service';
import { PasswordUtilsService } from '@shared/services/password-utils.service';
import { Router } from '@angular/router';
import { AuthService } from '@auth/services/auth.service';
import { AppInitService } from '@core/services/app-init.service';

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
    this.errorMessage.set(null); // Limpiar errores anteriores

    console.log('Intentando iniciar sesión con:', { usernameOrEmail, password });
    this.authService.signIn(usernameOrEmail.toLowerCase(), password!)
      .subscribe((isAuthenticated) => {
        if (isAuthenticated) {
          console.log('Inicio de sesión exitoso, iniciando TokenRefreshService y redirigiendo');
          this.resetForm();
          this.isLoading.set(false);
          this.appInitService.startTokenRefresh(); // Iniciar TokenRefreshService
          this.router.navigateByUrl('/'); // Redirigir a la página principal
        } else {
          console.error('Inicio de sesión fallido');
          this.errorMessage.set('Credenciales inválidas. Por favor, verifica tu usuario y contraseña.');
          this.isLoading.set(false);
        }
      },
        (error) => {
          console.error('Error en el inicio de sesión:', error);
          this.isLoading.set(false);
          if (error && error.status === 401) {
            this.errorMessage.set('Credenciales inválidas. Por favor, verifica tu usuario y contraseña.');
          } else if (error && error.error && error.error.message) {
            this.errorMessage.set(error.error.message);
          } else {
            this.errorMessage.set('Ocurrió un error inesperado al iniciar sesión. Por favor, intenta de nuevo.');
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
