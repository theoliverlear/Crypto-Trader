// trade-checkout.component.ts
import {Component, Input} from "@angular/core";
import {DisplayCurrency} from "../../../../models/currency/types";
import {BuyType} from "../buy-type/models/BuyType";
import {ElementSize, InputType, TagType} from "@theoliverlear/angular-suite";

@Component({
    selector: 'trade-checkout',
    templateUrl: './trade-checkout.component.html',
    styleUrls: ['./trade-checkout.component.scss'],
    standalone: false
})
export class TradeCheckoutComponent {
    @Input() displayCurrency: DisplayCurrency;
    protected buyType: BuyType = BuyType.DOLLARS;
    constructor() {
        
    }
    
    setBuyType(buyType: BuyType): void {
        this.buyType = buyType;
    }
    
    getYouPayText(): string {
        // TODO: Implement this and price WebSocket.
        return "$0.00";
    }
    
    getYouGetText(): string {
        // TODO: Implement this and price WebSocket.
        return "1.255 BTC";
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