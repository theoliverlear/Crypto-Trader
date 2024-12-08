// auth-input.component.ts 
import {Component, Input} from "@angular/core";
import {AuthInputType} from "./models/AuthInputType";
import {InputType} from "../ss-input/models/InputType";

@Component({
    selector: 'auth-input',
    templateUrl: './auth-input.component.html',
    styleUrls: ['./auth-input-style.component.css']
})
export class AuthInputComponent {
    @Input() authInputType: AuthInputType;
    constructor() {
        
    }
    getInputType() {
        switch (this.authInputType) {
            case AuthInputType.USERNAME:
                return InputType.TEXT;
            case AuthInputType.PASSWORD:
            case AuthInputType.CONFIRM_PASSWORD:
                return InputType.PASSWORD;
        }
    }
}
