// auth-console-signup-section.component.ts 
import {Component, EventEmitter, Output} from "@angular/core";
import {AuthInputType} from "../auth-input/models/AuthInputType";
import {ButtonText, ElementSize} from "@theoliverlear/angular-suite";
import {SignupCredentials} from "../../../../models/auth/SignupCredentials";

@Component({
    selector: 'auth-console-signup-section',
    standalone: false,
    templateUrl: './auth-console-signup-section.component.html',
    styleUrls: ['./auth-console-signup-section.component.scss']
})
export class AuthConsoleSignupSectionComponent {
    signupCredentials: SignupCredentials = new SignupCredentials();
    @Output() signupButtonClicked: EventEmitter<SignupCredentials> = new EventEmitter<SignupCredentials>();
    constructor() {
        
    }
    updateUsername(username: string): void {
        this.signupCredentials.username = username;
    }
    updateEmail(email: string): void {
        this.signupCredentials.email = email;
    }
    updatePassword(password: string): void {
        this.signupCredentials.password = password;
    }
    updateConfirmPassword(confirmPassword: string): void {
        this.signupCredentials.confirmPassword = confirmPassword;
    }
    emitFields(): void {
        this.signupButtonClicked.emit(this.signupCredentials);
    }


    protected readonly AuthInputType = AuthInputType;
    protected readonly ElementSize = ElementSize;
    protected readonly ButtonText = ButtonText;
}
