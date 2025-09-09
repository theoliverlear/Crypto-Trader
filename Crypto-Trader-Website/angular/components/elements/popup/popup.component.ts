// popup.component.ts 
import {Component, Input} from "@angular/core";
import {AuthPopup, TagType} from "@theoliverlear/angular-suite";
import {NgIf} from "@angular/common";

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

    protected readonly TagType = TagType;
    protected readonly AuthPopup = AuthPopup;
}
