import { Component } from '@angular/core';

import { upgradeElementLink } from '@assets/elementLinkAssets';

@Component({
    selector: 'nav-upgrade',
    templateUrl: './nav-upgrade.component.html',
    styleUrls: ['./nav-upgrade.component.scss'],
    standalone: false,
})
export class NavUpgradeComponent {
    constructor() {}

    protected readonly upgradeElementLink = upgradeElementLink;
}
