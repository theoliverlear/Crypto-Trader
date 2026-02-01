import { Component } from '@angular/core';

import { ElementSize, TagType } from '@theoliverlear/angular-suite';
import { getStartedElementLink } from '@assets/elementLinkAssets';
import { transparentLogo } from '@assets/imageAssets';
import { cryptoTraderDescription } from '@assets/textAssets';

@Component({
    selector: 'home',
    standalone: false,
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss'],
})
export class HomeComponent {
    constructor() {}

    protected readonly TagType = TagType;
    protected readonly transparentLogo = transparentLogo;
    protected readonly cryptoTraderDescription = cryptoTraderDescription;
    protected readonly crypto = crypto;
    protected readonly ElementSize = ElementSize;
    protected readonly getStartedElementLink = getStartedElementLink;
}
