// trade-checkout.component.ts
import {Component, Input} from "@angular/core";
import {DisplayCurrency} from "../../../../models/currency/types";

@Component({
    selector: 'trade-checkout',
    templateUrl: './trade-checkout.component.html',
    styleUrls: ['./trade-checkout.component.scss'],
    standalone: false
})
export class TradeCheckoutComponent {
    @Input() displayCurrency: DisplayCurrency;
    constructor() {
        
    }
}