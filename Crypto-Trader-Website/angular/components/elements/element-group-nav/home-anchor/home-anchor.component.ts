// home-anchor.component.ts
import { Component } from '@angular/core';

import { homeElementLink, navBarHomeLink } from '@assets/elementLinkAssets';
import { transparentLogo } from '@assets/imageAssets';

@Component({
    selector: 'home-anchor',
    templateUrl: './home-anchor.component.html',
    styleUrls: ['./home-anchor.component.scss'],
    standalone: false,
})
export class HomeAnchorComponent {
    constructor() {}

    protected readonly transparentLogo = transparentLogo;
    protected readonly homeElementLink = homeElementLink;
}
