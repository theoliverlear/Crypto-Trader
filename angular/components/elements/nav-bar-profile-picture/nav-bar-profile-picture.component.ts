import {Component} from "@angular/core";

@Component({
    selector: 'nav-bar-profile-picture',
    templateUrl: './nav-bar-profile-picture.component.html',
    styleUrls: ['./nav-bar-profile-picture-style.component.css']
})
export class NavBarProfilePictureComponent {
    constructor() {
        console.log('NavBarProfilePictureComponent loaded');
    }
}