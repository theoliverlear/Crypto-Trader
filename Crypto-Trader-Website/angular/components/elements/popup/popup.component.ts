// popup.component.ts 
import {Component, Input} from "@angular/core";
import {AuthPopup, TagType} from "@theoliverlear/angular-suite";

type PossibleAuthPopup = AuthPopup | null;

@Component({
    selector: 'popup',
    standalone: false,
    templateUrl: './popup.component.html',
    styleUrls: ['./popup.component.scss']
})
export class PopupComponent {
    @Input() popupMessage: PossibleAuthPopup = null;
    constructor() {
        
    }

    shouldDisplay(): boolean {
        let isNoneMessage: boolean = (this.popupMessage as AuthPopup) !== AuthPopup.NONE;
        return this.popupMessage && isNoneMessage
    }
    
    protected readonly TagType = TagType;
    protected readonly AuthPopup = AuthPopup;
}
