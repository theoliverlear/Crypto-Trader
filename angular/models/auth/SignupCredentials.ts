import {LoginCredentials} from "./LoginCredentials";

export class SignupCredentials extends LoginCredentials {
    email: string;
    confirmPassword: string;
    agreedTerms: boolean;
    constructor(username: string = "",
                email: string = "",
                password: string = "",
                confirmPassword: string = "",
                agreedTerms: boolean = false) {
        super(username, password);
        this.email = email;
        this.confirmPassword = confirmPassword;
        this.agreedTerms = agreedTerms;
    }
}