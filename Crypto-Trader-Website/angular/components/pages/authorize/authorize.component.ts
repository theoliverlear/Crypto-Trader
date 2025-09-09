import {Component} from "@angular/core";
import {AuthPopup} from "@theoliverlear/angular-suite";

@Component({
    selector: 'authorize',
    standalone: false,
    templateUrl: './authorize.component.html',
    styleUrls: ['./authorize.component.scss']
})
export class AuthorizeComponent {
    constructor() {

    }

    protected readonly AuthPopup = AuthPopup;
}