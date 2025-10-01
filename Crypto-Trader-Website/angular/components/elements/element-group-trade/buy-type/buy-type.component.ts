// buy-type.component.ts
import {Component, Input} from "@angular/core";
import {BuyType} from "./models/BuyType";
import {ElementSize} from "@theoliverlear/angular-suite";
import {
    coinIcon,
    dollarIcon,
    ImageAsset
} from "../../../../assets/imageAssets";

@Component({
    selector: 'buy-type',
    templateUrl: './buy-type.component.html',
    styleUrls: ['./buy-type.component.scss'],
    standalone: false
})
export class BuyTypeComponent {
    @Input() buyType: BuyType;
    constructor() {
        
    }
    
    getImageAsset(): ImageAsset {
        if (this.buyType === BuyType.DOLLARS) {
            return dollarIcon;
        } else {
            return coinIcon;
        }
    }

    protected readonly ElementSize = ElementSize;
}