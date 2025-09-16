// nav-console.component.ts
import { Component } from "@angular/core";
import {consoleElementLink} from "../../../../assets/elementLinkAssets";
import {consoleIcon} from "../../../../assets/imageAssets";

@Component({
    selector: 'nav-console',
    templateUrl: './nav-console.component.html',
    styleUrls: ['./nav-console.component.scss'],
    standalone: false
})
export class NavConsoleComponent {
    constructor() {
        
    }

    protected readonly consoleElementLink = consoleElementLink;
    protected readonly consoleIcon = consoleIcon;
}