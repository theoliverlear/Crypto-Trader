import {Component, Input} from "@angular/core";
import {AuthType} from "../../../models/auth/AuthType";
import {AuthPopup} from "../../../models/auth/AuthPopup";

@Component({
    selector: 'auth-console',
    templateUrl: './auth-console.component.html',
    styleUrls: ['./auth-console.component.css']
})
export class AuthConsoleComponent {
    @Input() currentAuthType: AuthType = AuthType.SIGN_UP;
    username: string = "";
    email: string = "";
    password: string = "";
    confirmPassword: string = "";
    constructor() {

    }
    setAuthType(authType: AuthType): void {
        this.currentAuthType = authType;
    }
    isSignupSection(): boolean {
        return this.currentAuthType === AuthType.SIGN_UP;
    }
    isLoginSection(): boolean {
        return this.currentAuthType === AuthType.LOGIN;
    }

    protected readonly AuthPopup = AuthPopup;
}