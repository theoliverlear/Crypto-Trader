// popup.component.ts
import { Component, Input } from '@angular/core';

import { AuthPopup, TagType } from '@theoliverlear/angular-suite';

type PossibleAuthPopup = AuthPopup | null;

/** A popup box with a message.
 *
 */
@Component({
    selector: 'popup',
    standalone: false,
    templateUrl: './popup.component.html',
    styleUrls: ['./popup.component.scss'],
})
export class PopupComponent {
    @Input() protected popupMessage: PossibleAuthPopup = null;
    constructor() {}

    /** Determines whether the popup should be displayed.
     * @returns If message is missing or explicitly set to none.
     */
    protected shouldDisplay(): boolean {
        const isNoneMessage: boolean =
            (this.popupMessage as AuthPopup) !== AuthPopup.NONE;
        return this.popupMessage !== null && isNoneMessage;
    }

    protected readonly TagType: typeof TagType = TagType;
    protected readonly AuthPopup: typeof AuthPopup = AuthPopup;
}
