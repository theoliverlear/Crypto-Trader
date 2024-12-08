import {Component, Input} from "@angular/core";
import {AuthType} from "../../../models/auth/AuthType";

@Component({
    selector: 'auth-console',
    templateUrl: './auth-console.component.html',
    styleUrls: ['./auth-console-style.component.css']
})
export class AuthConsoleComponent {
    @Input() currentAuthType: AuthType;
    constructor() {

    }
    isSignupSection(): boolean {
        return this.currentAuthType === AuthType.SIGNUP;
    }
    isLoginSection(): boolean {
        return this.currentAuthType === AuthType.LOGIN;
    }
}