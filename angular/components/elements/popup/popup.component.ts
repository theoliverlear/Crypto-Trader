// popup.component.ts 
import {Component, Input} from "@angular/core";
import {AuthPopup} from "../../../models/auth/AuthPopup";

@Component({
    selector: 'popup',
    templateUrl: './popup.component.html',
    styleUrls: ['./popup.component.css']
})
export class PopupComponent {
    @Input() popupMessage: AuthPopup | null = null;
    constructor() {
        
    }
}
