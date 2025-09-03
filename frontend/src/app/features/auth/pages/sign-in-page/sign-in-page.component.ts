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
import { IconComponent } from '@shared/components/icon/icon.component';
import { firstValueFrom } from 'rxjs';

@Component({
    selector: 'app-sign-in-page',
    imports: [ReactiveFormsModule, LoadingComponent, FeedbackMessageComponent, IconComponent],
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

    async submitForm() {
        this.feedbackService.clearMessage();

        if (this.signInForm.invalid) {
            this.signInForm.markAllAsTouched();
            return;
        }

        const { usernameOrEmail, password } = this.signInForm.value;
        if (!this.validateCredentials(usernameOrEmail, password)) {
            return;
        }

        if (!usernameOrEmail || !password) {
            this.showError('Invalid credentials, please check your username/email and password');
            return;
        }

        this.isLoading.set(true);
        this.hasError.set(false);
        this.feedbackService.clearMessage();

        try {
            const signInResponse = await firstValueFrom(
                this.authService.signIn(usernameOrEmail, password)
            );

            if (signInResponse.authStatus === 'authenticated') {
                this.handleSuccessfulSignIn(signInResponse);
            } else {
                this.handleSignInError(signInResponse.message || 'Sign-in failed');
            }
        } catch (error: any) {
            this.handleSignInError(error.message || 'An error occurred during sign-in');
        } finally {
            this.isLoading.set(false);
        }
    }

    private validateCredentials(usernameOrEmail: string | undefined, password: string | undefined): boolean {
        if (!usernameOrEmail || !password) {
            this.showError('Invalid credentials, please check your username/email and password');
            return false;
        }

        const usernameOrEmailControl = this.signInForm.get('usernameOrEmail')!;
        const passwordControl = this.signInForm.get('password')!;

        const usernameValidation = this.formUtils.validateUsername(usernameOrEmailControl);
        if (usernameValidation) {
            this.showError('Invalid credentials, please check your username/email and password');
            return false;
        }

        const passwordValidation = this.passwordUtils.passwordValidator(passwordControl);
        if (passwordValidation) {
            this.showError('Invalid credentials, please check your username/email and password');
            return false;
        }

        return true;
    }

    private handleSuccessfulSignIn(authData: any): void {
        this.feedbackService.showSuccess('Sign-in successful!');
        this.router.navigate(['/dashboard']);
    }

    private handleSignInError(errorMessage: string): void {
        this.hasError.set(true);
        this.showError(errorMessage);
    }

    private showError(message: string): void {
        this.feedbackService.showError(message);
    }

    resetForm() {
        this.hasError.set(false);
        this.feedbackService.clearMessage();
        this.signInForm.reset();
    }

    // navigateToSignUp() {
    //   this.router.navigate(['/auth/sign-up']);
    // }
}
