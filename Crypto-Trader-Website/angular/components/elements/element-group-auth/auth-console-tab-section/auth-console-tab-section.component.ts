// auth-console-tab-section.component.ts 
import {Component, EventEmitter, Output} from "@angular/core";
import {AuthType} from "../../../../models/auth/AuthType";
import {AuthPopup} from "../../../../models/auth/AuthPopup";

@Component({
    selector: 'auth-console-tab-section',
    templateUrl: './auth-console-tab-section.component.html',
    styleUrls: ['./auth-console-tab-section.component.scss']
})
export class AuthConsoleTabSectionComponent {
    @Output() authTypeClicked: EventEmitter<AuthType> = new EventEmitter<AuthType>();
    constructor() {
        
    }
    emitAuthTypeClicked(authType: AuthType): void {
        this.authTypeClicked.emit(authType);
    }
    setAuthType(authType: AuthType): void {
        this.emitAuthTypeClicked(authType);
    }

    protected readonly AuthType = AuthType;
    protected readonly AuthPopup = AuthPopup;
}
