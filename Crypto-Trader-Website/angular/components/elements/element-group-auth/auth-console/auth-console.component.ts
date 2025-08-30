import {Component, Input} from "@angular/core";
import {AuthPopup, AuthType} from "@theoliverlear/angular-suite";

@Component({
    selector: 'auth-console',
    templateUrl: './auth-console.component.html',
    styleUrls: ['./auth-console.component.scss']
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