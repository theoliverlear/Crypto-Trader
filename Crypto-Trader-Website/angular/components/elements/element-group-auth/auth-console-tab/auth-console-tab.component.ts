// auth-console-tab.component.ts
import { Component, EventEmitter, HostListener, Input, Output } from '@angular/core';

import { AuthType, TagType } from '@theoliverlear/angular-suite';

/** A tab that can be clicked to switch between auth types.
 *
 */
@Component({
    selector: 'auth-console-tab',
    standalone: false,
    templateUrl: './auth-console-tab.component.html',
    styleUrls: ['./auth-console-tab.component.scss'],
})
export class AuthConsoleTabComponent {
    @Input() public authType: AuthType;
    @Output() public authTabClicked: EventEmitter<AuthType> = new EventEmitter<AuthType>();
    constructor() {}
    /** Emits the auth type when clicked.
     *
     */
    protected emitAuthTabClicked(): void {
        this.authTabClicked.emit(this.authType);
    }
    /** Emits the auth type when clicked.
     *
     */
    @HostListener('click')
    protected onClick(): void {
        this.emitAuthTabClicked();
    }

    protected readonly TagType: typeof TagType = TagType;
}
