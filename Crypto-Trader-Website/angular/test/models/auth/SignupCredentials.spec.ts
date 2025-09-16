import {SignupCredentials} from "../../../models/auth/SignupCredentials";

describe('SignupCredentials', () => {
    let signupCredentials: SignupCredentials;
    beforeEach(() => {
        signupCredentials = new SignupCredentials();
    })
    describe('isValidEmail', () => {
        it('should detect email validity', () => {
            const invalidEmails: string[] = [
                'invalid-email',
                "ollie@",
                "ollie.com"
            ];
            for (let invalidEmail of invalidEmails) {
                signupCredentials.email = invalidEmail;
                expect(signupCredentials.isValidEmail()).toBeFalsy();
            }
            const validEmails: string[] = [
                "ollie@gmail.com",
                "hello@sscryptotrader.com"
            ];
            for (let validEmail of validEmails) {
                signupCredentials.email = validEmail;
                expect(signupCredentials.isValidEmail()).toBeTruthy();
            }
        });
    });
    describe('getSignupRequest', () => {
        const password: string = "password";
        const confirmPassword: string = "password";
        const email: string = "invalid-email";
        const username: string = "ollie";
        const agreedTerms: boolean = true;
        signupCredentials = new SignupCredentials(username, email, password, confirmPassword, agreedTerms);
        it('should not allow invalid request generation', () => {
            expect(() => signupCredentials.getSignupRequest()).toThrowError(
                "Cannot get signup request due to input issues.")
        });
    });
});