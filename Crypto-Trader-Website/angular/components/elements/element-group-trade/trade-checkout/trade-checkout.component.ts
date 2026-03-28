// trade-checkout.component.ts
import { Component, Input } from '@angular/core';

import { ElementSize, InputType, TagType } from '@theoliverlear/angular-suite';
import { CurrencyFormatterService } from '@ui/currency-formatter.service';
import { DisplayCurrency } from '@models/currency/types';

import { BuyType } from '../buy-type/models/BuyType';

@Component({
    selector: 'trade-checkout',
    templateUrl: './trade-checkout.component.html',
    styleUrls: ['./trade-checkout.component.scss'],
    standalone: false,
})
export class TradeCheckoutComponent {
    @Input() public displayCurrency: DisplayCurrency;
    protected numDollars: number = 0;
    protected numShares: number = 0;
    protected numFeeDollars: number = 0;
    protected buyType: BuyType = BuyType.DOLLARS;
    protected showCheckoutPanel: boolean = false;
    constructor(private readonly currencyFormatter: CurrencyFormatterService) {}

    protected readonly ElementSize: typeof ElementSize = ElementSize;
    protected readonly InputType: typeof InputType = InputType;
    protected readonly TagType: typeof TagType = TagType;
}
