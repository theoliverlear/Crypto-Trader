import {Component, Input} from "@angular/core";
import {TextElementLink} from "../../../models/link/TextElementLink";

@Component({
    selector: 'nav-bar-item',
    templateUrl: './nav-bar-item.component.html',
    styleUrls: ['./nav-bar-item-style.component.scss']
})
export class NavBarItemComponent {
    @Input() elementLink: TextElementLink;
    constructor() {

    }
}