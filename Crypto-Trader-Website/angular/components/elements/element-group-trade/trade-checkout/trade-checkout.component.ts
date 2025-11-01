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
    protected buyType: BuyType = BuyType.DOLLARS;
    constructor(private currencyFormatter: CurrencyFormatterService) {
        
    }

    setFromInput(input: any): void {
        const inputAsNumber: number = Number(input);
        if (isNaN(inputAsNumber)) {
            return;
        }
        if (this.buyType === BuyType.DOLLARS) {
            this.numDollars = inputAsNumber;
        } else {
            this.numShares = inputAsNumber;
        }
    }

    setBuyType(buyType: BuyType): void {
        this.buyType = buyType;
    }
    
    getYouPayText(): string {
        if (this.buyType === BuyType.SHARES) {
            const numDollars: number = this.displayCurrency.value * this.numShares;
            return this.currencyFormatter.formatCurrency(numDollars);
        } else {
            return this.currencyFormatter.formatCurrency(this.numDollars);
        }
    }
    
    getYouGetText(): string {
        if (this.buyType === BuyType.SHARES) {
            const numDecimals: number = this.getNumDecimals(this.numShares);
            return this.numShares.toFixed(numDecimals) + " " + this.displayCurrency.currencyCode;
        } else {
            const numShares: number = this.numDollars / this.displayCurrency.value;
            const numDecimals: number = this.getNumDecimals(numShares);
            return String(numShares.toFixed(numDecimals)) + " " + this.displayCurrency.currencyCode;
        }
    }

    getNumDecimals(numShares: number): number {
        return numShares < 1 ? 10 : 0;
    }

    getPlaceholderText(): string {
        if (this.buyType === BuyType.DOLLARS) {
            return "# of Dollars";
        } else {
            return "# of Shares";
        }
    }

    protected readonly ElementSize = ElementSize;
    protected readonly InputType = InputType;
    protected readonly TagType = TagType;
}