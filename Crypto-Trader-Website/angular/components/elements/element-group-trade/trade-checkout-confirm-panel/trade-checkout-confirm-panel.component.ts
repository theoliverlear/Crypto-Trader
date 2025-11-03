// trade-checkout-confirm-panel.component.ts
import { Component, Input } from "@angular/core";
import {DisplayCurrency} from "../../../../models/currency/types";

@Component({
    selector: 'trade-checkout-confirm-panel',
    templateUrl: './trade-checkout-confirm-panel.component.html',
    styleUrls: ['./trade-checkout-confirm-panel.component.scss'],
    standalone: false,
})
export class TradeCheckoutConfirmPanelComponent {
    @Input() numDollars: number;
    @Input() numShares: number;
    @Input() displayCurrency: DisplayCurrency;
    constructor() {
        
    }
}