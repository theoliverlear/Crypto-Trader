// trader-event-bubble.component.ts 
import { Component, Input } from "@angular/core";
import {TradeEvent} from "../../../../models/trader/types";
import {defaultTradeEvent} from "../../../../assets/tradeEventAssets";
import {TagType} from "@theoliverlear/angular-suite";
import {
    CurrencyFormatterService
} from "../../../../services/ui/currency-formatter.service";
import {
    TimeFormatterService
} from "../../../../services/ui/time-formatter.service";
import {
    defaultCurrencyIcon,
    ImageAsset,
    paperIcon
} from "../../../../assets/imageAssets";
import {VendorOption} from "../../../../models/vendor/VendorOption";

@Component({
    selector: 'trader-event-bubble',
    standalone: false,
    templateUrl: './trader-event-bubble.component.html',
    styleUrls: ['./trader-event-bubble.component.scss']
})
export class TraderEventBubbleComponent {
    @Input() tradeEvent: TradeEvent = defaultTradeEvent;
    constructor(private currencyFormatter: CurrencyFormatterService,
                private timeFormatter: TimeFormatterService) {
        
    }

    shouldLoadImage(): boolean {
        return this.getCurrencyImageAsset() !== defaultCurrencyIcon;
    }
    
    // TODO: Make into dedicated Angular service.
    getCurrencyImageAsset(): ImageAsset {
        try {
            return {
                src: `/assets/cryptofont/${this.getCurrencyCode().toLowerCase()}.svg`,
                alt: this.getCurrencyName() + " icon"
            }
        } catch (error) {
            return defaultCurrencyIcon;
        }
    }
    
    getVendorImageAsset(): ImageAsset {
        try {
            const vendorOption: VendorOption = VendorOption.from(this.tradeEvent.vendor);
            return VendorOption.getImageAsset(vendorOption);
        } catch (error) {
            return paperIcon;
        }
    }
    
    getValueChange(): string {
        let valueChange: number = this.tradeEvent.valueChange;
        if (valueChange < 1) {
            valueChange = Number(valueChange.toFixed(2));
        }
        if (valueChange < 0.01) {
            return "< +¢1"
        }
        const formatedCurrency: string = this.currencyFormatter.formatCurrency(valueChange);
        return "+" + formatedCurrency;
    }
    
    getTradeType(): string {
        return this.tradeEvent.tradeType.toUpperCase();
    }
    
    getTradeTime(): string {
        let formatedTime: string = this.timeFormatter.formatTime(this.tradeEvent.tradeTime, true);
        formatedTime = formatedTime.split(",")[1];
        return formatedTime;
    }
    
    getTradeDay(): string {
        let formatedTime: string = this.timeFormatter.formatTime(this.tradeEvent.tradeTime, true);
        formatedTime = formatedTime.split(",")[0];
        return formatedTime;
    }
    
    getCurrencyName(): string {
        if (this.tradeEvent.currency !== "") {
            return this.tradeEvent.currency.split(" (")[0];
        } else {
            return "";
        }
    }
    
    getCurrencyCode(): string {
        if (this.tradeEvent.currency !== "") {
            return this.tradeEvent.currency.split(" (")[1].split(")")[0];
        } else {
            return "";
        }
    }
    
    hasNoChange(): boolean {
        return this.tradeEvent.sharesChange === 0 && this.tradeEvent.valueChange === 0;
    }
    
    getNoChangeText(): string {
        return "No Change";
    }
    
    getSharesChange(): string {
        let numDecimals: number = 2;
        if (this.tradeEvent.sharesChange < 1) {
            let sharesString: string = this.tradeEvent.sharesChange.toString().split(".")[1];
            if (sharesString !== undefined) {
                numDecimals = sharesString.length;
                for (let i = 0; i < numDecimals; i++) {
                    if (sharesString[i] !== "0") {
                        numDecimals = i + 1;
                        break;
                    }
                }
            }
        }
        const sharesFormatter = new Intl.NumberFormat('en-US', {
            style: 'decimal',
            minimumFractionDigits: 0,
            maximumFractionDigits: numDecimals
        });
        const sharesChange: number = sharesFormatter.format(this.tradeEvent.sharesChange) as unknown as number;
        return "+" + sharesChange.toString() + " " + this.getCurrencyCode();
    }
    
    isSharesChange(): boolean {
        return !this.isValueChange() && this.tradeEvent.sharesChange !== 0;
    }
    
    isValueChange(): boolean {
        return this.tradeEvent.valueChange !== 0;
    }
    
    protected readonly TagType = TagType;
}
