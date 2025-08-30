// popup.component.ts 
import {Component, Input} from "@angular/core";
import {AuthPopup} from "@theoliverlear/angular-suite";

@Component({
    selector: 'popup',
    templateUrl: './popup.component.html',
    styleUrls: ['./popup.component.scss']
})
export class PopupComponent {
    @Input() popupMessage: AuthPopup | null = null;
    constructor() {
        
    }
}
