import {Component} from "@angular/core";
import {navBarAccountLink} from "../../../../assets/elementLinkAssets";

@Component({
    selector: 'nav-bar-profile-picture',
    standalone: false,
    templateUrl: './nav-bar-profile-picture.component.html',
    styleUrls: ['./nav-bar-profile-picture.component.scss']
})
export class NavBarProfilePictureComponent {
    constructor() {
        
    }

    protected readonly navBarAccountLink = navBarAccountLink;
}