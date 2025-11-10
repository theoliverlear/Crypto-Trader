// trade-checkout.component.ts
import {Component, Input} from "@angular/core";
import {DisplayCurrency} from "../../../../models/currency/types";
import {BuyType} from "../buy-type/models/BuyType";
import {ElementSize, InputType, TagType} from "@theoliverlear/angular-suite";
import {
    CurrencyFormatterService
} from "../../../../services/ui/currency-formatter.service";

@Component({
    selector: 'trade-checkout',
    templateUrl: './trade-checkout.component.html',
    styleUrls: ['./trade-checkout.component.scss'],
    standalone: false
})
export class TradeCheckoutComponent {
    @Input() displayCurrency: DisplayCurrency;
    numDollars: number = 0;
    numShares: number = 0;
    numFeeDollars: number = 0;
    protected buyType: BuyType = BuyType.DOLLARS;
    protected showCheckoutPanel: boolean = false;
    constructor(private currencyFormatter: CurrencyFormatterService) {
        
    }

    protected readonly ElementSize = ElementSize;
    protected readonly InputType = InputType;
    protected readonly TagType = TagType;
}