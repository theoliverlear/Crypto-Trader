import {
    AuthPopup,
    EmailValidatorService,
    FilledFieldsService,
    HashPasswordService,
} from '@theoliverlear/angular-suite';

import { LoginCredentials } from './LoginCredentials';
import { type SignupRequest } from './types';

export class SignupCredentials extends LoginCredentials {
    private _confirmPassword: string;
    private _agreedTerms: boolean;
    constructor(
        email: string = '',
        password: string = '',
        confirmPassword: string = '',
        agreedTerms: boolean = false,
    ) {
        super(email, password);
        this.email = email;
        this._confirmPassword = confirmPassword;
        this._agreedTerms = agreedTerms;
    }

    public get confirmPassword(): string {
        return this._confirmPassword;
    }

    public set confirmPassword(confirmPassword: string) {
        this._confirmPassword = confirmPassword;
    }

    public get agreedTerms(): boolean {
        return this._agreedTerms;
    }

    public set agreedTerms(agreedTerms: boolean) {
        this._agreedTerms = agreedTerms;
    }

    public isValidEmail(): boolean {
        const emailValidator: EmailValidatorService = new EmailValidatorService();
        return emailValidator.isValidEmail(this.email);
    }

    public isPasswordMatch(): boolean {
        if (this.password === '' || this._confirmPassword === '') {
            return true;
        }
        return this.password === this.confirmPassword;
    }

    public isAgreedTerms(): boolean {
        return this._agreedTerms;
    }

    public isFilledFields(): boolean {
        const filledFieldsService: FilledFieldsService = new FilledFieldsService();
        return filledFieldsService.isFilledFields([
            this.email,
            this.password,
            this._confirmPassword,
        ]);
    }

    public getAnyTypingIssue(): AuthPopup {
        if (!this.isValidEmail()) {
            return AuthPopup.INVALID_EMAIL;
        }
        if (!this.isPasswordMatch()) {
            return AuthPopup.PASSWORDS_DONT_MATCH;
        }
        return AuthPopup.NONE;
    }

    public getSubmitIssue(): AuthPopup {
        if (!this.isFilledFields()) {
            return AuthPopup.FILL_ALL_FIELDS;
        }
        if (!this.isAgreedTerms()) {
            return AuthPopup.AGREE_TERMS;
        }
        return AuthPopup.NONE;
    }

    public getAnyIssue(): AuthPopup {
        const submitIssue: AuthPopup = this.getSubmitIssue();
        if (submitIssue !== AuthPopup.NONE) {
            return submitIssue;
        }

        const typingIssue: AuthPopup = this.getAnyTypingIssue();
        if (typingIssue !== AuthPopup.NONE) {
            return typingIssue;
        }
        return AuthPopup.NONE;
    }

    public getSignupRequest(): SignupRequest {
        if (this.getAnyIssue() !== AuthPopup.NONE) {
            throw new Error('Cannot get signup request due to input issues.');
        }
        const hashPasswordService: HashPasswordService = new HashPasswordService();
        return {
            email: this.email,
            password: hashPasswordService.hashPassword(this.password),
        };
    }
}
