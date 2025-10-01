// buy-type.component.ts
import {
    Component,
    EventEmitter,
    HostListener,
    Input,
    Output
} from "@angular/core";
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
    @Output() buyTypeSelectedEvent: EventEmitter<BuyType> = new EventEmitter<BuyType>();
    constructor() {
        
    }
    
    @HostListener('click')
    onClick(): void {
        this.buyTypeSelectedEvent.emit(this.buyType);
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