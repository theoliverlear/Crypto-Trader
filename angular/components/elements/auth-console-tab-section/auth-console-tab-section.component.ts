// auth-console-tab-section.component.ts 
import { Component } from "@angular/core";
import {AuthType} from "../../../models/auth/AuthType";

@Component({
    selector: 'auth-console-tab-section',
    templateUrl: './auth-console-tab-section.component.html',
    styleUrls: ['./auth-console-tab-section-style.component.css']
})
export class AuthConsoleTabSectionComponent {
    constructor() {
        
    }

    protected readonly AuthType = AuthType;
}
