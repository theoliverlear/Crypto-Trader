// asset-field.component.ts
 import {Component, HostBinding, Input} from "@angular/core";
import {PortfolioAssetFieldType} from "./models/PortfolioAssetFieldType";
import {TagType} from "@theoliverlear/angular-suite";
import {PortfolioAsset} from "../../../../models/portfolio/types";
import {
    SharesFormatterService
} from "../../../../services/ui/shares-formatter.service";
import {
    CurrencyFormatterService
} from "../../../../services/ui/currency-formatter.service";
import {
    TimeFormatterService
} from "../../../../services/ui/time-formatter.service";

@Component({
    selector: 'asset-field',
    templateUrl: './asset-field.component.html',
    styleUrls: ['./asset-field.component.scss'],
    standalone: false
})
export class AssetFieldComponent {
    @Input() fieldType: PortfolioAssetFieldType;
    @Input() asset: PortfolioAsset;
    @Input() showFieldTitle: boolean = true;
    @HostBinding("class.highlighted-field") get isHighlighted(): boolean {
        return this.fieldType === PortfolioAssetFieldType.TOTAL_VALUE ||
               this.fieldType === PortfolioAssetFieldType.SHARES;
    }
    constructor(private sharesFormatter: SharesFormatterService,
                private currencyFormatter: CurrencyFormatterService,
                private timeFormatter: TimeFormatterService) {

    }

    getFieldText(): string {
        switch (this.fieldType) {
            case PortfolioAssetFieldType.CURRENCY_NAME:
                return this.getCurrencyName();
            case PortfolioAssetFieldType.SHARES:
                return this.sharesFormatter.formatShares(this.asset.shares,
                    this.asset.currencyCode);
            case PortfolioAssetFieldType.TARGET_PRICE:
                return this.currencyFormatter.formatCurrency(this.asset.targetPrice);
            case PortfolioAssetFieldType.LAST_UPDATED:
                return this.getFormattedLastUpdated();
            case PortfolioAssetFieldType.VENDOR_NAME:
                return this.asset.vendorName;
            case PortfolioAssetFieldType.TOTAL_VALUE:
                return this.currencyFormatter.formatCurrency(this.asset.totalValueInDollars);
            default:
                throw new Error("Unknown PortfolioAssetFieldType: " + this.fieldType);
        }
    }

    private getCurrencyName(): string {
        return this.asset.currencyName + " (" + this.asset.currencyCode + ")";
    }

    private getFormattedLastUpdated() {
        return this.timeFormatter.formatTime(this.asset.lastUpdated).replace(",", "");
    }

    protected readonly TagType = TagType;
}