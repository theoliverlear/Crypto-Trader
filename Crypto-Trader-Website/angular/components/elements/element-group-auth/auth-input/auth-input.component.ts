// auth-input.component.ts 
import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {AuthInputType} from "./models/AuthInputType";
import {InputType} from "@theoliverlear/angular-suite"

@Component({
    selector: 'auth-input',
    standalone: false,
    templateUrl: './auth-input.component.html',
    styleUrls: ['./auth-input.component.scss']
})
export class AuthInputComponent implements OnInit {
    @Input() authInputType: AuthInputType;
    @Input() authTypeString: string = "";
    @Output() inputChange: EventEmitter<string> = new EventEmitter<string>();
    inputText: string = "";
    constructor() {
        
    }
    emitInputText(): void {
        this.inputChange.emit(this.inputText);
    }
    updateInputText(text: string): void {
        this.inputText = text;
        this.emitInputText();
    }
    ngOnInit(): void {
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
