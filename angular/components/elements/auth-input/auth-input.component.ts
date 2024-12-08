// auth-input.component.ts 
import {Component, Input, OnInit} from "@angular/core";
import {AuthInputType} from "./models/AuthInputType";
import {InputType} from "../ss-input/models/InputType";

@Component({
    selector: 'auth-input',
    templateUrl: './auth-input.component.html',
    styleUrls: ['./auth-input-style.component.css']
})
export class AuthInputComponent implements OnInit {
    @Input() authInputType: AuthInputType;
    @Input() authTypeString: string = "";
    inputText: string = "";
    constructor() {
        
    }
    updateInputText(text: string): void {
        this.inputText = text;
    }
    ngOnInit() {
        if (this.authInputType !== AuthInputType.AGREED_TERMS) {
            this.authTypeString = this.authInputType + ":";
        } else {
            this.authTypeString = this.authInputType;
        }
    }
    getInputType() {
        switch (this.authInputType) {
            case AuthInputType.USERNAME:
                return InputType.TEXT;
            case AuthInputType.EMAIL:
                return InputType.EMAIL;
            case AuthInputType.PASSWORD:
            case AuthInputType.CONFIRM_PASSWORD:
                return InputType.PASSWORD;
            case AuthInputType.AGREED_TERMS:
                return InputType.CHECKBOX;
            default:
                return InputType.TEXT;
        }
    }
}
