// trade-currency.component.ts
import {
    Component,
    Input,
    OnChanges,
    OnInit,
    SimpleChanges
} from "@angular/core";
import {
    CurrencyValueWebsocketService
} from "../../../../services/net/websocket/currency-value-websocket.service";
import {TagType, WebSocketCapable} from "@theoliverlear/angular-suite";
import { Subscription } from "rxjs";
import {
    CurrencyImageService
} from "../../../../services/ui/currency-image.service";
import {
    defaultCurrencyIcon,
    ImageAsset
} from "../../../../assets/imageAssets";
import {Currency, DisplayCurrency} from "../../../../models/currency/types";

@Component({
    selector: 'trade-currency',
    templateUrl: './trade-currency.component.html',
    styleUrls: ['./trade-currency.component.scss'],
    standalone: false
})
class TradeCurrencyComponent implements OnInit, OnChanges {
    @Input() selectedCurrency: Currency;
    currencyValue: number = 0;
    imageAsset: ImageAsset = defaultCurrencyIcon;
    constructor(private currencyImageService: CurrencyImageService) {

    }

    ngOnChanges(changes: SimpleChanges) {
        const curr = changes['selectedCurrency'];
        if (!curr || !curr.currentValue) return;

        const prevCode = curr.previousValue?.currencyCode;
        const nextCode = curr.currentValue.currencyCode;
        if (prevCode === nextCode) return;

        // Defer to avoid blocking current CD cycle (optional)
        Promise.resolve().then(() => this.setImageAsset());
    }

    ngOnInit() {
        this.setImageAsset();
    }

    async setImageAsset(): Promise<void> {
        if (!this.selectedCurrency) {
            return;
        }
        const displayCurrency: DisplayCurrency = {
            currencyCode: this.selectedCurrency.currencyCode,
            currencyName: this.selectedCurrency.currencyName,
            value: this.currencyValue,
            logoUrl: this.getImageUrl(this.selectedCurrency.currencyCode)
        }
        const imageAsset: ImageAsset = await this.currencyImageService.resolveImageAsset(displayCurrency);
        this.imageAsset = imageAsset;
    }
    
    private getImageUrl(currencyCode: string): string {
        return `/assets/cryptofont/${currencyCode.toLowerCase()}.svg`;
    }
    

    protected readonly TagType = TagType;
}

export default TradeCurrencyComponent