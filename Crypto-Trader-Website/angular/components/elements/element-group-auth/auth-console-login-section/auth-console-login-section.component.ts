// auth-console-login-section.component.ts 
import {Component, EventEmitter, Output} from "@angular/core";
import {AuthInputType} from "../auth-input/models/AuthInputType";
import {LoginCredentials} from "../../../../models/auth/LoginCredentials";
import {ButtonText, ElementSize} from "@theoliverlear/angular-suite";

@Component({
    selector: 'auth-console-login-section',
    templateUrl: './auth-console-login-section.component.html',
    styleUrls: ['./auth-console-login-section.component.scss']
})
export class AuthConsoleLoginSectionComponent {
    loginCredentials: LoginCredentials = new LoginCredentials();
    @Output() loginButtonClicked: EventEmitter<LoginCredentials> = new EventEmitter<LoginCredentials>();
    constructor() {
        
    }
    emitFields() {

    }
    protected readonly AuthInputType = AuthInputType;
    protected readonly ElementSize = ElementSize;
    protected readonly ButtonText = ButtonText;
}
