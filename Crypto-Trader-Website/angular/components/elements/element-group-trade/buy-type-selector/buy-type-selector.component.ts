// buy-type-selector.component.ts
import { Component, EventEmitter, Output } from '@angular/core';

import { BuyType } from '../buy-type/models/BuyType';

@Component({
    selector: 'buy-type-selector',
    templateUrl: './buy-type-selector.component.html',
    styleUrls: ['./buy-type-selector.component.scss'],
    standalone: false,
})
export class BuyTypeSelectorComponent {
    @Output() public buyTypeSelectedEvent: EventEmitter<BuyType> = new EventEmitter<BuyType>();
    constructor() {}

    public emitBuyType(buyType: BuyType): void {
        this.buyTypeSelectedEvent.emit(buyType);
    }

    protected readonly BuyType = BuyType;
}
