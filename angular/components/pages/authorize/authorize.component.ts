import {Component} from "@angular/core";
import {AuthPopup} from "../../../models/auth/AuthPopup";

@Component({
    selector: 'authorize',
    templateUrl: './authorize.component.html',
    styleUrls: ['./authorize-style.component.css']
})
export class AuthorizeComponent {
    constructor() {

    }

    protected readonly AuthPopup = AuthPopup;
}