// buy-type-selector.component.ts
import {Component, EventEmitter, Output} from "@angular/core";
import {BuyType} from "../buy-type/models/BuyType";

@Component({
    selector: 'buy-type-selector',
    templateUrl: './buy-type-selector.component.html',
    styleUrls: ['./buy-type-selector.component.scss'],
    standalone: false
})
export class BuyTypeSelectorComponent {
    @Output() buyTypeSelectedEvent: EventEmitter<BuyType> = new EventEmitter<BuyType>();
    constructor() {
        
    }
    
    emitBuyType(buyType: BuyType) {
        this.buyTypeSelectedEvent.emit(buyType);
    }

    protected readonly BuyType = BuyType;
}