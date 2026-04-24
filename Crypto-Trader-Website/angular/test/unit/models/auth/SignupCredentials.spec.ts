import { AuthPopup } from '@theoliverlear/angular-suite';
import { SignupCredentials } from '@models/auth/SignupCredentials';

describe('SignupCredentials', () => {
    let signupCredentials: SignupCredentials;
    beforeEach(() => {
        signupCredentials = new SignupCredentials();
    });
    describe('isValidEmail', () => {
        it('should detect email validity', () => {
            const invalidEmails: string[] = ['invalid-email', 'ollie@', 'ollie.com'];
            for (const invalidEmail of invalidEmails) {
                signupCredentials.email = invalidEmail;
                expect(signupCredentials.isValidEmail()).toBeFalsy();
            }
            const validEmails: string[] = [
                'ollie@gmail.com',
                'hello@sscryptotrader.com',
                'dotted.email@emailed.dotted.com',
            ];
            for (const validEmail of validEmails) {
                signupCredentials.email = validEmail;
                expect(signupCredentials.isValidEmail()).toBeTruthy();
            }
        });
    });
    describe('getSignupRequest', () => {
        const password: string = 'password';
        const confirmPassword: string = 'password';
        const email: string = 'invalid-email';
        const agreedTerms: boolean = true;
        signupCredentials = new SignupCredentials(email, password, confirmPassword, agreedTerms);
        it('should not allow invalid request generation', () => {
            expect(() => signupCredentials.getSignupRequest()).toThrowError(
                'Cannot get signup request due to input issues.',
            );
        });
    });
    describe('getAnyIssue', () => {
        it('should return an issue if present', () => {
            const password: string = 'password';
            const confirmPassword: string = 'password';
            const email: string = 'invalid-email';
            const agreedTerms: boolean = true;
            signupCredentials = new SignupCredentials(
                email,
                password,
                confirmPassword,
                agreedTerms,
            );
            expect(signupCredentials.getAnyIssue()).toBe(AuthPopup.INVALID_EMAIL);
            const validEmail: string = 'valid@email.com';
            signupCredentials.email = validEmail;
            expect(signupCredentials.getAnyIssue()).toBe(AuthPopup.NONE);
        });
    });
    describe('isPasswordMatch', () => {
        it('should return false if passwords do not match', () => {
            const password: string = 'password';
            const confirmPassword: string = 'different-password';
            const email: string = 'invalid-email';
            const agreedTerms: boolean = true;
            signupCredentials = new SignupCredentials(
                email,
                password,
                confirmPassword,
                agreedTerms,
            );
            expect(signupCredentials.isPasswordMatch()).toBe(false);
        });
        it('should return true if passwords are empty', () => {
            const password: string = '';
            const confirmPassword: string = '';
            const email: string = 'invalid-email';
            const agreedTerms: boolean = true;
            signupCredentials = new SignupCredentials(
                email,
                password,
                confirmPassword,
                agreedTerms,
            );
            expect(signupCredentials.isPasswordMatch()).toBe(true);
        });
        it('should return true if passwords match', () => {
            const password: string = 'password';
            const confirmPassword: string = 'password';
            const email: string = 'invalid-email';
            const agreedTerms: boolean = true;
            signupCredentials = new SignupCredentials(
                email,
                password,
                confirmPassword,
                agreedTerms,
            );
            expect(signupCredentials.isPasswordMatch()).toBe(true);
        });
    });
});
