import { Component, Input } from '@angular/core';

import { TagType } from '@theoliverlear/angular-suite';

@Component({
    selector: 'start-trading-promo',
    standalone: false,
    templateUrl: './start-trading-promo.component.html',
    styleUrls: ['./start-trading-promo.component.scss'],
})
export class StartTradingPromoComponent {
    constructor() {}

    @Input() public useGradientBackground: boolean = true;

    protected readonly TagType = TagType;
}
