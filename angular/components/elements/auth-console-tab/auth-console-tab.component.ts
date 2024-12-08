// auth-console-tab.component.ts 
import {Component, Input} from "@angular/core";
import {AuthType} from "../../../models/auth/AuthType";

@Component({
    selector: 'auth-console-tab',
    templateUrl: './auth-console-tab.component.html',
    styleUrls: ['./auth-console-tab-style.component.css']
})
export class AuthConsoleTabComponent {
    @Input() authType: AuthType;
    constructor() {
        
    }
}
