// auth-console-login-section.component.ts 
import {Component, EventEmitter, Output} from "@angular/core";
import {AuthInputType} from "../auth-input/models/AuthInputType";
import {ButtonText} from "../ss-button/models/ButtonText";
import {ElementSize} from "../../../models/ElementSize";
import {LoginCredentials} from "../../../models/auth/LoginCredentials";

@Component({
    selector: 'auth-console-login-section',
    templateUrl: './auth-console-login-section.component.html',
    styleUrls: ['./auth-console-login-section-style.component.css']
})
export class AuthConsoleLoginSectionComponent {
    loginCredentials: LoginCredentials = new LoginCredentials();
    @Output() loginButtonClicked: EventEmitter<LoginCredentials> = new EventEmitter<LoginCredentials>();
    constructor() {
        
    }
    emitFields() {

    }
    protected readonly AuthInputType = AuthInputType;
    protected readonly ButtonText = ButtonText;
    protected readonly ElementSize = ElementSize;
}
