import {Component, OnInit} from "@angular/core";
import {AuthPopup, WebSocketCapable} from "@theoliverlear/angular-suite";
@Component({
    selector: 'authorize',
    standalone: false,
    templateUrl: './authorize.component.html',
    styleUrls: ['./authorize.component.scss']
})
export class AuthorizeComponent {
    authPopup: AuthPopup = AuthPopup.NONE;
    constructor() {

    }
    
    setAuthPopup(authPopup: AuthPopup): void {
        this.authPopup = authPopup;
    }
    


    protected readonly AuthPopup = AuthPopup;
}