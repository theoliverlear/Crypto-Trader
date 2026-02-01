// auth-console-login-section.component.ts
import { Component, EventEmitter, Output } from '@angular/core';

import { ButtonText, ElementSize } from '@theoliverlear/angular-suite';
import { LoginCredentials } from '@models/auth/LoginCredentials';

import { AuthInputType } from '../auth-input/models/AuthInputType';

/** A section for login in the auth console.
 *
 */
@Component({
    selector: 'auth-console-login-section',
    standalone: false,
    templateUrl: './auth-console-login-section.component.html',
    styleUrls: ['./auth-console-login-section.component.scss'],
})
export class AuthConsoleLoginSectionComponent {
    protected loginCredentials: LoginCredentials = new LoginCredentials();
    @Output() protected loginButtonClicked: EventEmitter<LoginCredentials> =
        new EventEmitter<LoginCredentials>();
    constructor() {}

    protected emitFields(): void {
        this.loginButtonClicked.emit(this.loginCredentials);
    }

    protected updateEmail(email: string): void {
        this.loginCredentials.email = email;
    }

    protected updatePassword(password: string): void {
        this.loginCredentials.password = password;
    }

    protected readonly AuthInputType: typeof AuthInputType = AuthInputType;
    protected readonly ElementSize: typeof ElementSize = ElementSize;
    protected readonly ButtonText: typeof ButtonText = ButtonText;
}
