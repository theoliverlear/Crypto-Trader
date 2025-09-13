import {Component, EventEmitter, Input, Output} from "@angular/core";
import {AuthPopup, AuthType} from "@theoliverlear/angular-suite";

@Component({
    selector: 'auth-console',
    standalone: false,
    templateUrl: './auth-console.component.html',
    styleUrls: ['./auth-console.component.scss']
})
export class AuthConsoleComponent {
    @Input() currentAuthType: AuthType = AuthType.SIGN_UP;
    @Output() authPopupEvent: EventEmitter<AuthPopup> = new EventEmitter<AuthPopup>();
    constructor() {

    }
    
    emitAuthPopup(authPopup: AuthPopup): void {
        this.authPopupEvent.emit(authPopup);
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