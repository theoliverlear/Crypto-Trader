// auth-input.component.ts
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

import { InputType, TagType } from '@theoliverlear/angular-suite';

import { AuthInputType } from './models/AuthInputType';

/** A text input for authentication.
 *
 */
@Component({
    selector: 'auth-input',
    standalone: false,
    templateUrl: './auth-input.component.html',
    styleUrls: ['./auth-input.component.scss'],
})
export class AuthInputComponent implements OnInit {
    @Input() public authInputType: AuthInputType;
    @Input() public authTypeString: string = '';
    @Output() public inputChange: EventEmitter<string> = new EventEmitter<string>();
    protected inputText: string = '';
    constructor() {}
    /** Emits the input text to the parent component.
     *
     */
    protected emitInputText(): void {
        this.inputChange.emit(this.inputText);
    }
    /**
     * Updates the input text and emits it.
     * @param text The new input text.
     */
    public updateInputText(text: string): void {
        this.inputText = text;
        this.emitInputText();
    }
    /**
     * On init, initialize the auth text string.
     */
    public ngOnInit(): void {
        this.initAuthText();
    }

    private initAuthText(): void {
        if (this.authInputType !== AuthInputType.AGREED_TERMS) {
            this.authTypeString = `${this.authInputType}:`;
        } else {
            this.authTypeString = this.authInputType;
        }
        this.authTypeString = this.authTypeString.toUpperCase();
    }

    /**
     * Returns the input type based on the auth input type.
     * @returns The corresponding InputType.
     */
    public getInputType(): InputType {
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

    protected readonly TagType: typeof TagType = TagType;
}
