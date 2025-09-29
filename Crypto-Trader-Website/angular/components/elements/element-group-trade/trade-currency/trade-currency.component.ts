// trade-currency.component.ts
import {Component, Input, OnInit} from "@angular/core";
import {
    CurrencyValueWebsocketService
} from "../../../../services/net/websocket/currency-value-websocket.service";
import {WebSocketCapable} from "@theoliverlear/angular-suite";
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
class TradeCurrencyComponent implements WebSocketCapable, OnInit {
    @Input() selectedCurrency: Currency;
    currencyValue: number = 0;
    imageAsset: ImageAsset = defaultCurrencyIcon;
    constructor(private currencyWebSocket: CurrencyValueWebsocketService,
                private currencyImageService: CurrencyImageService) {

    }
    
    ngOnInit() {
        this.setImageAsset();
    }

    async setImageAsset(): Promise<void> {
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
    
    webSocketSubscriptions: Record<string, Subscription>;
    initializeWebSockets(): void {
        throw new Error("Method not implemented.");
    }

}

export default TradeCurrencyComponent